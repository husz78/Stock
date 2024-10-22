package system;

import inwestorzy.Inwestor;
import zlecenia.*;

import java.util.ArrayList;
import java.util.HashMap;

public class SystemTransakcyjny {
    private int nrTury; // Numer aktualnej tury w systemie
    private ArrayList<Inwestor> inwestorzy; // Lista inwestorów w systemie
    private int liczbaTur; // Maksymalna liczba tur w systemie
    private HashMap<String, Akcja> akcje; // Słownik przechowujący wszystkie akcje spółek w systemie
    private long liczbaZlecen; // Łączna liczba zleconych zleceń w systemie podczas wszystkich tur

    public SystemTransakcyjny(int liczbaTur) {
        this.nrTury = 0;
        this.inwestorzy = new ArrayList<>();
        this.liczbaTur = liczbaTur;
        this.akcje = new HashMap<>();
        this.liczbaZlecen = 0;
    }
    public void nastepnaTura() { // Przechodzi do następnej tury
        nrTury++;
    }
    public int getNrTury() {
        return nrTury;
    }
    public void wypiszStanPortfeli() { // Wypisuje stan portfeli wszystkich inwestorów
        for (Inwestor inwestor : inwestorzy) {
            System.out.println(inwestor.getPortfel());
        }
    }
    public void zrealizujZlecenia() { // Realizuje zlecenia kupna i sprzedaży
        for (Akcja akcja : akcje.values()) {
            akcja.zrealizujZlecenia(this);
        }
    }
    public void zapytajInwestorow() { // Pyta inwestorów o złożenie zleceń
        for (Inwestor inwestor : inwestorzy) {
            inwestor.podejmijDecyzje(this);
        }
    }
    public void dodajAkcje(Akcja akcja) { // Dodaje akcję do systemu
        akcje.put(akcja.getId(), akcja);
    }
    public void dodajInwestora(Inwestor inwestor) { // Dodaje inwestora do systemu
        inwestorzy.add(inwestor);
    }
    public int[] dajOstatniaTransakcje(String id) { // Zwraca cenę ostatniej transakcji danej akcji oraz
        // turę, w której ta transakcja miała miejsce w postaci tablicy dwuelementowej
        Akcja akcja = akcje.get(id);
        int[] result = new int[2];
        result[0] = akcja.getOstatniaCena();
        result[1] = akcja.getOstatniaTura();
        return result;
    }
    public long getLiczbaZlecen() {
        return liczbaZlecen;
    }
    public void incLiczbaZlecen() {
        liczbaZlecen++;
    }
    public int getLiczbaTur() {
        return liczbaTur;
    }
    public Inwestor[] getInwestorzy() {
        return inwestorzy.toArray(new Inwestor[0]);
    }
    public void dodajZlecenie(Zlecenie zlecenie) { // Dodaje zlecenie do systemu
        String idAkcji = zlecenie.getIdAkcji();
        Akcja akcja = akcje.get(idAkcji);
        akcja.dodajZlecenie(zlecenie);
    }
    public Akcja[] getAkcje() {
        return akcje.values().toArray(new Akcja[0]);
    }
    public Akcja dajAkcje(String id) {
        return akcje.get(id);
    }
    public void przeprowadzTure() { // Przeprowadza jedną turę w systemie
        zapytajInwestorow();
        zrealizujZlecenia();
        nastepnaTura();
    }
}
