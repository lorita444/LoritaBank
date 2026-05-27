package service;

import model.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientService {

    private List<Client> clienti =
            new ArrayList<>();

    public void adaugaClient(Client client) {
        clienti.add(client);
    }

    public void afiseazaClienti() {

        for(Client client : clienti) {
            System.out.println(client);
        }
    }
}