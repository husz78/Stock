package inwestorzy;

import system.*;
import zlecenia.*;

public abstract class Inwestor {
    private Portfel portfel; // Portfel inwestora
    private int id; // Identyfikator inwestora

    public Inwestor(int id) {
        this.portfel = new Portfel();
        this.id = id;
    }
    public int dajNrTury(SystemTransakcyjny system) { // Pyta system o numer aktualnej tury
        return system.getNrTury();
    }
    // Pyta system o cenę ostatniej transakcji danej akcji oraz
    // turę, w której ta transakcja miała miejsce. Zwraca wynik w postaci tablicy dwuelementowej.
    // Pierwszy element to cena, a drugi to tura.
    public int[] dajOstatniaTransakcje(Akcja akcja, SystemTransakcyjny system) {
        int[] result = system.dajOstatniaTransakcje(akcja.getId());
        return result;
    }
    // Składa zlecenie kupna lub sprzedaży danej akcji. Zwraca złożone zlecenie lub null,
    // jeśli inwestor nie ma wystarczających środków na koncie lub nie posiada wystarczającej liczby akcji.
    public abstract Zlecenie zlozZlecenie(Akcja akcja, TypZlecenia typ, SystemTransakcyjny system);

    // Podejmuje decyzję o złożeniu zlecenia kupna lub sprzedaży akcji, lub nie podejmuje.
    public abstract void podejmijDecyzje(SystemTransakcyjny system);

    @Override
    public String toString() {
        return id + "";
    }
    public Portfel getPortfel() {
        return portfel;
    }
    public void kupAkcje(Akcja akcja, int liczbaAkcji, int cena) {
        portfel.dodajAkcje(akcja, liczbaAkcji);
        portfel.odejmijSaldo(liczbaAkcji * cena);
    }
    public void sprzedajAkcje(Akcja akcja, int liczbaAkcji, int cena) {
        portfel.usunAkcje(akcja, liczbaAkcji);
        portfel.dodajSaldo(liczbaAkcji * cena);
    }
}
