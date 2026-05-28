package model;

import java.time.LocalDate;
import java.util.Random;

public abstract class Card {
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

        for(int i = 0; i < 4; i++) {
            pin = pin + String.valueOf(rand.nextInt(10));
        }

        return pin;

    }

    public void schimbaPin(String pin) {
        if (pin == null || pin.length() != 4 || !pin.matches("^[0-9]+")) {
            throw new IllegalArgumentException("PIN-ul trebuie să fie format din 4 cifre");
        }
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

    public void setActiv(boolean activ) {
        this.activ = activ;
    }

    public String getPan() {
        return pan.substring(pan.length() - 4);
    }
    public String getPIN() {
        return pin;
    }

    public String getFullPan() {
        return pan;
    }

    public Client getTitular() {
        return titular;
    }

    public void afiseazaDetalii() {
        System.out.println("\n");
        System.out.println("Titular: " + this.numeTitular);
        System.out.println("Numar Card: " + getPan());
        System.out.println("Activ: " + isActiv());
        System.out.println("Data Expirare: " + this.dataExpirare);
    }

    public abstract boolean plateste(double suma);

}
