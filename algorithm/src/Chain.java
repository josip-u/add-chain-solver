import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by josip on 14.05.16..
 */
public class Chain implements Comparable<Chain> {
    private final Map<BigInteger, Exponent> exponents;
    private final List<Exponent> exponentsSorted;
    /*private boolean dirty = false;*/

    private final Exponent exponent;

    public Chain(Chain chain) {
        this(chain.getExponent().getValue());

        for (Exponent exponent : chain.getExponents()) {
            Exponent result;
            Exponent newExponent = new Exponent(exponent.getValue());

            result = putIfAbsent(newExponent);
            if (result != null) {
                newExponent = result;
            }
            if (exponent.hasSummandLeft()){
                Exponent newSummandLeft = new Exponent(exponent.getSummandLeft().getValue());
                Exponent newSummandRight = new Exponent(exponent.getSummandRight().getValue());

                result = putIfAbsent(newSummandLeft);
                if (result != null) {
                    newSummandLeft = result;
                }

                result = putIfAbsent(newSummandRight);
                if (result != null) {
                    newSummandRight = result;
                }

                newExponent.setSummands(newSummandLeft, newSummandRight);
            }

        }

    }

    public Chain(BigInteger exponent) {
        this.exponent = new Exponent(exponent);
        exponents = new HashMap<>();
        exponentsSorted = new ArrayList<>();
        exponents.put(exponent, this.exponent);
    }

    public Integer size() {
        return exponents.size();
    }

    /*
        private void refreshExponentsSorted() {
            exponentsSorted.clear();
            exponentsSorted.addAll(exponents.values());
            Collections.sort(exponentsSorted, (o1, o2) -> o2.compareTo(o1));
            dirty = false;
        }
    */
    public Exponent putIfAbsent(Exponent exponent) {
        return exponents.putIfAbsent(exponent.getValue(), exponent);
    }

    public Exponent remove(BigInteger value) {
        return exponents.remove(value);
    }


    public Exponent getExponent() {
        return exponent;
    }

    public Exponent getNth(long index) {
        return exponents.values().stream().skip(index).findFirst().orElse(null);
    }

    public Exponent getRandom() {
        return exponents.values().stream().skip(ThreadLocalRandom.current().nextInt(size())).findFirst().orElse(null);
    }

    public Exponent get(BigInteger value) {
        return exponents.get(value);
    }

    public Collection<Exponent> getExponents() {
        return exponents.values();
    }

    public Map<BigInteger, Exponent> getExponentsMap() {
        return Collections.unmodifiableMap(exponents);
    }

    /*
        public Collection<Exponent> getExponentsDescLE(Exponent exponent) {
            if (dirty) {
                refreshExponentsSorted();
            }
            Exponent first = exponentsSorted.stream().filter(exponent1 -> exponent1.compareTo(exponent) <= 0).findFirst().orElse(null);

            return exponentsSorted.subList(exponentsSorted.indexOf(first), exponentsSorted.size());
        }

        public Collection<Exponent> getExponentsDescGT(Exponent exponent) {
            if (dirty) {
                refreshExponentsSorted();
            }
            Exponent first = exponentsSorted.stream().filter(exponent1 -> exponent1.compareTo(exponent) <= 0).findFirst().orElse(null);

            return exponentsSorted.subList(0, exponentsSorted.indexOf(first));
        }
    */
    @Override
    public String toString() {
        return exponents.values().stream().sorted(Exponent::compareTo)
                .map(
                        exponent -> exponent.getValue().toString() + "(" +
                                exponent.getParents().stream()
                                        .map(exponent1 -> exponent1.toString() + (exponents.containsValue(exponent1) ? "-1" : "-0"))
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
