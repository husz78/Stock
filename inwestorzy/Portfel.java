package inwestorzy;

import system.Akcja;

import java.util.HashMap;

public class Portfel {
    private int saldo; ; // Saldo portfela w jednostkach monetarnych
    private HashMap<Akcja, Integer> akcje; // Słownik przechowujący akcje w portfelu wraz z ich liczbą

    public Portfel() {
        this.saldo = 0;
        this.akcje = new HashMap<>();
    }
    public int getSaldo() {
        return saldo;
    }
    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }
    public void dodajSaldo(int kwota) { // Dodaje kwotę do salda
        saldo += kwota;
    }
    public void odejmijSaldo(int kwota) { // Odejmuje kwotę od salda
        saldo -= kwota;
    }
    public void dodajAkcje(Akcja akcja, int liczba) { // Dodaje daną liczbę akcji do portfela
        if (akcje.containsKey(akcja)) {
            akcje.put(akcja, akcje.get(akcja) + liczba);
        } else {
            akcje.put(akcja, liczba);
        }
    }
    public void usunAkcje(Akcja akcja, int liczba) { // Usuwa daną liczbę akcji z portfela
        if (akcje.containsKey(akcja)) {
            akcje.put(akcja, akcje.get(akcja) - liczba);
        }
    }
    public int dajLiczbeAkcji(Akcja akcja) { // Zwraca liczbę akcji danej spółki w portfelu
        if (akcje.containsKey(akcja)) {
            return akcje.get(akcja);
        } else {
            return 0;
        }
    }
    @Override
    public String toString() { // Zwraca zawarość portfela w postaci napisu
        String result = "" + saldo;
        for (HashMap.Entry<Akcja, Integer> rekord : akcje.entrySet()) {
            result += " " + rekord.getKey().getId() + ": " + rekord.getValue();
        }
        return result;
    }
}
