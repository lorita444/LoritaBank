package service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class AuditService {

    private static final String FILE_PATH = "audit.csv";

    static {
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            try (FileWriter writer = new FileWriter(FILE_PATH, true)) {
                writer.write("Operatiune   Data   ID Client   Detalii\n");
            } catch (IOException e) {
                System.out.println("Eroare la initializarea fisierului");
            }
        }
    }

    public static void log(String actiune, String entitate, String detalii) {
        try (FileWriter writer = new FileWriter(FILE_PATH, true)) {
            writer.write(actiune + ", " + LocalDateTime.now() + ", " + entitate + ", " + detalii + "\n"
            );
        } catch (IOException e) {
            System.out.println("Eroare la scrierea in fisier");
        }
    }

}