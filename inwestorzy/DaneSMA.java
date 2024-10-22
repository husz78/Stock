package inwestorzy;

import system.Akcja;
import system.SystemTransakcyjny;

public class DaneSMA {
    private Akcja akcjaSygnalu; // Akcja, której dotyczy sygnał
    private Sygnal sygnal; // Sygnał SMA (KUP, SPRZEDAJ, BRAK)
    private SystemTransakcyjny system; // System transakcyjny

    public DaneSMA(SystemTransakcyjny system) {
        this.system = system;
        this.akcjaSygnalu = null;
        this.sygnal = Sygnal.BRAK;
    }
    public Sygnal getSygnal() {
        return sygnal;
    }


    public void obliczSMA() {
        Akcja[] akcje = system.getAkcje();
        int sma10 = 0, sma5 = 0, sma5poprzednie = 0;
        Integer[] dane;
        for (Akcja akcja : akcje) {
            dane = akcja.getOstatnie10Tur();
            if (dane.length < 10) {
                sygnal = Sygnal.BRAK;
                akcjaSygnalu = null;
                continue;
            }
            sma10 = 0;
            sma5 = 0;
            sma5poprzednie = 0;
            for (int i = 0; i < dane.length; i++) {
                sma10 += dane[i];
            }
            for (int i = 5; i < dane.length; i++) {
                sma5 += dane[i];
            }
            for (int i = 4; i < dane.length - 1; i++) {
                sma5poprzednie += dane[i];
            }
            sma10 /= 10;
            sma5 /= 5;
            sma5poprzednie /= 5;
            if (sma5poprzednie < sma10 && sma5 >= sma10) {
                sygnal = Sygnal.KUP;
                akcjaSygnalu = akcja;
                break;
            }
            else if (sma5poprzednie > sma10 && sma5 <= sma10) {
                sygnal = Sygnal.SPRZEDAJ;
                akcjaSygnalu = akcja;
                break;
            }
            else {
                sygnal = Sygnal.BRAK;
                akcjaSygnalu = null;
            }
        }
    }
    public void powiadomSMA() {
        for (Inwestor inwestor : system.getInwestorzy()) {
            if (inwestor instanceof SMA) {
                ((SMA) inwestor).odbierzSygnal(akcjaSygnalu, sygnal);
            }
        }
    }
}
