package model;

public class CardDebit extends Card {
    private String contAsociat;
    private int limiita = 1500;

    public CardDebit(Client titular, ContBancar contAsociat) {
       super(titular);
       this.contAsociat = contAsociat.toString();
    }

    public void setLimiita(int limiita) {
        this.limiita = limiita;
    }
}
