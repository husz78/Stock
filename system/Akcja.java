package system;

import zlecenia.TypZlecenia;
import zlecenia.Zlecenie;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Akcja {
    private String id; // Identyfikator akcji
    private PriorityQueue<Zlecenie> zleceniaKupna; // Kolejka zleceń kupna danej akcji
    private PriorityQueue<Zlecenie> zleceniaSprzedazy; // Kolejka zleceń sprzedaży danej akcji
    private int ostatniaCena; // Cena ostatniej transakcji
    private int ostatniaTura; // Numer tury ostatniej transakcji
    private LinkedList<Integer> ostatnie10Tur; // Lista przechowująca ceny z ostatnich 10 tur

    public Akcja(String id, int ostatniaCena) {
        this.id = id;
        ostatniaTura = -1;
        zleceniaKupna = new PriorityQueue<>();
        zleceniaSprzedazy = new PriorityQueue<>();
        this.ostatniaCena = ostatniaCena;
        ostatnie10Tur = new LinkedList<>();
        ostatnie10Tur.offer(ostatniaCena);
    }
    // Realizuje zlecenia kupna i sprzedaży
    void zrealizujZlecenia(SystemTransakcyjny system) {
        // Dodajemy nową cenę do listy tylko wtedy, gdy numer tury się zmienia
        if (ostatniaTura != system.getNrTury()) {
            if (ostatnie10Tur.size() == 10) {
                ostatnie10Tur.poll();
            }
            ostatnie10Tur.offer(ostatniaCena);
        }
        while (!zleceniaKupna.isEmpty() && !zleceniaSprzedazy.isEmpty()) {
            Zlecenie kupno = zleceniaKupna.peek();
            Zlecenie sprzedaz = zleceniaSprzedazy.peek();
            if (kupno.getLimitCeny() < sprzedaz.getLimitCeny()) {
                break;
            }
            zrealizujZlecenia(kupno, sprzedaz, system);
        }
        // Pozbywamy się zleceń, które mają tę samą turę eliminacji co aktualna tura
        Iterator<Zlecenie> kupnaIterator = zleceniaKupna.iterator();
        while (kupnaIterator.hasNext()) {
            Zlecenie kupna = kupnaIterator.next();
            if (kupna.getTuraElminacji() == system.getNrTury()) {
                kupnaIterator.remove();
            }
        }

        Iterator<Zlecenie> sprzedazyIterator = zleceniaSprzedazy.iterator();
        while (sprzedazyIterator.hasNext()) {
            Zlecenie sprzedaz = sprzedazyIterator.next();
            if (sprzedaz.getTuraElminacji() == system.getNrTury()) {
                sprzedazyIterator.remove();
            }
        }

    }

    // Realizuje jedną parę zleceń kupna i sprzedaży
    private void zrealizujZlecenia(Zlecenie kupno, Zlecenie sprzedaz, SystemTransakcyjny system) {
        int cenaTransakcji = Zlecenie.cenaWczesniejszegoZlecenia(kupno, sprzedaz);
        int liczbaAkcji = Math.min(kupno.getLiczbaAkcji(), sprzedaz.getLiczbaAkcji());

        // Sprawdzenie, czy inwestorzy mają wystarczająco środków na realizację transakcji
        if (kupno.getInwestor().getPortfel().getSaldo() < cenaTransakcji * liczbaAkcji) {
            zleceniaKupna.poll();
            return;
        }
        if (sprzedaz.getInwestor().getPortfel().dajLiczbeAkcji(this) < liczbaAkcji) {
            zleceniaSprzedazy.poll();
            return;
        }

        kupno.getInwestor().kupAkcje(this, liczbaAkcji, cenaTransakcji);
        sprzedaz.getInwestor().sprzedajAkcje(this, liczbaAkcji, cenaTransakcji);

        ostatniaCena = cenaTransakcji;
        ostatniaTura = system.getNrTury();


        if (kupno.getLiczbaAkcji() == liczbaAkcji) { // Zlecenie kupna zrealizowane
            zleceniaKupna.poll();
            sprzedaz.odejmijLiczbeAkcji(liczbaAkcji);
            if (sprzedaz.getLiczbaAkcji() == 0) {
                zleceniaSprzedazy.poll();
                return;
            }
            if (sprzedaz.getTuraZlozenia() == sprzedaz.getTuraElminacji()) { // Zlecenie natychmiastowe
                zleceniaSprzedazy.poll();
                return;
            }
        }
        else { // Zlecenie sprzedaży zrealizowane
            zleceniaSprzedazy.poll();
            kupno.odejmijLiczbeAkcji(liczbaAkcji);
            if (kupno.getLiczbaAkcji() == 0) {
                zleceniaKupna.poll();
                return;
            }
            if (kupno.getTuraZlozenia() == kupno.getTuraElminacji()) // Zlecenie natychmiastowe
                zleceniaKupna.poll();
        }
    }

    // Dodaje zlecenie do odpowiedniej kolejki
    public void dodajZlecenie(Zlecenie zlecenie) { // TODO zmienic z public na bez modyfikatora dostepu
        TypZlecenia typ = zlecenie.getTyp();
        if (typ == TypZlecenia.KUPNO) {
            zleceniaKupna.add(zlecenie);
        } else {
            zleceniaSprzedazy.add(zlecenie);
        }
    }
    public void wyswietlZlecenia(TypZlecenia typ){ // Wyświetla zlecenia kupna lub sprzedaży
        if (typ == TypZlecenia.KUPNO) {
            PriorityQueue<Zlecenie> tmp = new PriorityQueue<>(zleceniaKupna);
            while (!tmp.isEmpty()) {
                System.out.println(tmp.poll());
            }
        } else {
            PriorityQueue<Zlecenie> tmp = new PriorityQueue<>(zleceniaSprzedazy);
            while (!tmp.isEmpty()) {
                System.out.println(tmp.poll());
            }
        }
    }
    public Integer[] getOstatnie10Tur() {
        return ostatnie10Tur.toArray(new Integer[0]);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Akcja akcja = (Akcja) o;
        return id.equals(akcja.id);
    }
    @Override
    public int hashCode() {
        return id.hashCode();
    }
    public String getId() {
        return id;
    }
    public int getOstatniaCena() {
        return ostatniaCena;
    }
    public int getOstatniaTura() {
        return ostatniaTura;
    }
    public Zlecenie getZlecenieKupna() {
        return zleceniaKupna.peek();
    }
    public Zlecenie getZlecenieSprzedazy() {
        return zleceniaSprzedazy.peek();
    }
    public int liczbaZlecenKupna() {
        return zleceniaKupna.size();
    }
    public int liczbaZlecenSprzedazy() {
        return zleceniaSprzedazy.size();
    }
    public void setOstatniaCena(int cena) {
        ostatniaCena = cena;
    }
}
