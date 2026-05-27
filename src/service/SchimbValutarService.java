package service;
import model.Moneda;

public class SchimbValutarService {
    public static double getRataConversie(Moneda din, Moneda in) {
        if (din == in) {
            return 1.0;
        }

        if (din == Moneda.EUR && in == Moneda.RON) {
            return 4.98;
        }

        if (din == Moneda.RON && in == Moneda.EUR) {
            return 1 / 4.98;
        }

        if (din == Moneda.EUR && in == Moneda.USD) {
            return 1.09;
        }

        if (din == Moneda.USD && in == Moneda.EUR) {
            return 1 / 1.09;
        }

        if (din == Moneda.EUR && in == Moneda.GBP) {
            return 0.85;
        }

        if (din == Moneda.GBP && in == Moneda.EUR) {
            return 1 / 0.85;
        }

        if (din == Moneda.USD && in == Moneda.RON) {
            return 4.55;
        }

        if (din == Moneda.RON && in == Moneda.USD) {
            return 1 / 4.55;
        }

        if (din == Moneda.USD && in == Moneda.GBP) {
            return 0.78;
        }

        if (din == Moneda.GBP && in == Moneda.USD) {
            return 1 / 0.78;
        }

        if (din == Moneda.GBP && in == Moneda.RON) {
            return 5.85;
        }

        if (din == Moneda.RON && in == Moneda.GBP) {
            return 1 / 5.85;
        }

        throw new IllegalArgumentException("Introdu valute valide (RON, EUR, USD, GBP)");
    }

    public static double converteste(double suma, Moneda din, Moneda in) {
        double rata = getRataConversie(din, in);
        return suma * rata;
    }
}
