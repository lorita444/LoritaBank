package model;

import java.time.LocalDate;

public class DepozitLaTermen  extends ContBancar {
    private double dobanda = 4.5;
    private int perioada;
    private static double sumaMinima = 100000;
    private LocalDate dataScadenta;

    public DepozitLaTermen(Moneda moneda, Client titular, int perioada, double suma) {
        if( suma < sumaMinima) {
            throw new IllegalArgumentException("Suma initiala este mai mica decat suma minima pentru depozit.");
        }
        super(moneda, titular);
        this.perioada = perioada;
        dataScadenta = LocalDate.now().plusMonths(perioada);

        depune(suma);
    }

    public double calculeazaDobanda() {
        return getSold() * this.dobanda / 100;
    }

    public double calculeazaSumaFinala() {
        double x = getSold();
        for(int i = 1; i < this.perioada; i++) {
           x += x * this.dobanda / 100;
        }
        return x;
    }

    @Override
    public void retrage(double suma) {
        if (LocalDate.now().isBefore(this.dataScadenta)) {
            throw new IllegalStateException("Nu poți retrage bani dintr-un depozit la termen înainte de data de scadență: " + this.dataScadenta);
        }
        super.retrage(suma);
    }
}
