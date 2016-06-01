import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Created by Josip on 22.05.2016..
 */
public class mainCrossover {
    public static void main(String[] args) {
        final int TOURNAMENT_SIZE = 8;

        BigInteger exponent = new BigInteger("170141183460469231731687303715884105725");
        if (args.length > 0){
            exponent = new BigInteger(args[0]);
        }



        OperatorMutation mutation1 = new OperatorMutationSplitNode();
        OperatorMutation mutation2 = new OperatorMutationAddOne();
        OperatorCrossover crossover = new OperatorCrossoverCombine();
        OperatorSelection selection = new OperatorSelectionRandom();

//        ChainFactory factory = new ChainMutationFactoryDecorator(new ChainBinaryFactory(BigInteger.valueOf(60)), mutation1);
        ChainFactory factory = new ChainMutationFactoryDecorator(
                new ChainMutationFactoryDecorator(
                        new ChainBinaryFactory(
//                                new BigInteger("170141183460469231731687303715884105725")
//                                new BigInteger("2").pow(37).subtract(BigInteger.valueOf(3))
//                                new BigInteger("2").pow(67).subtract(BigInteger.valueOf(3))
                                exponent
                        ), mutation2
                ), mutation1
        );
//        ChainFactory factory = new ChainMutationFactoryDecorator(new ChainBinaryFactory(new BigInteger("6490123999")), mutation2);

        Population population = new Population(1000);
        population.initialize(factory);

        System.out.println(population.getNth(0).size() + ", " + population.getNth(0));

        Set<Chain> tournament = new HashSet<>();
        List<Chain> parents;
        List<Chain> children;
        List<Chain> worst;
        List<Chain> childrenModified = new ArrayList<>();

        Chain bestChain = factory.generate();

        long startTime = System.currentTimeMillis();

        int iStagnation = 0;

        for (int i = 0; i < 10000000; i++) {
            if (i - iStagnation == 100000) {
                break;
            }
            tournament.clear();
            childrenModified.clear();

            while (tournament.size() < TOURNAMENT_SIZE) {
                tournament.add(selection.select(population));
            }

            parents = tournament.stream().sorted().limit(2).collect(Collectors.toList());

            children = (List<Chain>) crossover.crossover(parents.get(0), parents.get(1));

            tournament.removeAll(parents);
            worst = tournament.stream().skip(TOURNAMENT_SIZE - 4).limit(2).collect(Collectors.toList());

            population.removeAll(worst);
            if (ThreadLocalRandom.current().nextDouble() < 0.75) {
                childrenModified.add(mutation1.mutate(children.get(0)));
            } else {
                childrenModified.add(children.get(0));
            }

            if (ThreadLocalRandom.current().nextDouble() < 0.75) {
                childrenModified.add(mutation2.mutate(children.get(1)));
            } else {
                childrenModified.add(children.get(1));
            }

            population.addAll(childrenModified);


            if (population.getBestChainEver().compareTo(bestChain) < 0) {
                bestChain = population.getBestChainEver();
                System.out.println(i + ": Pop. size:" + population.size() + ", Chain size: " + bestChain.size());
                iStagnation = i;
            }

            if (i % 10000 == 0) {
                System.out.println("-- " + i / 10000 + "% --");
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
