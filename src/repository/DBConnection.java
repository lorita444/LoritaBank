package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3307/loritabank";
    private static final String USER = "root";
    private static final String PASSWORD = "mata";

    private static Connection connection = null;
    private static boolean schemaInitialized = false;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");

                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                if (!schemaInitialized) {
                    initializeSchema(connection);
                    schemaInitialized = true;
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Driverul JDBC nu a fost gasit.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Eroare la conectare");
            e.printStackTrace();
        }
        return connection;
    }

    private static void initializeSchema(Connection conn) {
        try {
            addColumnIfNotExists(conn, "clienti", "data_deschidere", "DATE");
            addColumnIfNotExists(conn, "clienti", "is_admin", "TINYINT(1) DEFAULT 0");
            
            addColumnIfNotExists(conn, "conturi", "data_deschidere", "DATE");
            addColumnIfNotExists(conn, "conturi", "data_inchidere", "DATE");
            addColumnIfNotExists(conn, "conturi", "rata_dobanda", "FLOAT DEFAULT 0");
            addColumnIfNotExists(conn, "conturi", "perioada", "INT DEFAULT 0");
            addColumnIfNotExists(conn, "conturi", "data_scadenta", "DATE");
            
            addColumnIfNotExists(conn, "carduri", "nume_titular", "VARCHAR(100)");
            addColumnIfNotExists(conn, "carduri", "pin", "VARCHAR(4)");
            addColumnIfNotExists(conn, "carduri", "cvv", "VARCHAR(3)");
            addColumnIfNotExists(conn, "carduri", "data_emitere", "DATE");
            addColumnIfNotExists(conn, "carduri", "data_expirare", "DATE");
            addColumnIfNotExists(conn, "carduri", "limita", "DOUBLE DEFAULT 0");
            addColumnIfNotExists(conn, "carduri", "total_plata", "DOUBLE DEFAULT 0");
            addColumnIfNotExists(conn, "carduri", "minim_plata", "DOUBLE DEFAULT 0");
            
            addColumnIfNotExists(conn, "tranzactii", "moneda", "VARCHAR(10)");
        } catch (Exception e) {
            System.err.println("Eroare la initializarea schemei auto-migrare:");
            e.printStackTrace();
        }
    }

    private static void addColumnIfNotExists(Connection conn, String tableName, String columnName, String columnDefinition) {
        String sql = "SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = 'loritabank' AND table_name = ? AND column_name = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tableName);
            pstmt.setString(2, columnName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    String alterSql = "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + columnDefinition;
                    try (Statement stmt = conn.createStatement()) {
                        stmt.executeUpdate(alterSql);
                        System.out.println("Coloana adaugata cu succes: " + tableName + "." + columnName);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Eroare la adaugarea coloanei " + columnName + " in " + tableName);
            e.printStackTrace();
        }
    }
}