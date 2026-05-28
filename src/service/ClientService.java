package service;

import model.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientService {

    private List<Client> clienti = new ArrayList<>();

    public void adaugaClient(Client client) {
        clienti.add(client);
        AuditService.log("Adauga client", Integer.toString(client.getIdClient()), "Client adaugat cu succes");
    }

    public void afiseazaClienti() {
        if (clienti.isEmpty()) {
            System.out.println("Nu exista clienti inregistrati.");
            return;
        }
        for (Client client : clienti) {
            client.getDetalii();
        }
    }

    public Client cautaClient(String cnp) {
        for (Client client : clienti) {
            if (client.getCnp().equals(cnp)){
                return client;
            }
        }
        return null;
    }

    public List<Client> getClienti() {
        return clienti;
    }
}