import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by josip on 14.05.16..
 */
public class Chain implements Comparable<Chain> {
    private final SortedMap<BigInteger, Exponent> exponents;

    private final Exponent exponent;

    public Chain(BigInteger exponent) {
        this.exponent = new Exponent(exponent);
        exponents = new TreeMap<>((o1, o2) -> o2.compareTo(o1));
        exponents.put(exponent, this.exponent);
    }

    public Integer size() {
        return exponents.size();
    }

    public boolean add(Exponent exponent) {
        if (!exponents.containsKey(exponent.getValue())) {
            exponents.put(exponent.getValue(), exponent);
            return true;
        }
        return false;
    }

    public boolean remove(BigInteger value) {
        return exponents.remove(value) != null;
    }

    public Exponent getExponent() {
        return exponent;
    }

    public Exponent getNth(long index) {
        long i = 0;
        for (Exponent current : exponents.values()) {
            if (i == index) {
                return current;
            }
            ++i;
        }
        throw new IndexOutOfBoundsException("Chain doesn't have that many elements.");
    }

    public Exponent get(BigInteger value) {
        return exponents.get(value);
    }

    public SortedMap<BigInteger, Exponent> getExponentsDescLE(Exponent exponent) {
        return exponents.tailMap(exponent.getValue());
    }

    @Override
    public String toString() {
        return exponents.values().stream().sorted((o1, o2) -> o1.compareTo(o2))
                .map(
                        exponent -> exponent.getValue().toString() + "(" +
                                exponent.getParents().stream()
                                .map(exponent1 -> exponent1.toString())
                                .collect(Collectors.joining(",")
                                ) + ") "
                )
                .collect(Collectors.joining("->"));
    }

    @Override
    public int compareTo(Chain chain) {
        return size().compareTo(chain.size());
    }
}
