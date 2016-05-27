import javafx.collections.transformation.SortedList;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Created by Josip on 17.05.2016..
 */
public class Population implements Collection<Chain> {
    private final int sizeMax;

    private Chain bestChainEver = null;

    public List<Chain> population;


    public Population(int sizeMax) {
        this.sizeMax = sizeMax;
        this.population = new ArrayList<>();
    }

    public void initialize(ChainFactory factory) {
        this.population.clear();
        for (int i = 0; i < sizeMax; i++) {
            population.add(factory.generate());
        }
    }

    public int getSizeMax() {
        return sizeMax;
    }

    public Chain getBestChainEver() {
        return bestChainEver;
    }

    public Chain getNth(int n) {
        if (n >= size()) {
            return null;
        }
        return population.get(n);
    }

    public Chain getRandom(){
        Chain result;
        do {
            result = population.get(ThreadLocalRandom.current().nextInt(size()));
        } while (result == null);
        return result;
    }

    @Override
    public String toString() {
        return population.stream().map(Chain::toString).collect(Collectors.joining("\n"));
    }

    @Override
    public int size() {
        return population.size();
    }

    @Override
    public boolean isEmpty() {
        return population.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return population.contains(o);
    }

    @Override
    public Iterator<Chain> iterator() {
        return population.iterator();
    }

    @Override
    public Object[] toArray() {
        return population.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return population.toArray(a);
    }

    @Override
    public boolean add(Chain exponents) {
        if (size() < sizeMax) {
            if (bestChainEver == null || exponents.compareTo(bestChainEver) < 0) {
                bestChainEver = exponents;
            }
            return population.add(exponents);
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return population.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return population.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Chain> c) {

        if (size() < sizeMax) {
            Chain tmp = c.stream().min(Chain::compareTo).orElse(null);
            if (tmp != null && (bestChainEver == null || tmp.compareTo(bestChainEver) < 0)) {
                bestChainEver = tmp;
            }

            return population.addAll(c);
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return population.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return population.retainAll(c);
    }

    @Override
    public void clear() {
        population.clear();
    }
}
