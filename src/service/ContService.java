package service;

import exception.SumaInvalida;
import model.*;

import java.util.ArrayList;
import java.util.List;

public class ContService {

    private final List<ContBancar> toateConturileEver = new ArrayList<>();

    private void inregistreazaCont(Client client, ContBancar contNou) {
        client.getConturi().add(contNou);
        toateConturileEver.add(contNou);
        System.out.println("Contul " + contNou.getIban() + " a fost activat pentru " + client.getNume());

        AuditService.log("Creare cont", Integer.toString(client.getIdClient()), "Cont activat cu succes." );
    }

    public ContCurent deschideContCurent(Moneda moneda, Client client) {
        ContCurent cont = new ContCurent(moneda, client);
        inregistreazaCont(client, cont);
        AuditService.log("Deschidere cont curent", Integer.toString(client.getIdClient()), cont.getIban());
        return cont;
    }

    public ContEconomii deschideContEconomii(Moneda moneda, Client client, float rataDobanda) {
        ContEconomii cont = new ContEconomii(moneda, client, rataDobanda);
        inregistreazaCont(client, cont);
        AuditService.log("Deschidere cont economii", Integer.toString(client.getIdClient()), cont.getIban());
        return cont;
    }

    public DepozitLaTermen deschideDepozitLaTermen(Moneda moneda, Client client, int perioada, double sumaInitiala) {
        DepozitLaTermen depozit = new DepozitLaTermen(moneda, client, perioada, sumaInitiala);
        inregistreazaCont(client, depozit);
        AuditService.log("Deschidere depozit", Integer.toString(client.getIdClient()), depozit.getIban());
        return depozit;
    }

    public void inchideCont(Client client, ContBancar contBancar) {
        if (contBancar.getSold() != 0) {
            throw new IllegalStateException("Eroare. Clientul are sold activ. Sold curent: " + contBancar.getSold());
        }
        AuditService.log("Inchidere cont curent", Integer.toString(client.getIdClient()), contBancar.getIban());
        client.getConturi().remove(contBancar);
        toateConturileEver.remove(contBancar);
        System.out.println("Contul " + contBancar.getIban() + " a fost șters cu succes.");
        CardService cardService = new CardService();
        if(contBancar instanceof ContCurent contCurent) {
            for(Card c : contCurent.getCarduriAsociate()) {
                cardService.blocheazaCard(c);
            }
        }
    }


    public void depune(ContBancar contBancar, double suma){

        contBancar.depune(suma);
        AuditService.log("Depunere", Integer.toString(contBancar.getTitular().getIdClient()), "suma " + Double.toString(suma) + " in " + contBancar.getIban());
    }

    public void retrage(ContBancar contBancar, double suma){

        contBancar.retrage(suma);
        AuditService.log("Retragere", Integer.toString(contBancar.getTitular().getIdClient()), "suma " + Double.toString(suma) + " in " + contBancar.getIban());
    }

    public void afiseazaSold(ContBancar contBancar) {
        System.out.println("Soldul contului " + contBancar.getIban() + " este: " + contBancar.getSold() + " " + contBancar.getMoneda());
        AuditService.log("Afisare sold", Integer.toString(contBancar.getTitular().getIdClient()), contBancar.getIban());
    }
}
