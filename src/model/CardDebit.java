package model;

public class CardDebit extends Card {
    private final ContCurent contAsociat;
    private double limita = 1500;

    public CardDebit(Client titular, ContCurent contAsociat) {
       super(titular);
       this.contAsociat = contAsociat;
    }

    public void setLimita(double limita) {
        this.limita = limita;
    }

    @Override
    public void afiseazaDetalii() {
        super.afiseazaDetalii();

        System.out.println("Tip dard: debit");
        System.out.println("Cont IBAN asociat: " + this.contAsociat.getIban());
        System.out.println("Limita Zilnica POS: " + this.limita + this.contAsociat.getMoneda()); // folosește numele variabilei tale din clasă
        System.out.println("/n");
    }
}
