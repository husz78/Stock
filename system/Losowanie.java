package system;

public class Losowanie {
    public static int losuj(int a, int b) { // Losuje liczbę z przedziału [a, b]
        return a + (int) (Math.random() * (b - a + 1));
    }
}
