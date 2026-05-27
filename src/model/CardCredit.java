package model;

import java.time.LocalDate;

public class CardCredit extends Card{
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

    public boolean efectueazaPlata(double suma) {

        if(!isActiv()) {
            return false;
        }

        if(suma <= 0) {
            return false;
        }

        if(totalPlata + suma > limitaCredit) {
            return false;
        }

        totalPlata += suma;

        minimPlata = totalPlata * 0.1;

        return true;
    }

    public void aplicaDobandaLunara() {

        if(LocalDate.now().getDayOfMonth() >= ziScadenta) {
            return;
        }

        if(totalPlata <= 0) {
            return;
        }
        int azi = LocalDate.now().getDayOfMonth();
        double d = totalPlata * dobanda / 100 * (azi - ziScadenta);

        totalPlata += totalPlata * d;
    }
}
