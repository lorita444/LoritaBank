package repository;

import model.Client;
import model.Admin;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClientRepository {
    private static ClientRepository instance = null;

    private ClientRepository() {
    }
    public static ClientRepository getInstance() {
        if (instance == null) {
            instance = new ClientRepository();
        }
        return instance;
    }
    public void save(Client client) {
        String sql = "INSERT INTO clienti (nume, cnp, telefon, email, serie_nr_ci, data_expirare_ci, venit_declarat, data_deschidere, is_admin) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, client.getNume());
            stmt.setString(2, client.getCnp());
            stmt.setString(3, client.getTelefon());
            stmt.setString(4, client.getEmail());
            stmt.setString(5, client.getSerieNrCI());
            stmt.setDate(6, client.getDataExpirareCI() != null ? Date.valueOf(client.getDataExpirareCI()) : null);
            stmt.setDouble(7, client.getVenitDeclarat());
            stmt.setDate(8, client.getDataDeschidere() != null ? Date.valueOf(client.getDataDeschidere()) : null);
            stmt.setBoolean(9, client instanceof Admin);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    client.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Client> findAll() {
        List<Client> list = new ArrayList<>();
        String sql = "SELECT * FROM clienti";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id_client");
                String nume = rs.getString("nume");
                String cnp = rs.getString("cnp");
                String telefon = rs.getString("telefon");
                String email = rs.getString("email");
                String serieNrCi = rs.getString("serie_nr_ci");
                Date dataExpDate = rs.getDate("data_expirare_ci");
                LocalDate dataExp = dataExpDate.toLocalDate();
                double venit = rs.getDouble("venit_declarat");
                Date dataDeschidereDate = rs.getDate("data_deschidere");
                LocalDate dataDeschidere = dataDeschidereDate.toLocalDate();
                boolean isAdmin = rs.getBoolean("is_admin");

                Client client;
                if (isAdmin) {
                    client = new Admin(nume, cnp, telefon, email, serieNrCi, dataExp, venit);
                } else {
                    client = new Client(nume, cnp, telefon, email, serieNrCi, dataExp, venit);
                }
                
                client.setId(id);
                if (dataDeschidere != null) {
                    client.setDataDeschidere(dataDeschidere);
                }
                list.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (list.isEmpty()) {
            Admin defaultAdmin = new Admin(
                "Admin Principal", 
                "1111111111111", 
                "0000000000", 
                "admin@loritabank.ro", 
                "LR000000",
                LocalDate.now().plusYears(10), 
                0.0
            );
            save(defaultAdmin);
            list.add(defaultAdmin);
        }

        return list;
    }
}
