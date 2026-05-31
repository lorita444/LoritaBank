package model;

import java.time.LocalDate;

public class Tranzactie {
    private static int nextId = 1;

    private int idTranzactie;
    private double suma;
    private Moneda moneda;
    private ContBancar contSursa;
    private ContBancar contDestinatie;
    private LocalDate data;
    private String detalii;

    public Tranzactie(double suma, Moneda moneda, ContBancar contSursa, ContBancar contDestinatie, String detalii) {
        this.idTranzactie = nextId++;
        this.suma = suma;
        this.moneda = moneda;
        this.contDestinatie = contDestinatie;
        this.contSursa = contSursa;
        this.detalii = detalii;
        this.data = LocalDate.now();
    }

    public void afiseazaDetalii() {
        System.out.println("\n");
        System.out.println("Cod de referinta: " + this.idTranzactie);
        System.out.println("Data: " + this.data);
        System.out.println("Ordonator: " + this.contSursa.getTitular().getNume());
        System.out.println("Cont sursa: " + this.contSursa.getIban());
        System.out.println("Beneficiar: " + this.contDestinatie.getTitular().getNume());
        System.out.println("Cont destinatie: " + this.contDestinatie.getIban());
        System.out.println("Suma: " + this.suma + " RON");
        System.out.println("Detalii: " + this.detalii);
        System.out.println("\n");
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public ContBancar getContSursa() {
        return contSursa;
    }

    public ContBancar getContDestinatie() {
        return contDestinatie;
    }

    public int getId() {
        return idTranzactie;
    }

    public void setId(int id) {
        this.idTranzactie = id;
    }

    public double getSuma() {
        return suma;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public String getDetalii() {
        return detalii;
    }
}
