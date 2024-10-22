package testy;

import inwestorzy.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import system.*;
import zlecenia.TypZlecenia;
import zlecenia.Zlecenie;

import static org.junit.jupiter.api.Assertions.*;

public class SystemTransakcyjnyTest {
    SystemTransakcyjny system;
    DaneSMA daneSMA;
    @BeforeEach
    void setUp() {
        system = new SystemTransakcyjny(40);
        daneSMA = new DaneSMA(system);
    }
    @Test
    void InwestorzyRandom() {
        Inwestor[] inwestors = new Inwestor[10];
        for (int i = 0; i < inwestors.length; i++) {
            inwestors[i] = new Random(i);
            inwestors[i].getPortfel().setSaldo(1000);
        }
        Akcja akcja1 = new Akcja("Akcja1", 100);
        Akcja akcja2 = new Akcja("Akcja2", 35);
        for (Inwestor inwestor : inwestors) system.dodajInwestora(inwestor);

        system.dodajAkcje(akcja1);
        system.dodajAkcje(akcja2);
        for (Inwestor inwestor : inwestors) {
            inwestor.getPortfel().dodajAkcje(akcja1, 14);
            inwestor.getPortfel().dodajAkcje(akcja2, 15);
        }
        while (system.getNrTury() < system.getLiczbaTur()) {
            system.zapytajInwestorow();
            system.zrealizujZlecenia();
            system.nastepnaTura();
        }
        int sumaPieniedzy = 0;
        Akcja[] akcje = system.getAkcje();
        int sumaAkcji1 = 0;
        int sumaAkcji2 = 0;

        for (Inwestor inwestor : system.getInwestorzy()) {
            sumaPieniedzy += inwestor.getPortfel().getSaldo();
            sumaAkcji2 += inwestor.getPortfel().dajLiczbeAkcji(akcja2);
            sumaAkcji1 += inwestor.getPortfel().dajLiczbeAkcji(akcja1);
        }
        assertEquals(sumaPieniedzy, 10000);
        assertEquals(sumaAkcji1, 140);
        assertEquals(sumaAkcji2, 150);
    }

    @Test
    void InwestorSMA() {
        Inwestor sma = new SMA(0);
        sma.getPortfel().setSaldo(765);
        Akcja akcja1 = new Akcja("Akcja1", 23);
        sma.getPortfel().dodajAkcje(akcja1, 7);
        system.dodajInwestora(sma);
        system.dodajAkcje(akcja1);

        while (system.getNrTury() < system.getLiczbaTur()) {
            daneSMA.obliczSMA();
            daneSMA.powiadomSMA();
            Sygnal sygnal = daneSMA.getSygnal();
            system.zapytajInwestorow();
            akcja1.setOstatniaCena(system.getNrTury() % 15);
            if (sygnal == Sygnal.KUP && akcja1.liczbaZlecenKupna() == 1) {
                Zlecenie zlecenie = akcja1.getZlecenieKupna();
                assertEquals(zlecenie.getTyp(), TypZlecenia.KUPNO);
                assertEquals(zlecenie.getInwestor(), sma);
            }
            if (sygnal == Sygnal.SPRZEDAJ && akcja1.liczbaZlecenSprzedazy() == 1) {
                Zlecenie zlecenie = akcja1.getZlecenieSprzedazy();
                assertEquals(zlecenie.getTyp(), TypZlecenia.SPRZEDAZ);
                assertEquals(zlecenie.getInwestor(), sma);
            }
            system.zrealizujZlecenia();
            system.nastepnaTura();
        }
        assertEquals(sma.getPortfel().getSaldo(), 765);
    }
    @Test
    void DuzoInwestorow() {
        Inwestor[] inwestors = new Inwestor[10000];
        for (int i = 0; i < 10000; i++) {
            if (i % 2 == 0) inwestors[i] = new Random(i);
            else inwestors[i] = new SMA(i);
            inwestors[i].getPortfel().setSaldo(2000);
            system.dodajInwestora(inwestors[i]);
        }
        Akcja[] akcje = new Akcja[100];
        for (int i = 0; i < 100; i++) {
            akcje[i] = new Akcja("Akcja" + i, i * i / 2);
            system.dodajAkcje(akcje[i]);
        }
        for (int i = 0; i < 10000; i++) {
            inwestors[i].getPortfel().dodajAkcje(akcje[i % 100], i % 30);
            inwestors[i].getPortfel().dodajAkcje(akcje[0], 1000);
        }

        while (system.getNrTury() < system.getLiczbaTur()) {
            system.zapytajInwestorow();
            system.zrealizujZlecenia();
            system.nastepnaTura();
        }
        int sumaPieniedzy = 0;
        int maxLiczbaAkcji29 = 0;
        int maxLiczbaAkcji0 = 0;
        for (Inwestor inwestor : system.getInwestorzy()) {
            sumaPieniedzy += inwestor.getPortfel().getSaldo();
            maxLiczbaAkcji29 = Math.max(maxLiczbaAkcji29, inwestor.getPortfel().dajLiczbeAkcji(akcje[29]));
            maxLiczbaAkcji0 = Math.max(maxLiczbaAkcji0, inwestor.getPortfel().dajLiczbeAkcji(akcje[0]));
        }
        assertEquals(sumaPieniedzy, 20000000);
        assertTrue(maxLiczbaAkcji29 >= 29);
        assertTrue(maxLiczbaAkcji0 >= 1000);
    }
}
