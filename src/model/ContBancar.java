package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class ContBancar {
    private static int nextId = 1;
    private int idCont;

    private String iban;
    private double sold = 0;
    private String moneda;

    private boolean activ = true;
    private LocalDate dataDeschidere;
    private LocalDate dataInchidere;

    private Client titular;
    private List<Client> mandatati;

    private static int nextIbanNumber = 14567;

    public ContBancar(String moneda, Client titular) {
        idCont = nextId++;
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

    public void depune(double suma) {
        if (suma > 0) {
            sold += suma;
        }
    }
}
