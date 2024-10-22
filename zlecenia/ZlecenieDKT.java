package zlecenia;

import inwestorzy.Inwestor;

// Zlecenie ważne do końca określonej tury
public class ZlecenieDKT extends Zlecenie{
    public ZlecenieDKT(TypZlecenia typ, String idAkcji, int liczbaAkcji, int limitCeny,
                       int turaZlozenia, long kolejnoscZlozenia, int turaElminacji, Inwestor inwestor) {
        super(typ, idAkcji, liczbaAkcji, limitCeny, turaZlozenia, kolejnoscZlozenia, turaElminacji, inwestor);
    }
}
