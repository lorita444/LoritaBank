package model;

public class ContEconomii extends ContBancar {

    private float rataDobanda;

    public ContEconomii(String moneda,
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

}