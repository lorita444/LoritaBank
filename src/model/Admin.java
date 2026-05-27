package model;

import java.time.LocalDate;

public class Admin extends Client {

    public Admin(String username,
                 String parola,
                 String nume,
                 String cnp,
                 String telefon,
                 String email,
                 String serieNrCI,
                 LocalDate dataExpirareCI) {

        super(username, parola, nume, cnp, telefon, email, serieNrCI, dataExpirareCI);
    }
}