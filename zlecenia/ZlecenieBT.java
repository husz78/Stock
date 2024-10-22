package zlecenia;

import inwestorzy.Inwestor;

// Zlecenie bez określonego terminu ważności
public class ZlecenieBT extends Zlecenie{
    public ZlecenieBT(TypZlecenia typ, String idAkcji, int liczbaAkcji, int limitCeny,
                      int turaZlozenia, long kolejnoscZlozenia, Inwestor inwestor) {
        super(typ, idAkcji, liczbaAkcji, limitCeny, turaZlozenia, kolejnoscZlozenia, Integer.MAX_VALUE, inwestor);
    }
}
