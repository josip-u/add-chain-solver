import java.util.Comparator;

/**
 * Created by josip on 14.05.16..
 */
public class Exponent implements Comparable<Exponent> {
    private int value;
    private Exponent summandLeft;

    public Exponent(int value) {
        this.value = value;
    }

    @Override
    public int compareTo(Exponent exponent) {
        return value == exponent.value ? 0 : value < exponent.value ? 1 : -1;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

    public Exponent getSummandLeft() {
        return summandLeft;
    }

    public void setSummand(Exponent exponent){
        if (exponent.value <= (value - 1) / 2){
            throw new IllegalArgumentException("Summand must be greater than or equal to half of exponent.");
        }
        summandLeft = exponent;
    }

}
