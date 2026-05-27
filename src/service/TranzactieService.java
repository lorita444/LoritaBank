package service;

import model.Client;
import model.ContBancar;
import model.ContCurent;
import model.Tranzactie;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TranzactieService {
    public List<Tranzactie> toateTranzactiileEver = new ArrayList<>();

    public void transfera(Client ordonator, double suma, ContBancar contSursa, ContBancar contDestinatie, String detalii) {
        if(contSursa==null || contDestinatie==null || contSursa.getTitular() != ordonator || suma <= 0) {
            throw new IllegalArgumentException("Datele transferului nu sunt corecte");
        }

        double sumaConvertita = SchimbValutarService.converteste(suma, contSursa.getMoneda(), contDestinatie.getMoneda());
        contSursa.retrage(suma);
        contDestinatie.depune(sumaConvertita);

        Tranzactie transfer = new Tranzactie(suma, contSursa.getMoneda(), contSursa, contDestinatie, detalii);
        ordonator.getTranzactii().add(transfer);
        contDestinatie.getTitular().getTranzactii().add(transfer);
        toateTranzactiileEver.add(transfer);
        AuditService.log("Transfer", Integer.toString(ordonator.getIdClient()), Double.toString(suma) + " " + contSursa.getMoneda() + " in contul " + contDestinatie.getIban() );
    }

    public void afiseazaToateTranzactiile() {
        for(Tranzactie t : toateTranzactiileEver) {
            t.afiseazaDetalii();
        }
    }

    public void afiseazaTranzactiiFiltrate(Client client, ContBancar cont, LocalDate dataStart, LocalDate dataEnd) {
        List<Tranzactie> gasite = new ArrayList<>();

        for(Tranzactie t : client.getTranzactii()) {
            if(t.getContSursa() == cont || t.getContDestinatie() == cont) {
                LocalDate data = t.getData();
                if((data.isEqual(dataStart) || data.isAfter(dataStart)) && (data.isEqual(dataEnd) || data.isBefore(dataEnd))){
                    gasite.add(t);
                }
            }
        }
        if(gasite.isEmpty()) {
            System.out.println("Nu exista tranzactii din perioada selectata.");
        } else {
            for(Tranzactie t : gasite) {
                t.afiseazaDetalii();
            }
        }
    }


}
