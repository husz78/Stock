package symulacja;

import inwestorzy.*;
import system.*;
import zlecenia.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static boolean checkIfNumber(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    public static void main(String[] args) {
        int liczbaTur;
        int idInwestora = 0;
        try {
            if (args.length != 2) {
                throw new InputException("Nieprawidłowa liczba argumentów");
            }
            if (checkIfNumber(args[1])) {
                liczbaTur = Integer.parseInt(args[1]);
            } else {
                throw new InputException("Nieprawidłowa liczba tur: " + args[1]);
            }
            SystemTransakcyjny system = new SystemTransakcyjny(liczbaTur); // Tworzymy system transakcyjny
            File plik = new File(args[0]);
            Scanner scanner = new Scanner(plik);
            while (scanner.hasNextLine()) { // Czytamy plik linia po linii
                String line = scanner.nextLine();
                if (line.charAt(0) == '#') continue;
                String[] words = line.split(" ");
                if (words[0].equals("R") || words[0].equals("S")) {
                    for (String word : words) {
                        if (word.equals("R")) {
                            Random random = new Random(idInwestora);
                            system.dodajInwestora(random);
                            idInwestora++;
                        } else if (word.equals("S")) {
                            SMA sma = new SMA(idInwestora);
                            system.dodajInwestora(sma);
                            idInwestora++;
                        }
                    }
                }
                else if (checkIfNumber(words[0])) {
                    int saldo = Integer.parseInt(words[0]);
                    for (Inwestor inwestor : system.getInwestorzy()) {
                        inwestor.getPortfel().setSaldo(saldo);
                    }
                    for (int i = 1; i < words.length; i++) {
                        String[] akcja = words[i].split(":");
                        if (akcja.length != 2) { // Musimy mieć idAkcji:liczbaAkcji
                           throw new InputException("Nieprawidłowy format danych: " + words[i]);
                        }
                        if (akcja[0].length() > 5) { // Id akcji nie może być dłuższe niż 5 znaków
                            throw new InputException("Nieprawidłowe id akcji: " + akcja[0]);
                        }
                        if (!checkIfNumber(akcja[1])) { // Liczba akcji musi być liczbą
                            throw new InputException("Nieprawidłowa liczba akcji: " + akcja[1]);
                        }
                        int liczbaAkcji = Integer.parseInt(akcja[1]);
                        Akcja idAkcji = system.dajAkcje(akcja[0]);
                        if (idAkcji == null) {
                            throw new InputException("Nie ma akcji o id: " + akcja[0]);
                        }
                        for (Inwestor inwestor : system.getInwestorzy()) {
                            inwestor.getPortfel().dodajAkcje(system.dajAkcje(akcja[0]), liczbaAkcji);
                        }
                    }
                }
                else {
                    for (String word : words) {
                        String[] akcja = word.split(":");
                        if (akcja.length != 2) { // Musimy mieć idAkcji:ostatniaCena
                            throw new InputException("Nieprawidłowy format danych: " + word);
                        }
                        if (akcja[0].length() > 5) { // Id akcji nie może być dłuższe niż 5 znaków
                            throw new InputException("Nieprawidłowe id akcji: " + akcja[0]);
                        }
                        if (!checkIfNumber(akcja[1])) { // Cena musi być liczbą
                            throw new InputException("Nieprawidłowa cena akcji: " + akcja[1]);
                        }
                        int ostatniaCena = Integer.parseInt(akcja[1]);
                        Akcja nowaAkcja = new Akcja (akcja[0], ostatniaCena);
                        system.dodajAkcje(nowaAkcja);
                    }
                }

            }
            scanner.close();
            DaneSMA daneSMA = new DaneSMA(system);
            while (system.getLiczbaTur() > system.getNrTury()) {
                daneSMA.obliczSMA();
                daneSMA.powiadomSMA();
                system.przeprowadzTure();
            }
            system.wypiszStanPortfeli();
        }
        catch (FileNotFoundException | InputException e) {
            System.out.println(e.getMessage());
        }
    }

}
