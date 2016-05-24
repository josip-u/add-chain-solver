import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Josip on 22.05.2016..
 */
public class mainCrossover {
    public static void main(String[] args) {
        final int TOURNAMENT_SIZE = 3;

        OperatorMutation mutation1 = new OperatorMutationSplitNode();
        OperatorMutation mutation2 = new OperatorMutationAddOne();
        OperatorCrossover crossover = new OperatorCrossoverCombine();
        OperatorSelection selection = new OperatorSelectionProportional();

        ChainBinaryFactory factory = new ChainBinaryFactory(BigInteger.valueOf(20));

        Population population = new Population(100);
        population.initialize(factory);

        List<Chain> tournament = new ArrayList<>();
        Stream<Chain> sortedTournament;
        List<Chain> parents;
        Collection<Chain> children;

        for (int i = 0; i < 100000; i++) {
            tournament.clear();
            System.out.println(i+":" + population.size());

            while (tournament.size() < TOURNAMENT_SIZE){
                tournament.add(selection.select(population));
            }

            sortedTournament = tournament.stream().sorted();
            parents = sortedTournament.limit(2).collect(Collectors.toList());
            children = crossover.crossover(parents.get(0), parents.get(1));
            population.removeAll(parents);
            population.add(children.stream().min(Chain::compareTo).orElse(null));
            population.add(mutation1.mutate(children.stream().max(Chain::compareTo).orElse(null)));

        }

    }
}
