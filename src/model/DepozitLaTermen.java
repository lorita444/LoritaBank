package model;

import java.time.LocalDate;

public class DepozitLaTermen  extends ContBancar {
    private double dobanda = 4.5;
    private int perioada;
    private static double sumaMinima = 100000;
    private LocalDate dataScadenta;

    public DepozitLaTermen(String moneda, Client titular, int perioada, double suma) {
        if( suma < sumaMinima) {
            throw new IllegalArgumentException("Suma initiala este mai mica decat suma minima pentru depozit.");
        }
        super(moneda, titular);
        this.perioada = perioada;
        dataScadenta = LocalDate.now().plusMonths(perioada);

        depune(suma);
    }

    public double calculeazaDobanda() {
        return getSold() * dobanda / 100;
    }

    public double calculeazaSumaFinala() {
        double x = getSold();
        for(int i = 1; i < perioada; i++) {
           x = x * dobanda / 100;
        }
        return x;
    }
}
