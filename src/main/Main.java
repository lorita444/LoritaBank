package main;

import model.Admin;
import model.Client;

import java.util.Scanner;

public class Main {

    private static final Scanner scanner =
            new Scanner(System.in);

    public static void main(String[] args) {

        while(true) {

            System.out.println("\nLoritaBank");
            System.out.println("1. Login");
            System.out.println("0. Iesire");
            System.out.print("Alege optiunea: ");

            int optiune = scanner.nextInt();
            scanner.nextLine();

            switch(optiune) {

                case 1:

                    System.out.println("1. Admin");
                    System.out.println("2. Client");

                    int tip = scanner.nextInt();
                    scanner.nextLine();

                    if(tip == 1) {

                        Admin admin = null;

                        meniuAdmin(admin);

                    } else {

                        Client client = null;

                        meniuClient(client);
                    }

                    break;

                case 0:

                    System.out.println("Pa si la revedere!");
                    return;

                default:

                    System.out.println("Optiune invalida.");
            }
        }
    }

    private static void meniuAdmin(Admin admin) {

        while(true) {

            System.out.println("\nMENIU ADMIN");

            System.out.println("1. Adauga client");
            System.out.println("2. Afiseaza toti clientii");
            System.out.println("3. Cauta client");

            System.out.println("0. Logout");

            System.out.print("Alege optiunea: ");

            int optiune = scanner.nextInt();
            scanner.nextLine();

            switch(optiune) {

                case 1:
                    break;

                case 2:
                    break;

                case 3:
                    System.out.println("4. Operatii conturi");
                    System.out.println("5. Operatii carduri");

                    System.out.println("0. Iesi");
                    System.out.print("Alege optiunea: ");
                    int optiune2 = scanner.nextInt();
                    scanner.nextLine();

                    switch( optiune2 ) {
                        case 1:
                            meniuConturiAdmin();
                            break;

                        case 2:
                            meniuCarduriAdmin();
                            break;
                        case 0:
                            return;

                        default:
                            System.out.println("Optiune invalida.");
                    }
                    break;

                case 0:
                    return;

                default:
                    System.out.println("Optiune invalida.");
            }
        }
    }

    private static void meniuClient(Client client) {

        while(true) {

            System.out.println("\nMENIU CLIENT");

            System.out.println("1. Conturile mele");
            System.out.println("2. Cardurile mele");
            System.out.println("3. Transferuri si incasari");

            System.out.println("0. Logout");

            System.out.print("Alege optiunea: ");

            int optiune = scanner.nextInt();
            scanner.nextLine();

            switch(optiune) {

                case 1:
                    meniuConturiClient();
                    break;

                case 2:
                    meniuCarduriClient();
                    break;

                case 3:
                    meniuTransferuri();
                    break;

                case 0:
                    return;

                default:
                    System.out.println("Optiune invalida.");
            }
        }
    }

    private static void meniuConturiAdmin() {

        while(true) {

            System.out.println("\nOPERATII CONTURI");

            System.out.println("1. Adauga cont curent");
            System.out.println("2. Adauga cont economii");
            System.out.println("3. Adauga depozit la termen");

            System.out.println("4. Sterge cont");

            System.out.println("5. Depune bani");
            System.out.println("6. Retrage bani");

            System.out.println("7. Mandateaza client");

            System.out.println("8. Vezi tranzactii");

            System.out.println("0. Inapoi");

            System.out.print("Alege optiunea: ");

            int optiune = scanner.nextInt();
            scanner.nextLine();

            switch(optiune) {

                case 1:
                    break;

                case 2:
                    break;

                case 3:
                    break;

                case 4:
                    break;

                case 5:
                    break;

                case 6:
                    break;

                case 7:
                    break;

                case 8:
                    break;

                case 0:
                    return;

                default:
                    System.out.println("Optiune invalida.");
            }
        }
    }

    private static void meniuCarduriAdmin() {

        while(true) {

            System.out.println("\nOPERATII CARDURI");

            System.out.println("1. Emite card debit");
            System.out.println("2. Emite card credit");

            System.out.println("3. Blocheaza card");

            System.out.println("4. Modifica limita");

            System.out.println("5. Schimba PIN");

            System.out.println("0. Inapoi");

            System.out.print("Alege optiunea: ");

            int optiune = scanner.nextInt();
            scanner.nextLine();

            switch(optiune) {

                case 1:
                    break;

                case 2:
                    break;

                case 3:
                    break;

                case 4:
                    break;

                case 5:
                    break;

                case 0:
                    return;

                default:
                    System.out.println("Optiune invalida.");
            }
        }
    }

    private static void meniuConturiClient() {

        while(true) {

            System.out.println("\nCONTURILE MELE");

            System.out.println("1. Adauga cont");
            System.out.println("2. Sterge cont");

            System.out.println("3. Mandateaza client");

            System.out.println("4. Vezi tranzactii");

            System.out.println("5. Schimb valutar");

            System.out.println("6. Retrage bani");

            System.out.println("0. Inapoi");

            System.out.print("Alege optiunea: ");

            int optiune = scanner.nextInt();
            scanner.nextLine();

            switch(optiune) {

                case 1:
                    break;

                case 2:
                    break;

                case 3:
                    break;

                case 4:
                    break;

                case 5:
                    break;

                case 6:
                    break;

                case 0:
                    return;

                default:
                    System.out.println("Optiune invalida.");
            }
        }
    }

    private static void meniuCarduriClient() {

        while(true) {

            System.out.println("\nCARDURILE MELE");

            System.out.println("1. Emite card");

            System.out.println("2. Blocheaza card");

            System.out.println("3. Modifica limita");

            System.out.println("4. Schimba PIN");

            System.out.println("0. Inapoi");

            System.out.print("Alege optiunea: ");

            int optiune = scanner.nextInt();
            scanner.nextLine();

            switch(optiune) {

                case 1:
                    break;

                case 2:
                    break;

                case 3:
                    break;

                case 4:
                    break;

                case 0:
                    return;

                default:
                    System.out.println("Optiune invalida.");
            }
        }
    }

    private static void meniuTransferuri() {

        while(true) {

            System.out.println("\nTRANSFERURI SI INCASARI");

            System.out.println("1. Fa transfer");

            System.out.println("2. Cere incasare");

            System.out.println("0. Inapoi");

            System.out.print("Alege optiunea: ");

            int optiune = scanner.nextInt();
            scanner.nextLine();

            switch(optiune) {

                case 1:
                    break;

                case 2:
                    break;

                case 0:
                    return;

                default:
                    System.out.println("Optiune invalida.");
            }
        }
    }
}