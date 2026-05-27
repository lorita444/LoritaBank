package model;

import java.time.LocalDate;
import java.util.Random;

public class Card {
    private static int nextId = 1;

    private int idCard;
    private boolean activ;
    private String pan;
    private String numeTitular;
    private Client titular;

    private String pin;
    private String cvv;

    private LocalDate dataEmitere;
    private LocalDate dataExpirare;

    private static long nextPan = 1L;

    public Card(Client titular) {
        this.idCard = nextId++;
        this.titular = titular;
        this.numeTitular = titular.getNume();

        this.pan = genereazaPan();
        this.cvv = genereazaCVV();
        this.pin = genereaza_pin();

        this.dataEmitere = LocalDate.now();
        this.dataExpirare = dataEmitere.plusYears(4);
        this.activ = true;
    }

    private static String genereaza_pin() {
        Random rand = new Random();
        String pin = "";

        for(int i = 0; i < 10; i++) {
            pin = pin + String.valueOf(rand.nextInt(10));
        }

        return pin;

    }

    public void schimbaPin(String pin) {
        this.pin = pin;
    }

    private static String genereazaPan() {
        return "4" +
                String.format("%015d", nextPan++);
    }

    private static String genereazaCVV() {
        Random random = new Random();

        String cvv = "";
        for(int i = 0; i < 3; i++) {
            cvv += random.nextInt(10);
        }

        return cvv;
    }

    public boolean isActiv() {
        return activ;
    }
}
