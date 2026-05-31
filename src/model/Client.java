package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private static int nextId = 1;

    private int id_client = 1;

    private String nume;
    private String cnp;
    private String telefon;
    private String email;
    private String serieNrCI;
    private LocalDate dataExpirareCI;
    private LocalDate dataDeschidere;
    private List<ContBancar> conturi;
    private List<Card> carduri;
    private List<Tranzactie> tranzactii;

    private double venitDeclarat;

    public Client(
            String nume,
            String cnp,
            String telefon,
            String email,
            String serieNrCI,
            LocalDate dataExpirareCI,
            double venitDeclarat) {

        this.id_client = nextId++;
        this.nume = nume;
        this.cnp = cnp;
        this.telefon = telefon;
        this.email = email;
        this.serieNrCI = serieNrCI;
        this.dataExpirareCI = dataExpirareCI;
        this.dataDeschidere = LocalDate.now();
        this.venitDeclarat = venitDeclarat;
        this.conturi = new ArrayList<>();
        this.carduri = new ArrayList<>();
        this.tranzactii = new ArrayList<>();
    }

    public String getNume() {
        return this.nume;
    }

    public List<ContBancar> getConturi() {
        return this.conturi;
    }

    public List<Card> getCarduri() {
        return this.carduri;
    }

    public double getVenitDeclarat() {
        return this.venitDeclarat;
    }

    public String getSerieNrCI() {
        return this.serieNrCI;
    }

    public List<Card> getCarduriActive() {
        List<Card> carduri = new ArrayList<>();
        for (Card card : this.carduri) {
            if (card.isActiv()) {
                carduri.add(card);
            }
        }
        return carduri;
    }

    public List<Tranzactie> getTranzactii() {
        return this.tranzactii;
    }

    public int getIdClient() {
        return this.id_client;
    }

    public void getDetalii() {
        System.out.println("CIF: " + this.id_client + "   Nume: " + this.nume);
    }

    public String getCnp() {
        return cnp;
    }

    public String getTelefon() {
        return telefon;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getDataExpirareCI() {
        return dataExpirareCI;
    }

    public int getId() {
        return id_client;
    }

    public void setId(int id) {
        this.id_client = id;
    }

    public LocalDate getDataDeschidere() {
        return dataDeschidere;
    }

    public void setDataDeschidere(LocalDate dataDeschidere) {
        this.dataDeschidere = dataDeschidere;
    }
}
