import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.DoubleAccumulator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Josip on 22.05.2016..
 */
public class mainCrossover {
    public static void main(String[] args) {
        final int TOURNAMENT_SIZE = 4;

        OperatorMutation mutation1 = new OperatorMutationSplitNode();
        OperatorMutation mutation2 = new OperatorMutationAddOne();
        OperatorCrossover crossover = new OperatorCrossoverCombine();
        OperatorSelection selection = new OperatorSelectionRandom();

//        ChainFactory factory = new ChainMutationFactoryDecorator(new ChainBinaryFactory(BigInteger.valueOf(60)), mutation1);
//        ChainFactory factory = new ChainMutationFactoryDecorator(new ChainBinaryFactory(new BigInteger("170141183460469231731687303715884105725")), mutation2);
        ChainFactory factory = new ChainMutationFactoryDecorator(new ChainBinaryFactory(new BigInteger("6490123999")), mutation2);

        Population population = new Population(300);
        population.initialize(factory);

        System.out.println(population.getNth(0).size() + ", " + population.getNth(0));

        Set<Chain> tournament = new HashSet<>();
        List<Chain> parents;
        List<Chain> worst;
        List<Chain> childrenModified = new ArrayList<>();

        Chain bestChain = factory.generate();

        long startTime = System.currentTimeMillis();

        List<Integer> counterList = new ArrayList<>();

        for (int i = 0; i < 1000000; i++) {
            tournament.clear();
            childrenModified.clear();

            while (tournament.size() < TOURNAMENT_SIZE) {
                tournament.add(selection.select(population));
            }

            parents = tournament.stream().sorted().limit(2).collect(Collectors.toList());

            tournament.removeAll(parents);
            worst = tournament.stream().skip(TOURNAMENT_SIZE - 4).limit(2).collect(Collectors.toList());

            population.removeAll(worst);
            if (ThreadLocalRandom.current().nextDouble() < 0.75) {
                childrenModified.add(mutation1.mutate(parents.get(0)));
            } else {
                childrenModified.add(new Chain(parents.get(0)));
            }

            if (ThreadLocalRandom.current().nextDouble() < 0.25) {
                childrenModified.add(mutation2.mutate(parents.get(1)));
            } else {
                childrenModified.add(new Chain(parents.get(1)));
            }

            population.addAll(childrenModified);


            if (population.getBestChainEver().compareTo(bestChain) < 0) {
                bestChain = population.getBestChainEver();
                System.out.println(i + ": Pop. size:" + population.size() + ", Chain size: " + bestChain.size());
            }

            if (i % 20000 == 0) {
                System.out.println("-- " + i / 20000 + "% --");
            }

        }

        long endTime = System.currentTimeMillis();

        System.out.println();
        System.out.println("Time elapsed: " + (endTime - startTime) / 1000.0 + " s");
        System.out.println("Avg size: " + population.stream().mapToInt(Chain::size).average().orElse(0));

        Chain result = population.getNth(0);
        System.out.println();
        //System.out.println(population);
        System.out.println();

        System.out.println("Pop. size:" + population.size() + ", Chain size: " + bestChain.size());

        System.out.println(bestChain.size() + ", " + bestChain);

    }
}
