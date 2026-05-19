package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private static int nextId = 1;

    private int idClient;
    private String username;
    private String parola;

    private String nume;
    private String cnp;
    private String telefon;
    private String email;
    private String serieNrCI;
    private LocalDate dataExpirareCI;
    private LocalDate dataDeschidere;
    private List<ContBancar> conturi;
    private List<Card> carduri;

    public Client(String username,
                  String parola,
                  String nume,
                  String cnp,
                  String telefon,
                  String email,
                  String serieNrCI,
                  LocalDate dataExpirareCI) {

        this.idClient = nextId++;
        this.username = username;
        this.parola = parola;
        this.nume = nume;
        this.cnp = cnp;
        this.telefon = telefon;
        this.email = email;
        this.serieNrCI = serieNrCI;
        this.dataExpirareCI = dataExpirareCI;
        this.dataDeschidere = LocalDate.now();
        this.conturi = new ArrayList<>();
        this.carduri = new ArrayList<>();
    }

    public String getNume() {
        return this.nume;
    }


}
