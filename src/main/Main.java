package main;

import model.*;
import service.*;
import repository.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static final ClientService clientService = new ClientService();
    private static final ContService contService = new ContService();
    private static final CardService cardService = new CardService();
    private static final TranzactieService tranzactieService = new TranzactieService();

    private static void initializeDataFromDatabase() {
        try {
            ClientRepository clientRepo = ClientRepository.getInstance();
            ContRepository contRepo = ContRepository.getInstance();
            CardRepository cardRepo = CardRepository.getInstance();
            TranzactiiRepository tranzactiiRepo = TranzactiiRepository.getInstance();

            List<Client> loadedClienti = clientRepo.findAll();
            clientService.getClienti().addAll(loadedClienti);

            List<ContBancar> loadedConturi = contRepo.findAll(loadedClienti);
            contService.getToateConturileEver().addAll(loadedConturi);

            List<Card> loadedCarduri = cardRepo.findAll(loadedClienti, loadedConturi);
            cardService.getToateCardurileEver().addAll(loadedCarduri);

            List<Tranzactie> loadedTranzactii = tranzactiiRepo.findAll(loadedConturi);
            tranzactieService.toateTranzactiileEver.addAll(loadedTranzactii);
            
            System.out.println("Date incarcate");
        } catch (Exception e) {
            System.err.println("Eroare la incarcarea datelor din baza de date:");
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        initializeDataFromDatabase();
        while (true) {
            System.out.println("Bun venit la LoritaBank!");
            System.out.println("1. Login");
            System.out.println("0. Iesire");
            System.out.print("Alege optiunea: ");

            int opt = citesteInt();
            switch (opt) {
                case 1: meniuLogin(); break;
                case 0:
                    System.out.println("Pa si la revedere!");
                    return;
                default:
                    System.out.println("Optiune invalida.");
            }
        }
    }

    private static void meniuLogin() {
        System.out.println("\nLogin");
        System.out.println("1. Admin");
        System.out.println("2. Client");
        System.out.println("0. Inapoi");
        System.out.print("Alege optiunea: ");

        int tip = citesteInt();
        switch (tip) {
            case 1:
                meniuLoginAdmin();
                break;
            case 2:
                meniuLoginClient();
                break;
            case 0:
                break;
            default:
                System.out.println("Optiune invalida.");
        }
    }

    private static void meniuLoginAdmin() {
        System.out.print("Introduceti CNP-ul adminului: ");
        String cnp = scanner.nextLine().trim();
        Client client = clientService.cautaClient(cnp);
        if (client == null || !(client instanceof Admin)) {
            System.out.println("Eroare: Nu aveti drepturi de administrator!");
            return;
        }
        System.out.println("Autentificare reusita! Bun venit, " + client.getNume() + " (Admin)!");
        meniuAdmin();
    }

    private static void meniuLoginClient() {
        System.out.print("Introduceti CNP-ul clientului: ");
        String cnp = scanner.nextLine().trim();
        Client client = clientService.cautaClient(cnp);
        if (client == null) {
            System.out.println("Clientul cu CNP " + cnp + " nu a fost gasit.");
            return;
        }
        System.out.println("Bun venit, " + client.getNume() + "!");
        meniuClient(client);
    }

    private static void meniuAdmin() {
        while (true) {
            System.out.println("\nMENIU ADMIN");
            System.out.println("1. Adauga client");
            System.out.println("2. Afiseaza toti clientii");
            System.out.println("3. Cauta client");
            System.out.println("0. Logout");
            System.out.print("Alege optiunea: ");

            int opt = citesteInt();
            switch (opt) {
                case 1: adaugaClientFlow(); break;
                case 2: clientService.afiseazaClienti(); break;
                case 3: cautaClientAdmin(); break;
                case 0: return;
                default: System.out.println("Optiune invalida.");
            }
        }
    }

    private static void adaugaClientFlow() {
        System.out.println("\n Adauga Client ");
        System.out.print("Nume: ");
        String nume = scanner.nextLine();
        System.out.print("CNP: ");
        String cnp = scanner.nextLine();
        System.out.print("Telefon: ");
        String telefon = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Serie si numar CI (ex: AB123456): ");
        String ci = scanner.nextLine();
        System.out.print("Data expirare CI (YYYY-MM-DD): ");
        LocalDate dataExp = LocalDate.parse(scanner.nextLine());
        System.out.print("Venit declarat (RON): ");
        double venit = citesteDouble();

        Client client = new Client(nume, cnp, telefon, email, ci, dataExp, venit);
        clientService.adaugaClient(client);
        ClientRepository.getInstance().save(client);
        System.out.println("Client adaugat cu succes. ID: " + client.getIdClient());
    }

    private static void cautaClientAdmin() {
        System.out.print("Introduceti CNP-ul clientului: ");
        String cnp = scanner.nextLine().trim();
        Client client = clientService.cautaClient(cnp);
        if (client == null) {
            System.out.println("Clientul cu CNP-ul " + cnp + " nu a fost gasit.");
            return;
        }
        client.getDetalii();
        meniuOperatiiAdmin(client);
    }

    private static void meniuOperatiiAdmin(Client client) {
        while (true) {
            System.out.println("\nOperatii pentru: " + client.getNume());
            System.out.println("1. Operatii conturi");
            System.out.println("2. Operatii carduri");
            System.out.println("0. Inapoi");
            System.out.print("Alege optiunea: ");

            int opt = citesteInt();
            switch (opt) {
                case 1: meniuConturiAdmin(client); break;
                case 2: meniuCarduriAdmin(client); break;
                case 0: return;
                default: System.out.println("Optiune invalida.");
            }
        }
    }


    private static void meniuConturiAdmin(Client client) {
        while (true) {
            System.out.println("\n Operatii conturi:");
            System.out.println("1. Adauga cont curent");
            System.out.println("2. Adauga cont economii");
            System.out.println("3. Adauga depozit la termen");
            System.out.println("4. Sterge cont");
            System.out.println("5. Depune bani");
            System.out.println("6. Retrage bani");
            System.out.println("7. Vezi tranzactii");
            System.out.println("8. Calculeaza dobanda");
            System.out.println("9. Aplica dobanda");
            System.out.println("0. Inapoi");
            System.out.print("Alege optiunea: ");

            int opt = citesteInt();
            switch (opt) {
                case 1: adaugaContCurentFlow(client); break;
                case 2: adaugaContEconomiiFlowAdmin(client); break;
                case 3: adaugaDepozitFlow(client); break;
                case 4: stergContFlow(client); break;
                case 5: depuneFlow(client); break;
                case 6: retrageContFlow(client); break;
                case 7: veziTranzactiiFlow(client); break;
                case 8: calculeazaDobandaContFlow(client); break;
                case 9: aplicaDobandaContFlow(client); break;
                case 0: return;
                default: System.out.println("Optiune invalida.");
            }
        }
    }

    private static void adaugaContCurentFlow(Client client) {
        Moneda moneda = selecteazaMoneda();
        if (moneda == null) return;
        ContCurent cont = contService.deschideContCurent(moneda, client);
        ContRepository.getInstance().save(cont);
    }

    private static void adaugaContEconomiiFlow(Client client) {
        Moneda moneda = selecteazaMoneda();
        if (moneda == null) return;
        ContEconomii cont = contService.deschideContEconomii(moneda, client);
        ContRepository.getInstance().save(cont);
    }

    private static void adaugaContEconomiiFlowAdmin(Client client) {
        Moneda moneda = selecteazaMoneda();
        if (moneda == null) return;
        System.out.print("Rata dobanda (%): ");
        float rata = (float) citesteDouble();
        ContEconomii cont = contService.deschideContEconomiiAdmin(moneda, client, rata);
        ContRepository.getInstance().save(cont);
    }

    private static void adaugaDepozitFlow(Client client) {
        Moneda moneda = selecteazaMoneda();
        if (moneda == null) return;
        System.out.print("Perioada (luni): ");
        int perioada = citesteInt();
        System.out.print("Suma initiala (min 100000): ");
        double suma = citesteDouble();
        try {
            DepozitLaTermen cont = contService.deschideDepozitLaTermen(moneda, client, perioada, suma);
            ContRepository.getInstance().save(cont);
        } catch (IllegalArgumentException e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }

    private static void stergContFlow(Client client) {
        ContBancar cont = selecteazaCont(client);
        if (cont == null) return;
        try {
            contService.inchideCont(client, cont);
            ContRepository.getInstance().delete(cont);
            for (Card c : client.getCarduri()) {
                if (c instanceof CardDebit cardDebit) {
                    if (cardDebit.getContAsociat() != null && cardDebit.getContAsociat().getId() == cont.getId()) {
                        cardDebit.setActiv(false);
                    }
                }
            }
        } catch (IllegalStateException e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }

    private static void depuneFlow(Client client) {
        ContBancar cont = selecteazaCont(client);
        if (cont == null) return;
        System.out.print("Suma de depus: ");
        double suma = citesteDouble();
        try {
            contService.depune(cont, suma);
            ContRepository.getInstance().updateSold(cont);
            System.out.println("Depunere efectuata. Sold nou: " + cont.getSold());
        } catch (Exception e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }

    private static void retrageContFlow(Client client) {
        ContBancar cont = selecteazaCont(client);
        if (cont == null) return;
        System.out.print("Suma de retras: ");
        double suma = citesteDouble();
        try {
            contService.retrage(cont, suma);
            ContRepository.getInstance().updateSold(cont);
            System.out.println("Retragere efectuata. Sold nou: " + cont.getSold());
        } catch (Exception e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }

    private static void veziTranzactiiFlow(Client client) {
        System.out.println("\nVizualizare tranzactii");
        System.out.println("1. Afiseaza toate tranzactiile.");
        System.out.println("2. Filtreaza tranzactiile.");
        System.out.println("0. Inapoi");
        System.out.print("Alege optiunea: ");
        int opt = citesteInt();
        if (opt == 1) {
            tranzactieService.afiseazaTranzactiile(client);
        } else if (opt == 2) {
            ContBancar cont = selecteazaCont(client);
            if (cont == null) return;
            System.out.print("Introduceti data de inceput (YYYY-MM-DD): ");
            LocalDate start = LocalDate.parse(scanner.nextLine().trim());
            System.out.print("Introduceti data de sfarsit (YYYY-MM-DD): ");
            LocalDate end = LocalDate.parse(scanner.nextLine().trim());
            tranzactieService.afiseazaTranzactiiFiltrate(client, cont, start, end);
        }
    }

    private static void calculeazaDobandaContFlow(Client client) {
        ContBancar cont = selecteazaCont(client);
        if (cont == null) return;
        if (cont instanceof Interfaces.Dobandibil) {
            double d = ((Interfaces.Dobandibil) cont).calculeazaDobanda();
            System.out.println("Dobanda calculata: " + d + " " + cont.getMoneda());
        } else {
            System.out.println("Acest tip de cont nu are dobanda.");
        }
    }


    private static void meniuCarduriAdmin(Client client) {
        while (true) {
            System.out.println("\nOperatii carduri");
            System.out.println("1. Emite card debit");
            System.out.println("2. Emite card credit");
            System.out.println("3. Blocheaza card");
            System.out.println("4. Modifica limita (card debit - nelimitata)");
            System.out.println("5. Calculeaza dobanda (card credit)");
            System.out.println("6. Aplica dobanda (card credit)");
            System.out.println("0. Inapoi");
            System.out.print("Alege optiunea: ");

            int opt = citesteInt();
            switch (opt) {
                case 1: emiteCardDebitFlow(client); break;
                case 2: emiteCardCreditFlow(client); break;
                case 3: blocheazaCardFlow(client); break;
                case 4: modificaLimitaAdminFlow(client); break;
                case 5: calculeazaDobandaCardFlow(client); break;
                case 6: aplicaDobandaCardFlow(client); break;
                case 0: return;
                default: System.out.println("Optiune invalida.");
            }
        }
    }

    private static void emiteCardDebitFlow(Client client) {
        List<ContBancar> conturi = client.getConturi();
        List<ContBancar> curente = new ArrayList<>();
        for (ContBancar c : conturi) {
            if (c instanceof ContCurent) curente.add(c);
        }
        if (curente.isEmpty()) {
            System.out.println("Clientul nu are niciun cont curent pentru a asocia cardul.");
            return;
        }
        System.out.println("Selectati contul curent:");
        for (int i = 0; i < curente.size(); i++) {
            System.out.println((i + 1) + ". " + curente.get(i).getIban());
        }
        System.out.print("Alege: ");
        int idx = citesteInt() - 1;
        if (idx < 0 || idx >= curente.size()) { System.out.println("Selectie invalida."); return; }
        int nrInainte = client.getCarduri().size();
        cardService.emiteCardDebit(client, (ContCurent) curente.get(idx));
        if (client.getCarduri().size() > nrInainte) {
            Card cardNou = client.getCarduri().get(client.getCarduri().size() - 1);
            CardRepository.getInstance().save(cardNou);
            System.out.println(">>> PIN card: " + cardNou.getPIN() + " Retine-l!");
        }
    }

    private static void emiteCardCreditFlow(Client client) {
        System.out.print("Limita credit dorita (max 3x venit = " + (3 * client.getVenitDeclarat()) + "): ");
        double limita = citesteDouble();
        try {
            int nrInainte = client.getCarduri().size();
            cardService.emiteCardCredit(client, limita);
            if (client.getCarduri().size() > nrInainte) {
                Card cardNou = client.getCarduri().get(client.getCarduri().size() - 1);
                CardRepository.getInstance().save(cardNou);
                System.out.println(">>> PIN card: " + cardNou.getPIN() + " (retineti-l in siguranta!) <<<");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }

    private static void blocheazaCardFlow(Client client) {
        Card card = selecteazaCard(client);
        if (card == null) return;
        try {
            cardService.blocheazaCard(card);
            CardRepository.getInstance().updateStatusOrPin(card);
        } catch (IllegalStateException e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }

    private static void modificaLimitaAdminFlow(Client client) {
        Card card = selecteazaCard(client);
        if (card == null) return;
        if (!(card instanceof CardDebit)) {
            System.out.println("Aceasta operatie este disponibila doar pentru carduri debit.");
            return;
        }
        System.out.print("Limita noua (0 = nelimitata): ");
        double limita = citesteDouble();
        double limitaSetata = (limita == 0) ? Double.MAX_VALUE : limita;
        int nrCarduri = client.getCarduriActive().size();
        try {
            cardService.modificaLimita((CardDebit) card, limitaSetata, nrCarduri);
            System.out.println("Limita modificata cu succes.");
        } catch (Exception e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }

    private static void calculeazaDobandaCardFlow(Client client) {
        Card card = selecteazaCard(client);
        if (card == null) return;
        if (card instanceof CardCredit) {
            double d = ((CardCredit) card).calculeazaDobanda();
            System.out.println("Dobanda calculata: " + d + " RON");
        } else {
            System.out.println("Cardul debit nu are dobanda.");
        }
    }

    private static void ramburseazaCreditFlow(Client client) {
        Card card = selecteazaCard(client);
        if (card == null) return;
        if (!(card instanceof CardCredit)) {
            System.out.println("Aceasta operatie este disponibila doar pentru carduri de credit.");
            return;
        }
        System.out.print("Suma de rambursat: ");
        double suma = citesteDouble();
        try {
            cardService.ramburseaza((CardCredit) card, suma);
            CardRepository.getInstance().updateCreditData((CardCredit) card);
        } catch (Exception e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }

    private static void aplicaDobandaContFlow(Client client) {
        ContBancar cont = selecteazaCont(client);
        if (cont == null) return;
        if (cont instanceof Interfaces.Dobandibil) {
            try {
                ((Interfaces.Dobandibil) cont).aplicaDobanda();
                ContRepository.getInstance().updateSold(cont);
                System.out.println("Dobanda a fost aplicata cu succes! Sold nou: " + cont.getSold() + " " + cont.getMoneda());
            } catch (Exception e) {
                System.out.println("Eroare la aplicarea dobanzii: " + e.getMessage());
            }
        } else {
            System.out.println("Acest tip de cont nu are dobanda.");
        }
    }

    private static void aplicaDobandaCardFlow(Client client) {
        Card card = selecteazaCard(client);
        if (card == null) return;
        if (card instanceof CardCredit) {
            try {
                ((CardCredit) card).aplicaDobanda();
                CardRepository.getInstance().updateCreditData((CardCredit) card);
                System.out.println("Dobanda a fost aplicata cu succes!");
            } catch (Exception e) {
                System.out.println("Eroare la aplicarea dobanzii: " + e.getMessage());
            }
        } else {
            System.out.println("Cardul debit nu are dobanda.");
        }
    }


    private static void meniuClient(Client client) {
        while (true) {
            System.out.println("\nMeniu client: " + client.getNume() + " ===");
            System.out.println("1. Conturile mele");
            System.out.println("2. Cardurile mele");
            System.out.println("3. Transferuri");
            System.out.println("0. Logout");
            System.out.print("Alege optiunea: ");

            int opt = citesteInt();
            switch (opt) {
                case 1: meniuConturiClient(client); break;
                case 2: meniuCarduriClient(client); break;
                case 3: meniuTransferuri(client); break;
                case 0: return;
                default: System.out.println("Optiune invalida.");
            }
        }
    }


    private static void meniuConturiClient(Client client) {
        while (true) {
            System.out.println("\nConturile mele");
            System.out.println("1. Adauga cont");
            System.out.println("2. Sterge cont");
            System.out.println("3. Vezi toate conturile si cardurile");
            System.out.println("4. Depune bani");
            System.out.println("5. Retrage bani");
            System.out.println("6. Schimb valutar");
            System.out.println("7. Vezi tranzactii");
            System.out.println("8. Calculeaza dobanda");
            System.out.println("0. Inapoi");
            System.out.print("Alege optiunea: ");

            int opt = citesteInt();
            switch (opt) {
                case 1: meniuAdaugaContClient(client); break;
                case 2: stergContFlow(client); break;
                case 3: afiseazaConturiSiCarduriFlow(client); break;
                case 4: depuneFlow(client); break;
                case 5: retrageContFlow(client); break;
                case 6: schimbValutarFlow(client); break;
                case 7: veziTranzactiiFlow(client); break;
                case 8: calculeazaDobandaContFlow(client); break;
                case 0: return;
                default: System.out.println("Optiune invalida.");
            }
        }
    }

    private static void afiseazaConturiSiCarduriFlow(Client client) {
        System.out.println("\nConturile tale: ");
        List<ContBancar> conturi = client.getConturi();
        if (conturi.isEmpty()) {
            System.out.println("Nu ai niciun cont.");
        } else {
            for (ContBancar c : conturi) {
                contService.afiseazaSold(c);
            }
        }
        System.out.println("\nCardurile tale: ");
        List<Card> carduri = client.getCarduri();
        if (carduri.isEmpty()) {
            System.out.println("Nu ai niciun card.");
        } else {
            for (Card c : carduri) {
                c.afiseazaDetalii();
            }
        }
    }

    private static void meniuAdaugaContClient(Client client) {
        System.out.println("Tip cont:");
        System.out.println("1. Cont curent");
        System.out.println("2. Cont economii");
        System.out.println("3. Depozit la termen");
        System.out.println("0. Inapoi");
        System.out.print("Alege: ");
        int opt = citesteInt();
        switch (opt) {
            case 1: adaugaContCurentFlow(client); break;
            case 2: adaugaContEconomiiFlow(client); break;
            case 3: adaugaDepozitFlow(client); break;
            case 0: break;
            default: System.out.println("Optiune invalida.");
        }
    }

    private static void schimbValutarFlow(Client client) {
        if (client.getConturi().size() < 2) {
            System.out.println("Aveti nevoie de cel putin 2 conturi pentru schimb valutar.");
            return;
        }
        System.out.println("Cont sursa:");
        ContBancar sursa = selecteazaCont(client);
        if (sursa == null) return;
        System.out.println("Cont destinatie:");
        ContBancar dest = selecteazaCont(client);
        if (dest == null || dest == sursa) { System.out.println("Conturi invalide."); return; }
        System.out.print("Suma de schimbat: ");
        double suma = citesteDouble();
        try {
            tranzactieService.transfera(client, suma, sursa, dest, "Schimb valutar");
            ContRepository.getInstance().updateSold(sursa);
            ContRepository.getInstance().updateSold(dest);
            if (!client.getTranzactii().isEmpty()) {
                Tranzactie transfer = client.getTranzactii().get(client.getTranzactii().size() - 1);
                TranzactiiRepository.getInstance().save(transfer);
            }
            System.out.println("Schimb valutar efectuat cu succes.");
        } catch (Exception e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }

    private static void meniuCarduriClient(Client client) {
        while (true) {
            System.out.println("\nCardurile tale");
            System.out.println("1. Emite card");
            System.out.println("2. Blocheaza card");
            System.out.println("3. Modifica limita (max 60000 RON)");
            System.out.println("4. Schimba PIN");
            System.out.println("5. Plata cu cardul");
            System.out.println("6. Calculeaza dobanda");
            System.out.println("7. Ramburseaza credit");
            System.out.println("0. Inapoi");
            System.out.print("Alege optiunea: ");

            int opt = citesteInt();
            switch (opt) {
                case 1: meniuEmiteCardClient(client); break;
                case 2: blocheazaCardFlow(client); break;
                case 3: modificaLimitaClientFlow(client); break;
                case 4: schimbaPinFlow(client); break;
                case 5: plataCardFlow(client); break;
                case 6: calculeazaDobandaCardFlow(client); break;
                case 7: ramburseazaCreditFlow(client); break;
                case 0: return;
                default: System.out.println("Optiune invalida.");
            }
        }
    }

    private static void meniuEmiteCardClient(Client client) {
        System.out.println("Tip card:");
        System.out.println("1. Card debit");
        System.out.println("2. Card credit");
        System.out.println("0. Inapoi");
        System.out.print("Alege: ");
        int opt = citesteInt();
        switch (opt) {
            case 1: emiteCardDebitFlow(client); break;
            case 2: emiteCardCreditFlow(client); break;
            case 0: break;
            default: System.out.println("Optiune invalida.");
        }
    }

    private static void modificaLimitaClientFlow(Client client) {
        Card card = selecteazaCard(client);
        if (card == null) return;
        if (!(card instanceof CardDebit)) {
            System.out.println("Aceasta operatie este disponibila doar pentru carduri debit.");
            return;
        }
        System.out.print("Limita noua (max 60000 RON): ");
        double limita = citesteDouble();
        if (limita <= 0 || limita > 60000) {
            System.out.println("Limita invalida. Trebuie sa fie intre 1 si 60000 RON.");
            return;
        }
        int nrCarduri = client.getCarduriActive().size();
        try {
            cardService.modificaLimita((CardDebit) card, limita, nrCarduri);
            CardRepository.getInstance().updateStatusOrPin(card);
            System.out.println("Limita modificata cu succes.");
        } catch (Exception e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }

    private static void schimbaPinFlow(Client client) {
        Card card = selecteazaCard(client);
        if (card == null) return;
        System.out.print("Serie si numar CI (pentru verificare identitate): ");
        String ci = scanner.nextLine();
        System.out.print("PIN nou (4 cifre): ");
        String pinNou = scanner.nextLine();
        try {
            cardService.schimbaPin(card, pinNou, ci);
            CardRepository.getInstance().updateStatusOrPin(card);
        } catch (Exception e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }

    private static void plataCardFlow(Client client) {
        Card card = selecteazaCard(client);
        if (card == null) return;
        System.out.print("Introduceti PIN-ul cardului: ");
        String pinIntroduce = scanner.nextLine().trim();
        if (!pinIntroduce.equals(card.getPIN())) {
            System.out.println("PIN incorect. Plata a fost refuzata.");
            AuditService.log("Plata refuzata - PIN gresit",
                    Integer.toString(client.getIdClient()),
                    "Card **** " + card.getPan());
            return;
        }
        System.out.print("Suma de plata: ");
        double suma = citesteDouble();
        cardService.plataCard(card, suma);
        if (card instanceof CardDebit cardDebit) {
            ContRepository.getInstance().updateSold(cardDebit.getContAsociat());
        } else if (card instanceof CardCredit cardCredit) {
            CardRepository.getInstance().updateCreditData(cardCredit);
        }
    }


    private static void meniuTransferuri(Client client) {
        while (true) {
            System.out.println("\nTransferuri");
            System.out.println("1. Fa transfer");
            System.out.println("0. Inapoi");
            System.out.print("Alege optiunea: ");

            int opt = citesteInt();
            switch (opt) {
                case 1: faTransferFlow(client); break;
                case 0: return;
                default: System.out.println("Optiune invalida.");
            }
        }
    }

    private static void faTransferFlow(Client client) {
        System.out.println("Selectati contul sursa:");
        ContBancar sursa = selecteazaCont(client);
        if (sursa == null) return;
        System.out.print("IBAN destinatie: ");
        String ibanDest = scanner.nextLine();
        ContBancar dest = gasesteCont(ibanDest, clientService.getClienti());
        if (dest == null) {
            System.out.println("Contul destinatie nu a fost gasit.");
            return;
        }
        System.out.print("Suma de transferat: ");
        double suma = citesteDouble();
        System.out.print("Detalii transfer: ");
        String detalii = scanner.nextLine();
        try {
            tranzactieService.transfera(client, suma, sursa, dest, detalii);
            ContRepository.getInstance().updateSold(sursa);
            ContRepository.getInstance().updateSold(dest);
            if (!client.getTranzactii().isEmpty()) {
                Tranzactie transfer = client.getTranzactii().get(client.getTranzactii().size() - 1);
                TranzactiiRepository.getInstance().save(transfer);
            }
            System.out.println("Transfer efectuat cu succes.");
        } catch (Exception e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }






    private static Moneda selecteazaMoneda() {
        System.out.println("Moneda: 1.RON  2.EUR  3.USD  4.GBP  0.Inapoi");
        System.out.print("Alege: ");
        int opt = citesteInt();
        switch (opt) {
            case 1: return Moneda.RON;
            case 2: return Moneda.EUR;
            case 3: return Moneda.USD;
            case 4: return Moneda.GBP;
            case 0: return null;
            default: System.out.println("Moneda invalida."); return null;
        }
    }

    private static ContBancar selecteazaCont(Client client) {
        List<ContBancar> conturiAll = client.getConturi();
        List<ContBancar> conturi = new java.util.ArrayList<>();
        for (ContBancar c : conturiAll) {
            if (c.isActiv()) {
                conturi.add(c);
            }
        }
        if (conturi.isEmpty()) {
            System.out.println("Clientul nu are niciun cont activ.");
            return null;
        }
        System.out.println("Conturi disponibile:");
        for (int i = 0; i < conturi.size(); i++) {
            ContBancar c = conturi.get(i);
            String tip = (c instanceof ContCurent) ? "Curent" :
                          (c instanceof ContEconomii) ? "Economii" : "Depozit";
            System.out.println((i + 1) + ". [" + tip + "] " + c.getIban() + " - " + c.getSold() + " " + c.getMoneda());
        }
        System.out.print("Alege: ");
        int idx = citesteInt() - 1;
        if (idx < 0 || idx >= conturi.size()) {
            System.out.println("Selectie invalida.");
            return null;
        }
        return conturi.get(idx);
    }

    private static Card selecteazaCard(Client client) {
        List<Card> carduri = client.getCarduriActive();
        if (carduri.isEmpty()) {
            System.out.println("Clientul nu are niciun card activ.");
            return null;
        }
        System.out.println("Carduri active:");
        for (int i = 0; i < carduri.size(); i++) {
            Card c = carduri.get(i);
            String tip = (c instanceof CardCredit) ? "Credit" : "Debit";
            System.out.println((i + 1) + ". [" + tip + "] **** " + c.getPan());
        }
        System.out.print("Alege: ");
        int idx = citesteInt() - 1;
        if (idx < 0 || idx >= carduri.size()) {
            System.out.println("Selectie invalida.");
            return null;
        }
        return carduri.get(idx);
    }

    private static ContBancar gasesteCont(String iban, List<model.Client> totiClientii) {
        for (model.Client c : totiClientii) {
            for (ContBancar cont : c.getConturi()) {
                if (cont.getIban().equalsIgnoreCase(iban)) {
                    return cont;
                }
            }
        }
        return null;
    }

    private static int citesteInt() {
        while (true) {
            try {
                int val = Integer.parseInt(scanner.nextLine().trim());
                return val;
            } catch (NumberFormatException e) {
                System.out.print("Introduceti un numar intreg valid: ");
            }
        }
    }

    private static double citesteDouble() {
        while (true) {
            try {
                double val = Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
                return val;
            } catch (NumberFormatException e) {
                System.out.print("Introduceti un numar valid: ");
            }
        }
    }
}