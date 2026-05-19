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
    private ContBancar contAsociat;

    private String pin;
    private String cvv;

    private LocalDate dataEmitere;
    private LocalDate dataExpirare;

    private static long nextPan = 1L;

    public Card(Client titular, ContBancar contAsociat, String pin) {
        this.idCard = nextId++;
        this.titular = titular;
        this.contAsociat = contAsociat;
        this.numeTitular = titular.getNume();

        this.pan = genereazaPan();
        this.cvv = genereazaCVV();
        this.pin = pin;

        this.dataEmitere = LocalDate.now();
        this.dataExpirare = dataEmitere.plusYears(4);
        this.activ = true;
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

}
