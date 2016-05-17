import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by josip on 14.05.16..
 */
public class Chain {
    private final SortedSet<Exponent> exponents;

    public Chain(int exponent) {
        exponents = new TreeSet<>();
        exponents.add(new Exponent(exponent));
    }

    public int size() {
        return exponents.size();
    }

    public boolean add(Exponent exponent) {
        return exponents.add(exponent);
    }

    public Exponent first() {
        return exponents.first();
    }

    @Override
    public String toString() {
        Exponent current = first();
        String result = current.toString();
        Exponent next = current.getSummandLeft();

        while (next != null) {
            result += "->" + next;
            next = next.getSummandLeft();
        }

        return result;
    }
}
