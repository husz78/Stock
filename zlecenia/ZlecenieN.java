package zlecenia;

import inwestorzy.Inwestor;

// Zlecenie natychmiastowe
public class ZlecenieN extends Zlecenie{
    public ZlecenieN(TypZlecenia typ, String idAkcji, int liczbaAkcji, int limitCeny,
                     int turaZlozenia, long kolejnoscZlozenia, Inwestor inwestor) {
        super(typ, idAkcji, liczbaAkcji, limitCeny, turaZlozenia, kolejnoscZlozenia, turaZlozenia, inwestor);
    }

}
