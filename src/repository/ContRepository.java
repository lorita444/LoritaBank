package repository;

import model.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ContRepository {
    private static ContRepository instance = null;

    private ContRepository() {
    }
    public static ContRepository getInstance() {
        if (instance == null) {
            instance = new ContRepository();
        }
        return instance;
    }
    public void save(ContBancar cont) {
        String sql = "INSERT INTO conturi (iban, sold, moneda, activ, tip_cont, id_client, data_deschidere, data_inchidere, rata_dobanda, perioada, data_scadenta) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, cont.getIban());
            stmt.setDouble(2, cont.getSold());
            stmt.setString(3, cont.getMoneda().name());
            stmt.setBoolean(4, cont.isActiv());
            
            String tipCont = "CURENT";
            float rataDobanda = 0.0f;
            int perioada = 0;
            Date dataScadenta = null;
            
            if (cont instanceof ContEconomii) {
                tipCont = "ECONOMII";
                rataDobanda = ((ContEconomii) cont).getRataDobanda();
            } else if (cont instanceof DepozitLaTermen) {
                tipCont = "DEPOZIT";
                perioada = ((DepozitLaTermen) cont).getPerioada();
                if (((DepozitLaTermen) cont).getDataScadenta() != null) {
                    dataScadenta = Date.valueOf(((DepozitLaTermen) cont).getDataScadenta());
                }
            }
            
            stmt.setString(5, tipCont);
            stmt.setInt(6, cont.getTitular().getId());
            stmt.setDate(7, cont.getDataDeschidere() != null ? Date.valueOf(cont.getDataDeschidere()) : null);
            stmt.setDate(8, cont.getDataInchidere() != null ? Date.valueOf(cont.getDataInchidere()) : null);
            stmt.setFloat(9, rataDobanda);
            stmt.setInt(10, perioada);
            stmt.setDate(11, dataScadenta);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    cont.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateSold(ContBancar cont) {
        String sql = "UPDATE conturi SET sold = ? WHERE id_cont = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, cont.getSold());
            stmt.setInt(2, cont.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(ContBancar cont) {
        cont.setActiv(false);
        cont.setDataInchidere(LocalDate.now());
        if (cont instanceof ContCurent contCurent) {
            for (Card c : contCurent.getCarduriAsociate()) {
                c.setActiv(false);
            }
        }

        String updateContSql = "UPDATE conturi SET activ = ?, data_inchidere = ? WHERE id_cont = ?";
        String updateCarduriSql = "UPDATE carduri SET activ = ? WHERE id_cont = ?";
        try (Connection conn = DBConnection.getConnection()) {
            boolean autoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try (PreparedStatement stmtCont = conn.prepareStatement(updateContSql);
                 PreparedStatement stmtCards = conn.prepareStatement(updateCarduriSql)) {
                
                stmtCont.setBoolean(1, false);
                stmtCont.setDate(2, Date.valueOf(cont.getDataInchidere()));
                stmtCont.setInt(3, cont.getId());
                stmtCont.executeUpdate();
                
                stmtCards.setBoolean(1, false);
                stmtCards.setInt(2, cont.getId());
                stmtCards.executeUpdate();
                
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(autoCommit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ContBancar> findAll(List<Client> clients) {
        List<ContBancar> list = new ArrayList<>();
        String sql = "SELECT * FROM conturi";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id_cont");
                String iban = rs.getString("iban");
                double sold = rs.getDouble("sold");
                Moneda moneda = Moneda.valueOf(rs.getString("moneda"));
                boolean activ = rs.getBoolean("activ");
                String tipCont = rs.getString("tip_cont");
                int idClient = rs.getInt("id_client");
                Date dataDeschidereDate = rs.getDate("data_deschidere");
                LocalDate dataDeschidere = dataDeschidereDate != null ? dataDeschidereDate.toLocalDate() : null;
                Date dataInchidereDate = rs.getDate("data_inchidere");
                LocalDate dataInchidere = dataInchidereDate != null ? dataInchidereDate.toLocalDate() : null;
                float rataDobanda = rs.getFloat("rata_dobanda");
                int perioada = rs.getInt("perioada");
                Date dataScadentaDate = rs.getDate("data_scadenta");
                LocalDate dataScadenta = dataScadentaDate != null ? dataScadentaDate.toLocalDate() : null;

                // Find titular client
                Client titular = null;
                for (Client c : clients) {
                    if (c.getId() == idClient) {
                        titular = c;
                        break;
                    }
                }
                if (titular == null) continue;

                ContBancar cont = null;
                if ("CURENT".equalsIgnoreCase(tipCont)) {
                    cont = new ContCurent(moneda, titular);
                } else if ("ECONOMII".equalsIgnoreCase(tipCont)) {
                    cont = new ContEconomii(moneda, titular, rataDobanda);
                } else if ("DEPOZIT".equalsIgnoreCase(tipCont)) {
                    double initialSuma = Math.max(sold, 100000.0);
                    cont = new DepozitLaTermen(moneda, titular, perioada, initialSuma);
                }

                if (cont != null) {
                    cont.setId(id);
                    cont.setSold(sold);
                    cont.setActiv(activ);
                    if (dataDeschidere != null) cont.setDataDeschidere(dataDeschidere);
                    if (dataInchidere != null) cont.setDataInchidere(dataInchidere);
                    if (cont instanceof DepozitLaTermen && dataScadenta != null) {
                        ((DepozitLaTermen) cont).setDataScadenta(dataScadenta);
                    }
                    
                    // Add cont to client's conturi list
                    titular.getConturi().add(cont);
                    list.add(cont);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
