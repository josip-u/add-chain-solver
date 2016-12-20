import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Created by Josip on 22.05.2016..
 */
public class mainCrossover {
    public static void main(String[] args) {
        final int TOURNAMENT_SIZE = 20;

//        BigInteger exponent = new BigInteger("12509");
        BigInteger exponent = new BigInteger("170141183460469231731687303715884105725");
//        BigInteger exponent = new BigInteger("158456325028528675187087900669"); //2^97-3
        if (args.length > 0) {
            exponent = new BigInteger(args[0]);
        }


        OperatorMutation mutation1 = new OperatorMutationSplitNode();
        OperatorMutation mutation2 = new OperatorMutationAddOne();
        OperatorMutation mutation3 = new OperatorMutationAddRandom();
        OperatorCrossover crossover = new OperatorCrossoverCombine();
        OperatorSelection selection = new OperatorSelectionRandom();

//        ChainFactory factory = new ChainMutationFactoryDecorator(new ChainBinaryFactory(BigInteger.valueOf(60)), mutation1);
        ChainFactory factory = new ChainMutationFactoryDecorator(
                new ChainMutationFactoryDecorator(
                        new ChainWindowFactory(
//                                new BigInteger("170141183460469231731687303715884105725")
//                                new BigInteger("2").pow(37).subtract(BigInteger.valueOf(3))
//                                new BigInteger("2").pow(67).subtract(BigInteger.valueOf(3))
                                exponent,
                                -1,
                                "python3 /home/josip/PycharmProjects/lab/chain-solver/chains/factory.py -r"
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

        int MAX_ITER = 1000000;

        for (int i = 0; i < MAX_ITER; i++) {
//            if (i - iStagnation == 100000) {
//                break;
//            }
            tournament.clear();
            childrenModified.clear();

            while (tournament.size() < TOURNAMENT_SIZE) {
                tournament.add(selection.select(population));
            }

            parents = tournament.stream().sorted().limit(2).collect(Collectors.toList());

            children = (List<Chain>) crossover.crossover(parents.get(0), parents.get(1));

            tournament.removeAll(parents);
            worst = tournament.stream().sorted(Comparator.reverseOrder()).limit(2).collect(Collectors.toList());

            population.removeAll(worst);
            double choice = ThreadLocalRandom.current().nextDouble();
            if (choice < 0.6) {
                childrenModified.add(mutation1.mutate(children.get(0)));
            } else if (choice < 0.7) {
                childrenModified.add(mutation2.mutate(children.get(0)));
            } else if (choice < 0.8) {
                childrenModified.add(mutation3.mutate(children.get(0)));
            } else {
                childrenModified.add(children.get(0));

            }

            choice = ThreadLocalRandom.current().nextDouble();
            if (choice < 0.6) {
                childrenModified.add(mutation1.mutate(children.get(1)));
            } else if (choice < 0.7) {
                childrenModified.add(mutation2.mutate(children.get(1)));
            } else if (choice < 0.8) {
                childrenModified.add(mutation3.mutate(children.get(1)));
            } else {
                childrenModified.add(children.get(1));

            }

            population.addAll(childrenModified);


            if (population.getBestChainEver().compareTo(bestChain) < 0) {
                bestChain = population.getBestChainEver();
                System.out.println(i + ": Pop. size:" + population.size() + ", Chain size: " + bestChain.size());
                iStagnation = i;
            }

            if (i % (MAX_ITER / 100) == 0) {
                System.out.println("-- " + i / (MAX_ITER / 100) + "% --");
            }

        }

        long endTime = System.currentTimeMillis();

        System.out.println();
        System.out.println("Time elapsed: " + (endTime - startTime) / 1000.0 + " s");
        Double avg = population.stream().mapToInt(Chain::size).average().orElse(0);
        System.out.println("Avg size: " + avg);
        System.out.println("StdDev  : " + Math.sqrt(
                population.stream().mapToDouble(Chain::size).map(
                        operand -> Math.pow(operand - avg, 2)
                ).average().orElse(0))
        );

        Chain result = population.getNth(0);
        System.out.println();
        //System.out.println(population);
        System.out.println();

        System.out.println("Pop. size:" + population.size() + ", Chain size: " + bestChain.size());

        System.out.println(bestChain.size() + ", " + bestChain);

    }
}
