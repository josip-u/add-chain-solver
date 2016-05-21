import javafx.collections.transformation.SortedList;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Created by Josip on 17.05.2016..
 */
public class Population {
    private int size;
    public List<Chain> population;


    public Population(int size) {
        this.size = size;
        this.population = new LinkedList<>();
    }

    public void initialize(ChainFactory factory) {
        this.population.clear();
        for (int i = 0; i < size; i++) {
            population.add(factory.generate());
        }
    }

    @Override
    public String toString() {
        return population.stream().map(Chain::toString).collect(Collectors.joining("\n"));
    }
}
