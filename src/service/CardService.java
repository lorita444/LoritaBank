package service;

import exception.IdentificareEsuata;
import model.*;

import java.util.ArrayList;
import java.util.List;

public class CardService {
    List<Card> toateCardurileEver = new ArrayList<>();

    public void emiteCardDebit(Client client, ContCurent contAsociat) {
        CardDebit card = new CardDebit(client, contAsociat);
        client.getCarduri().add(card);

        toateCardurileEver.add(card);
        System.out.println("Cardul de debit a fost adaugat contului: " + contAsociat.getIban());
        AuditService.log("Emitere card debit", Integer.toString(client.getIdClient()), "Cardul" + card.getFullPan() );
    }

    public void emiteCardCredit(Client client, double limitaCredit) {
        if(limitaCredit <= 0 || limitaCredit > 3 * client.getVenitDeclarat()) {
            throw new IllegalArgumentException("Limita cardului de credit este in afara parametrilor acceptati.");
        }
        CardCredit card = new CardCredit(client, limitaCredit);
        toateCardurileEver.add(card);
        client.getCarduri().add(card);
        System.out.println("Cardul de credit a fost emis cu succes. Limită: " + limitaCredit + " RON");
        AuditService.log("Emitere card credit", Integer.toString(client.getIdClient()), "Cardul" + card.getFullPan() );
    }

    public void blocheazaCard(Card card) {
        if (!card.isActiv()) {
            throw new IllegalStateException("Cardul este deja blocat.");
        }

        card.setActiv(false);
        System.out.println("Cardul cu terminatia " + card.getPan() + " a fost blocat.");
        AuditService.log("Blocare card", Integer.toString(card.getTitular().getIdClient()), "Cardul" + card.getFullPan() );
    }

    public void deblocheazaCard(Card card) {
        if (card.isActiv()) {
            throw new IllegalStateException("Cardul este deja activ.");
        }

        card.setActiv(true);
        System.out.println("Cardul cu terminatia " + card.getPan() + " a fost deblocat.");
        AuditService.log("Deblocare card", Integer.toString(card.getTitular().getIdClient()), "Cardul" + card.getFullPan() );
    }

    public void schimbaPin(Card card, String pinNou, String serieNrCI) {
        if(!serieNrCI.equalsIgnoreCase(card.getTitular().getSerieNrCI())) {
            throw new IdentificareEsuata("Identificare esuata. Nu putem schimba PIN-ul");
        }
        AuditService.log("Reprint PIN", Integer.toString(card.getTitular().getIdClient()), "Cardul" + card.getFullPan() + ": " + card.getPIN() + "->" + pinNou);
        card.schimbaPin(pinNou);
        System.out.println("PIN-ul pentru cardul cu terminatia " + card.getPan() + "a fost schimbat.");

    }

    public void modificaLimita(CardDebit card, double limita, int nrCarduri) {
        if(nrCarduri != card.getTitular().getCarduriActive().size()) {
            throw new IdentificareEsuata("Identificare esuata. Nu putem modifica limitele cardurilor.");
        }
        card.setLimita(limita);
        AuditService.log("Modificare limita", Integer.toString(card.getTitular().getIdClient()), "Cardul" + card.getFullPan() + " limita: " + limita);

    }

    public void afiseazaCardurileToate() {
        if(toateCardurileEver.isEmpty()) {
            System.out.println("Nu exista carduri de afisat.");
        }
        for(Card card : toateCardurileEver) {
            card.afiseazaDetalii();
        }
    }



    public void afiseazaCarduriClient(Client client) {
        if (client.getCarduriActive().isEmpty()) {
            System.out.println("Clientul nu are niciun card activ.");
            return;
        }
        for(Card card : client.getCarduriActive()) {
            card.afiseazaDetalii();
        }
    }

    public void plataCard(Card card, double suma) {
        if(card.plateste(suma)) {
            System.out.println("Plata s-a efectuat cu succes.");
            AuditService.log("Plata card", Integer.toString(card.getTitular().getIdClient()), "Card " + card.getPan() + " suma: " + Double.toString(suma));
        }
    }

    public void ramburseaza(CardCredit card, double suma) {
        if(card.ramburseaza(suma)) {
            AuditService.log("Rambursare cc", Integer.toString(card.getTitular().getIdClient()), "Card " + card.getPan());
        } else AuditService.log("Rambursare esuata", Integer.toString(card.getTitular().getIdClient()), "Card " + card.getPan());
    }

    public List<Card> getToateCardurileEver() {
        return toateCardurileEver;
    }
}
