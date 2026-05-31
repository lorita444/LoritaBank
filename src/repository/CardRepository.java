package repository;

import model.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CardRepository {
    private static CardRepository instance = null;

    private CardRepository() {
    }
    public static CardRepository getInstance() {
        if (instance == null) {
            instance = new CardRepository();
        }
        return instance;
    }
    public void save(Card card) {
        String sql = "INSERT INTO carduri (pan, activ, tip_card, limita_credit, id_cont, id_client, nume_titular, pin, cvv, data_emitere, data_expirare, limita, total_plata, minim_plata) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, card.getFullPan());
            stmt.setBoolean(2, card.isActiv());
            
            String tipCard = "DEBIT";
            double limitaCredit = 0;
            Integer idCont = null;
            double limita = 0;
            double totalPlata = 0;
            double minimPlata = 0;
            
            if (card instanceof CardDebit) {
                tipCard = "DEBIT";
                limita = ((CardDebit) card).getLimita();
                if (((CardDebit) card).getContAsociat() != null) {
                    idCont = ((CardDebit) card).getContAsociat().getId();
                }
            } else if (card instanceof CardCredit) {
                tipCard = "CREDIT";
                limitaCredit = ((CardCredit) card).getLimitaCredit();
                totalPlata = ((CardCredit) card).getTotalPlata();
                minimPlata = ((CardCredit) card).getMinimPlata();
            }
            
            stmt.setString(3, tipCard);
            stmt.setDouble(4, limitaCredit);
            if (idCont != null) {
                stmt.setInt(5, idCont);
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            stmt.setInt(6, card.getTitular().getId());
            stmt.setString(7, card.getNumeTitular());
            stmt.setString(8, card.getRawPin());
            stmt.setString(9, card.getCvv());
            stmt.setDate(10, card.getDataEmitere() != null ? Date.valueOf(card.getDataEmitere()) : null);
            stmt.setDate(11, card.getDataExpirare() != null ? Date.valueOf(card.getDataExpirare()) : null);
            stmt.setDouble(12, limita);
            stmt.setDouble(13, totalPlata);
            stmt.setDouble(14, minimPlata);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    card.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStatusOrPin(Card card) {
        String sql = "UPDATE carduri SET activ = ?, pin = ?, limita = ? WHERE id_card = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, card.isActiv());
            stmt.setString(2, card.getRawPin());
            double limita = 0;
            if (card instanceof CardDebit) {
                limita = ((CardDebit) card).getLimita();
            }
            stmt.setDouble(3, limita);
            stmt.setInt(4, card.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCreditData(CardCredit card) {
        String sql = "UPDATE carduri SET total_plata = ?, minim_plata = ? WHERE id_card = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, card.getTotalPlata());
            stmt.setDouble(2, card.getMinimPlata());
            stmt.setInt(3, card.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Card> findAll(List<Client> clients, List<ContBancar> accounts) {
        List<Card> list = new ArrayList<>();
        String sql = "SELECT * FROM carduri";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id_card");
                String pan = rs.getString("pan");
                boolean activ = rs.getBoolean("activ");
                String tipCard = rs.getString("tip_card");
                double limitaCredit = rs.getDouble("limita_credit");
                int idCont = rs.getInt("id_cont");
                int idClient = rs.getInt("id_client");
                String numeTitular = rs.getString("nume_titular");
                String pin = rs.getString("pin");
                String cvv = rs.getString("cvv");
                Date dataEmitereDate = rs.getDate("data_emitere");
                LocalDate dataEmitere = dataEmitereDate != null ? dataEmitereDate.toLocalDate() : null;
                Date dataExpirareDate = rs.getDate("data_expirare");
                LocalDate dataExpirare = dataExpirareDate != null ? dataExpirareDate.toLocalDate() : null;
                double limita = rs.getDouble("limita");
                double totalPlata = rs.getDouble("total_plata");
                double minimPlata = rs.getDouble("minim_plata");

                // Find titular client
                Client titular = null;
                for (Client c : clients) {
                    if (c.getId() == idClient) {
                        titular = c;
                        break;
                    }
                }
                if (titular == null) continue;

                Card card = null;
                if ("DEBIT".equalsIgnoreCase(tipCard)) {
                    ContCurent contAsociat = null;
                    for (ContBancar ct : accounts) {
                        if (ct.getId() == idCont && ct instanceof ContCurent) {
                            contAsociat = (ContCurent) ct;
                            break;
                        }
                    }
                    if (contAsociat != null) {
                        CardDebit cardDebit = new CardDebit(titular, contAsociat);
                        cardDebit.setLimita(limita);
                        card = cardDebit;
                        contAsociat.getCarduriAsociate().add(cardDebit);
                    }
                } else if ("CREDIT".equalsIgnoreCase(tipCard)) {
                    CardCredit cardCredit = new CardCredit(titular, limitaCredit);
                    cardCredit.setTotalPlata(totalPlata);
                    cardCredit.setMinimPlata(minimPlata);
                    card = cardCredit;
                }

                if (card != null) {
                    card.setId(id);
                    card.setActiv(activ);
                    card.setPan(pan);
                    card.setPin(pin);
                    card.setCvv(cvv);
                    if (numeTitular != null) card.setNumeTitular(numeTitular);
                    if (dataEmitere != null) card.setDataEmitere(dataEmitere);
                    if (dataExpirare != null) card.setDataExpirare(dataExpirare);
                    
                    titular.getCarduri().add(card);
                    list.add(card);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
