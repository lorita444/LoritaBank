package model;

import Interfaces.Dobandibil;

import java.time.LocalDate;

import static java.lang.Math.abs;

public class CardCredit extends Card implements Dobandibil {
    private final double limitaCredit;
    private double totalPlata = 0;
    private double minimPlata = 0;
    private double sumaUtilizata = 0;
    private double dobanda;
    private int ziScadenta = 15;

    public CardCredit(Client titular, double limitaCredit) {
        super(titular);
        this.limitaCredit = limitaCredit;
        this.dobanda = 25;
    }

    public CardCredit(Client titular, double limitaCredit, double dobanda) {
        super(titular);
        this.limitaCredit = limitaCredit;
        this.dobanda = dobanda;

    }

    @Override
    public double calculeazaDobanda() {
        int azi = LocalDate.now().getDayOfMonth();
        if (azi <= ziScadenta) {
            return 0;
        }
        double d = dobanda / 100 * (azi - ziScadenta);
        return d;
    }
    @Override
    public void aplicaDobanda() {
        if(!this.isActiv()) {
            System.out.println("Card blocat.");
            return;
        }

        if(LocalDate.now().getDayOfMonth() <= ziScadenta) {
            System.out.println("Nicio dobanda aplicata.");
            return;
        }

        if(totalPlata <= 0) {
            System.out.println("Nicio dobanda aplicata.");
            return;
        }

        double d = calculeazaDobanda();

        totalPlata += totalPlata * d;
        minimPlata = totalPlata * 0.1;

        System.out.println("Dobanda aplicata: " + d);
        System.out.println("Total plata: " + totalPlata);
        System.out.println("Minim plata: " + minimPlata);
    }

    @Override
    public void afiseazaDetalii() {
        super.afiseazaDetalii();

        System.out.println("Tip card: Credit");
        System.out.println("Limita credit: " + this.limitaCredit + " RON");
        System.out.println("Suma utilizata: " + this.totalPlata + " RON");
        System.out.println("Dobanda: " + this.dobanda + "%");
        System.out.println("\n");
    }

    @Override
    public boolean plateste(double suma) {
        if (!isActiv()) {
            System.out.println("Plata esuata. Card blocat.");
            return false;
        }
        if (suma <= 0) {
            System.out.println("Suma invalida.");
            return false;
        }
        if (this.totalPlata + suma > this.limitaCredit) {
            System.out.println("Limita depasita");
            return false;
        }

        this.totalPlata += suma;
        this.minimPlata = this.totalPlata * 0.1;
        return true;
    }

    public boolean ramburseaza(double suma) {
        if (!this.isActiv()) {
            System.out.println("Card blocat.");
            return false;
        }

        if (suma <= 0 || suma > this.totalPlata) {
            System.out.println("Suma invalida.");
            return false;
        }

        totalPlata -= suma;
        if (totalPlata <= 0) {
            totalPlata = 0;
            minimPlata = 0;
            System.out.println("Rambursare totala efectuata cu succes.");
        } else {
            minimPlata = totalPlata * 0.1;
            System.out.println("Rambursare partiala efectuata. Rest de plata: " + totalPlata + " RON.");
        }

        return true;
    }
}
