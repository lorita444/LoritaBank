package model;

import exception.FonduriInsuficiente;
import exception.SumaInvalida;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public abstract class ContBancar {
    private static int nextId = 1;
    private int id_cont;

    private String iban;
    private double sold = 0;
    private Moneda moneda;

    private boolean activ = true;
    private LocalDate dataDeschidere;
    private LocalDate dataInchidere;

    private Client titular;
    private List<Client> mandatati;

    private static int nextIbanNumber = 67845;

    public ContBancar(Moneda moneda, Client titular) {
        id_cont = nextId++;
        this.moneda = moneda;
        this.dataDeschidere = LocalDate.now();
        this.titular = titular;
        this.iban = genereazaIBAN();
        this.mandatati = new ArrayList<>();
    }

    private static String genereazaIBAN() {
        String numar =
                String.format("%016d", nextIbanNumber++);

        int suma = 0;

        for(int i = 0; i < numar.length(); i++) {
            suma += numar.charAt(i) - '0';
        }

        int control = suma % 100;
        return "RO" + String.format("%02d", control) + "LORI" + numar;
    }

    private static boolean verificaIBAN(String iban) {
        if(iban == null) {
            return false;
        }

        if(iban.length() != 24) {
            return false;
        }

        if(!iban.startsWith("RO")) {
            return false;
        }

        if(!iban.substring(4, 8).equals("LORI")) {
            return false;
        }

        if(!(iban.charAt(2) >= '0' && iban.charAt(2) <= '9'
            && iban.charAt(3) >= '0' && iban.charAt(3) <= '9')) {
                return false;
        } else {
            int control =  (iban.charAt(2) - '0') * 10 + (iban.charAt(3) - '0');
            int suma = 0;
            for(int i = 8; i < 24; i++) {
                if(iban.charAt(i) >= '0' && iban.charAt(i) <= '9') {
                    suma += iban.charAt(i) - '0';
                } else return false;
            }

            if(control != suma % 100) {
                return false;
            }
        }

        return true;
    }

    public double getSold() {

        return sold;
    }

    public void setSold(double sold) {
        this.sold = sold;
    }

    public String getIban() {
        return iban;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public void depune(double suma) {
        if(suma < 0) {
            throw new SumaInvalida("Suma aleasa nu este valida.");
        } else {
            setSold(suma + this.sold);
        }
    }

    public void retrage(double suma) {
        if(suma < 0) {
            throw new SumaInvalida("Suma aleasa nu este valida.");
        }

        if(this.sold < suma) {
            throw new FonduriInsuficiente("Fonduri insuficiente.");
        }

        setSold(this.sold - suma);
    }

    public Client getTitular() {
        return titular;
    }

    public int getId() {
        return id_cont;
    }

    public void setId(int id) {
        this.id_cont = id;
    }

    public LocalDate getDataDeschidere() {
        return dataDeschidere;
    }

    public void setDataDeschidere(LocalDate dataDeschidere) {
        this.dataDeschidere = dataDeschidere;
    }

    public LocalDate getDataInchidere() {
        return dataInchidere;
    }

    public void setDataInchidere(LocalDate dataInchidere) {
        this.dataInchidere = dataInchidere;
    }

    public boolean isActiv() {
        return activ;
    }

    public void setActiv(boolean activ) {
        this.activ = activ;
    }
}
