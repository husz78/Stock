package inwestorzy;

import system.Akcja;
import system.Losowanie;
import system.SystemTransakcyjny;
import zlecenia.*;

public class SMA extends Inwestor{
    private Akcja akcjaSygnalu; // Akcja, której dotyczy sygnał
    private Sygnal sygnal; // Sygnał SMA (KUP, SPRZEDAJ, BRAK)
    public SMA(int id) {
        super(id);
        this.akcjaSygnalu = null;
        this.sygnal = Sygnal.BRAK;
    }
    @Override
    public Zlecenie zlozZlecenie(Akcja akcja, TypZlecenia typ, SystemTransakcyjny system) {
        int wybor = Losowanie.losuj(0, 2);
        int cenaOstatniejTransakcji = dajOstatniaTransakcje(akcja, system)[0];
        int cena; // cena zlecenia
        int liczbaAkcji; // liczba akcji w zleceniu

        if (typ == TypZlecenia.KUPNO) { // KUPNO
            int limitGorny = Math.min(cenaOstatniejTransakcji + 10, getPortfel().getSaldo());
            int limitDolny = Math.max(cenaOstatniejTransakcji - 10, 1);
            if (limitDolny > limitGorny) {
                return null;
            }
            cena = Losowanie.losuj(limitDolny, limitGorny);
            int maxLiczbaAkcji = getPortfel().getSaldo() / cena;
            if (getPortfel().getSaldo() / cena < 1) return null;
            liczbaAkcji = Losowanie.losuj(1, maxLiczbaAkcji);
        }
        else { // SPRZEDAŻ
            int limitDolny = Math.max(cenaOstatniejTransakcji - 10, 1);
            cena = Losowanie.losuj(limitDolny, cenaOstatniejTransakcji + 10);
            int liczbaAkcjiPosiadanych = getPortfel().dajLiczbeAkcji(akcja);
            if (liczbaAkcjiPosiadanych < 1 || getPortfel().getSaldo() / cena < 1) {
                return null;
            }
            int maxLiczbaAkcji = Math.min(getPortfel().getSaldo() / cena, liczbaAkcjiPosiadanych);
            liczbaAkcji = Losowanie.losuj(1, maxLiczbaAkcji);
        }
        Zlecenie zlecenie;
        // wybieramy rodzaj zlecenia
        if (wybor == 0) {
            zlecenie = new ZlecenieBT(typ, akcja.getId(), liczbaAkcji, cena, dajNrTury(system),
                    system.getLiczbaZlecen(), this);
        }
        else if (wybor == 1) {
            zlecenie = new ZlecenieDKT(typ, akcja.getId(), liczbaAkcji, cena,
                    dajNrTury(system), system.getLiczbaZlecen(),
                    Losowanie.losuj(dajNrTury(system) + 1, system.getLiczbaTur()),this);
        }
        else {
            zlecenie = new ZlecenieN(typ, akcja.getId(), liczbaAkcji, cena, dajNrTury(system),
                    system.getLiczbaZlecen(), this);
        }
        system.incLiczbaZlecen();
        return zlecenie;
    }
    public void odbierzSygnal(Akcja akcja, Sygnal sygnal) {
        this.akcjaSygnalu = akcja;
        this.sygnal = sygnal;
    }
    @Override
    public void podejmijDecyzje(SystemTransakcyjny system) {
        if (akcjaSygnalu == null) return;
        if (sygnal == Sygnal.KUP) {
            Zlecenie zlecenie = zlozZlecenie(akcjaSygnalu, TypZlecenia.KUPNO, system);
            if (zlecenie != null) {
                system.dodajZlecenie(zlecenie);
            }
        }
        else if (sygnal == Sygnal.SPRZEDAJ) {
            Zlecenie zlecenie = zlozZlecenie(akcjaSygnalu, TypZlecenia.SPRZEDAZ, system);
            if (zlecenie != null) {
                system.dodajZlecenie(zlecenie);
            }
        }
    }
    public Sygnal getSygnal() {
        return sygnal;
    }
}
