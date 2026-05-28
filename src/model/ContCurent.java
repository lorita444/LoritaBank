package model;

import java.util.ArrayList;
import java.util.List;

public class ContCurent extends ContBancar {

    private List<Card> carduriAsociate;

    public ContCurent(Moneda moneda, Client titular) {
        super(moneda, titular);
        this.carduriAsociate = new ArrayList<>();
    }

    public List<Card> getCarduriAsociate() {
        return carduriAsociate;
    }
}
