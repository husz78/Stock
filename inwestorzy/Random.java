package inwestorzy;

import system.Akcja;
import system.Losowanie;
import system.SystemTransakcyjny;
import zlecenia.*;

public class Random extends Inwestor{
    public Random(int id) {
        super(id);
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
            int liczbaAkcjiPosiadanych = getPortfel().dajLiczbeAkcji(akcja);
            int limitDolny = Math.max(cenaOstatniejTransakcji - 10, 1);
            cena = Losowanie.losuj(limitDolny, cenaOstatniejTransakcji + 10);
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
    @Override
    public void podejmijDecyzje(SystemTransakcyjny system) {
        Akcja[] akcje = system.getAkcje();
        int nrAkcji = Losowanie.losuj(0, akcje.length - 1);
        Akcja akcja = akcje[nrAkcji];
        int sygnal = Losowanie.losuj(0, 1);
        if (sygnal == 0) {
            Zlecenie zlecenie = zlozZlecenie(akcja, TypZlecenia.KUPNO, system);
            if (zlecenie != null) {
                system.dodajZlecenie(zlecenie);
            }
        }
        else {
            Zlecenie zlecenie = zlozZlecenie(akcja, TypZlecenia.SPRZEDAZ, system);
            if (zlecenie != null) {
                system.dodajZlecenie(zlecenie);
            }
        }
    }
}
