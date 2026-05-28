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

        System.out.println("Tip card: Debit");
        System.out.println("Cont IBAN asociat: " + this.contAsociat.getIban());
        System.out.println("Limita Zilnica POS: " + this.limita + " " + this.contAsociat.getMoneda());
        System.out.println("\n");
    }

    @Override
    public boolean plateste(double suma) {
        if (!isActiv()) {
            System.out.println("Plata esuata. Cardul este blocat.");
            return false;
        }
        if (suma <= 0) {
            System.out.println("Suma trebuie să fie pozitiva.");
            return false;
        }
        if (suma > this.limita) {
            System.out.println("Limita cardului depasita.");
            return false;
        }

        try {
            this.contAsociat.retrage(suma);
            return true;
        } catch (Exception e) {
            System.out.println("Tranzacție refuzată: " + e.getMessage());
            return false;
        }
    }
}
