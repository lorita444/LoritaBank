package model;

import java.time.LocalDate;

public class Admin extends Client {

    public Admin(String nume,
                 String cnp,
                 String telefon,
                 String email,
                 String serieNrCI,
                 LocalDate dataExpirareCI,
                 double venitDeclarat) {

        super(nume, cnp, telefon, email, serieNrCI, dataExpirareCI, venitDeclarat);
    }
}