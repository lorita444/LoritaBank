package model;

import java.time.LocalDate;

public class Tranzactie {
    private static int nextId = 1;

    private int idTranzactie;
    private double suma;
    private String contSursa;
    private String contDestinatie;
    private LocalDate data;
    private String detalii;

    public Tranzactie(double suma, String contAsociat, String contSursa, String contDestinatie, String detalii) {
        this.idTranzactie = nextId++;
        this.suma = suma;
        this.contDestinatie = contDestinatie;
        this.contSursa = contSursa;
        this.detalii = detalii;
        this.data = LocalDate.now();
    }
}
