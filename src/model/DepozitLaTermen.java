package model;

import Interfaces.Dobandibil;

import java.time.LocalDate;

public class DepozitLaTermen  extends ContBancar implements Dobandibil {
    private double dobanda = 4.5;
    private int perioada;
    private static double sumaMinima = 100000;
    private LocalDate dataScadenta;

    public DepozitLaTermen(Moneda moneda, Client titular, int perioada, double suma) {
        super(moneda, titular);
        if (suma < sumaMinima) {
            throw new IllegalArgumentException("Suma initiala este mai mica decat suma minima pentru depozit.");
        }
        this.perioada = perioada;
        dataScadenta = LocalDate.now().plusMonths(perioada);
        depune(suma);
    }

    @Override
    public void retrage(double suma) {
        if (LocalDate.now().isBefore(this.dataScadenta)) {
            throw new IllegalStateException("Nu poți retrage bani dintr-un depozit la termen înainte de data de scadență: " + this.dataScadenta);
        }

        super.retrage(suma);

    }

    @Override
    public double calculeazaDobanda() {
        return getSold() * this.dobanda / 100 * this.perioada;
    }

    @Override
    public void aplicaDobanda() {
        if (LocalDate.now().isBefore(this.dataScadenta)) {
            throw new IllegalStateException("Dobanda nu poate fi aplicata pana la scadenta.");
        }
        setSold(getSold() + calculeazaDobanda());
    }
}
