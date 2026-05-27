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
    }

    public ContCurent deschideContCurent(String moneda, Client client) {
        ContCurent cont = new ContCurent(moneda, client);
        inregistreazaCont(client, cont);
        return cont;
    }

    public ContEconomii deschideContEconomii(String moneda, Client client, float rataDobanda) {
        ContEconomii cont = new ContEconomii(moneda, client, rataDobanda);
        inregistreazaCont(client, cont);
        return cont;
    }

    public DepozitLaTermen deschideDepozitLaTermen(String moneda, Client client, int perioada, double sumaInitiala) {
        DepozitLaTermen depozit = new DepozitLaTermen(moneda, client, perioada, sumaInitiala);
        inregistreazaCont(client, depozit);
        return depozit;
    }

    public void inchideCont(Client client, ContBancar contBancar) {
        if (contBancar.getSold() != 0) {
            throw new IllegalStateException("Contul nu poate fi închis! Soldul trebuie să fie exact 0. Sold curent: " + contBancar.getSold());
        }
        client.getConturi().remove(contBancar);
        toateConturileEver.remove(contBancar);
        System.out.println("Contul " + contBancar.getIban() + " a fost șters cu succes.");
    }


    public void depune(ContBancar contBancar, double suma){
        contBancar.depune(suma);
    }

    public void retrage(ContBancar contBancar, double suma){
        contBancar.retrage(suma);
    }

    public void afiseazaSold(ContBancar contBancar) {
        System.out.println("Soldul contului " + contBancar.getIban() + " este: " + contBancar.getSold() + " " + contBancar.getMoneda());
    }
}
