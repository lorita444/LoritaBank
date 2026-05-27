package model;

import java.util.ArrayList;
import java.util.List;

public class ContCurent extends ContBancar {

    private List<Card> carduriAsociate;

    public ContCurent(String moneda, Client titular) {
        super(moneda, titular);
        this.carduriAsociate = new ArrayList<>();
    }
}
