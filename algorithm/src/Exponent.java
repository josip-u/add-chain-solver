import java.math.BigInteger;
import java.util.*;

/**
 * Created by josip on 14.05.16..
 */
public class Exponent implements Comparable<Exponent> {
    private BigInteger value;
    private Exponent summandLeft;
    private Exponent summandRight;
    private SortedSet<Exponent> parents;

    public Exponent(BigInteger value) {
        this.value = value;
        parents = new TreeSet<>();
    }

    @Override
    public int compareTo(Exponent exponent) {
        return this.value.compareTo(exponent.value);
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

    public Exponent getSummandLeft() {
        return summandLeft;
    }

    public boolean hasSummandLeft() {
        return summandLeft != null;
    }

    public Exponent getSummandRight() {
        return summandRight;
    }

    public boolean hasSummandRight() {
        return summandRight != null;
    }

    public void setSummands(Exponent summandLeft, Exponent summandRight) {
        summandLeft.parents.add(this);
        summandRight.parents.add(this);
        this.summandLeft = summandLeft.max(summandRight);
        this.summandRight = summandRight.min(summandLeft);
    }

    public void setSummands(Collection<Exponent> summands) {
        for (Exponent summand : summands) {
            summand.parents.add(this);
        }
        this.summandLeft = summands.stream().max(Exponent::compareTo).orElse(null);
        this.summandRight = summands.stream().min(Exponent::compareTo).orElse(null);
    }

    public void removeParent(Exponent parent) {
        parents.remove(parent);
    }

    public void removeParents(Collection<Exponent> parents) {
        parents.removeAll(parents);
    }

    public void removeParent(BigInteger parentValue) {
        Exponent parent = parents.stream().filter(exponent -> exponent.value.equals(parentValue)).findFirst().orElse(null);
        if (parent != null) {
            removeParent(parent);
        }
    }

    public BigInteger getValue() {
        return value;
    }

    public SortedSet<Exponent> getParents() {
        return parents;
    }

    public Exponent min(Exponent exponent) {
        return this.compareTo(exponent) < 0 ? this : exponent;
    }

    public Exponent max(Exponent exponent) {
        return this.compareTo(exponent) > 0 ? this : exponent;
    }
}
