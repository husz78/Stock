package zlecenia;

import inwestorzy.Inwestor;


public abstract class Zlecenie implements Comparable<Zlecenie>{
    private TypZlecenia typ; // Typ zlecenia (kupna lub sprzedaży)
    private String idAkcji; // Identyfikator akcji
    private int liczbaAkcji; // Liczba akcji w zleceniu
    private int limitCeny; // Limit ceny w zleceniu (dolny w sprzedaży, górny w kupnie)
    private int turaZlozenia; // Numer tury, w której zlecenie zostało złożone
    private int turaElminacji; // Numer tury, w której zlecenie będzie musiało być usunięte
    private long kolejnoscZlozenia; // Numer zlecenia, który decyduje o kolejności
    // realizacji zleceń w tej samej turze
    private Inwestor inwestor; // Inwestor, który złożył zlecenie

    protected Zlecenie(TypZlecenia typ, String idAkcji, int liczbaAkcji, int limitCeny,
                    int turaZlozenia, long kolejnoscZlozenia, int turaElminacji, Inwestor inwestor) {
        this.typ = typ;
        this.idAkcji = idAkcji;
        this.liczbaAkcji = liczbaAkcji;
        this.limitCeny = limitCeny;
        this.turaZlozenia = turaZlozenia;
        this.kolejnoscZlozenia = kolejnoscZlozenia;
        this.turaElminacji = turaElminacji;
        this.inwestor = inwestor;
    }

    @Override
    public int compareTo(Zlecenie zlecenie) {
        if (this.getTyp() == TypZlecenia.SPRZEDAZ) {
            int res = Integer.compare(this.getLimitCeny(), zlecenie.getLimitCeny());
            if (res != 0) return res;
            int res2 = Integer.compare(this.getTuraZlozenia(), zlecenie.getTuraZlozenia());
            if (res2 != 0) return res2;
            return Long.compare(this.getKolejnoscZlozenia(), zlecenie.getKolejnoscZlozenia());
        }
        else {
            int res = Integer.compare(zlecenie.getLimitCeny(), this.getLimitCeny());
            if (res != 0) return res;
            int res2 = Integer.compare(this.getTuraZlozenia(), zlecenie.getTuraZlozenia());
            if (res2 != 0) return res2;
            return Long.compare(this.getKolejnoscZlozenia(), zlecenie.getKolejnoscZlozenia());
        }
    }
    // Zwraca limit ceny zlecenia, które zostało złożone wcześniej
    public static int cenaWczesniejszegoZlecenia(Zlecenie zlecenie1, Zlecenie zlecenie2) {
        if (zlecenie1.getTuraZlozenia() < zlecenie2.getTuraZlozenia())
            return zlecenie1.getLimitCeny();
        else if (zlecenie1.getTuraZlozenia() > zlecenie2.getTuraZlozenia())
            return zlecenie2.getLimitCeny();
        else {
            if (zlecenie1.getKolejnoscZlozenia() < zlecenie2.getKolejnoscZlozenia())
                return zlecenie1.getLimitCeny();
            else
                return zlecenie2.getLimitCeny();
        }
    }

    @Override
    public String toString() {
        return "Zlecenie{" +
                "typ=" + typ +
                ", limitCeny=" + limitCeny +
                ", liczbaAkcji=" + liczbaAkcji +
                ", turaZlozenia=" + turaZlozenia +
                ", turaElminacji=" + turaElminacji +
                ", kolejnoscZlozenia=" + kolejnoscZlozenia +
                ", inwestor=" + inwestor +
                '}';
    }

    public TypZlecenia getTyp() {
        return typ;
    }
    public String getIdAkcji() {
        return idAkcji;
    }
    public int getLiczbaAkcji() {
        return liczbaAkcji;
    }
    public int getLimitCeny() {
        return limitCeny;
    }
    public int getTuraZlozenia() {
        return turaZlozenia;
    }
    public int getTuraElminacji() {
        return turaElminacji;
    }
    public long getKolejnoscZlozenia() {
        return kolejnoscZlozenia;
    }
    public Inwestor getInwestor() {
        return inwestor;
    }
    public void odejmijLiczbeAkcji(int liczba) {
        liczbaAkcji -= liczba;
    }

}
