package model;

import Interfaces.Dobandibil;

public class ContEconomii extends ContBancar implements Dobandibil {

    private float rataDobanda;

    public ContEconomii(Moneda moneda,
                        Client titular) {

        super(moneda, titular);
        this.rataDobanda = 2.5f;
    }

    public ContEconomii(Moneda moneda,
                        Client titular,
                        float rataDobanda) {

        super(moneda, titular);
        this.rataDobanda = rataDobanda;
    }

    @Override
    public double calculeazaDobanda() {
        return getSold() * rataDobanda / 100;
    }

    @Override
    public void aplicaDobanda() {
        double d = calculeazaDobanda();
        setSold(getSold() + d);
    }

    public float getRataDobanda() {
        return rataDobanda;
    }

    public void setRataDobanda(float rataDobanda) {
        this.rataDobanda = rataDobanda;
    }
}