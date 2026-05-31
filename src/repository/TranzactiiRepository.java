package repository;

import model.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TranzactiiRepository {
    private static TranzactiiRepository instance = null;

    private TranzactiiRepository() {
    }

    public static TranzactiiRepository getInstance() {
        if (instance == null) {
            instance = new TranzactiiRepository();
        }
        return instance;
    }
    public void save(Tranzactie t) {
        String sql = "INSERT INTO tranzactii (suma, id_cont_sursa, id_cont_destinatie, data_tranzactie, detalii, moneda) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDouble(1, t.getSuma());
            stmt.setInt(2, t.getContSursa().getId());
            stmt.setInt(3, t.getContDestinatie().getId());
            stmt.setDate(4, t.getData() != null ? Date.valueOf(t.getData()) : Date.valueOf(LocalDate.now()));
            stmt.setString(5, t.getDetalii());
            stmt.setString(6, t.getMoneda() != null ? t.getMoneda().name() : Moneda.RON.name());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    t.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Tranzactie> findAll(List<ContBancar> accounts) {
        List<Tranzactie> list = new ArrayList<>();
        String sql = "SELECT * FROM tranzactii";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id_tranzactie");
                double suma = rs.getDouble("suma");
                int idContSursa = rs.getInt("id_cont_sursa");
                int idContDest = rs.getInt("id_cont_destinatie");
                Date dataTrDate = rs.getDate("data_tranzactie");
                LocalDate data = dataTrDate != null ? dataTrDate.toLocalDate() : null;
                String detalii = rs.getString("detalii");
                String monedaStr = rs.getString("moneda");
                Moneda moneda = monedaStr != null ? Moneda.valueOf(monedaStr) : Moneda.RON;

                ContBancar contSursa = null;
                ContBancar contDest = null;
                for (ContBancar ct : accounts) {
                    if (ct.getId() == idContSursa) {
                        contSursa = ct;
                    }
                    if (ct.getId() == idContDest) {
                        contDest = ct;
                    }
                }
                
                if (contSursa != null && contDest != null) {
                    Tranzactie t = new Tranzactie(suma, moneda, contSursa, contDest, detalii);
                    t.setId(id);
                    if (data != null) {
                        t.setData(data);
                    }
                    
                    if (contSursa.getTitular() != null) {
                        contSursa.getTitular().getTranzactii().add(t);
                    }
                    if (contDest.getTitular() != null && contDest.getTitular() != contSursa.getTitular()) {
                        contDest.getTitular().getTranzactii().add(t);
                    }
                    list.add(t);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
