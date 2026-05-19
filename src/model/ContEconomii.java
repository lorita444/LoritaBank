package model;

public class ContEconomii extends ContBancar {

    private float rataDobanda = 2.5f;

    public ContEconomii(String moneda,
                        Client titular,
                        float rataDobanda) {

        super(moneda, titular);
        this.rataDobanda = rataDobanda;
    }
}