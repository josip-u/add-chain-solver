import java.math.BigInteger;

/**
 * Created by josip on 14.05.16..
 */
public class main {
    public static void main(String[] args) {
        OperatorMutation[] mutations = {
                new OperatorMutationAddOne(),
                new OperatorMutationSplitNode(),
                new OperatorMutationSplitNode(),
                new OperatorMutationSplitNode(),
                new OperatorMutationSplitNode(),
                new OperatorMutationSplitNode(),
                new OperatorMutationSplitNode(),
                new OperatorMutationSplitNode(),
                new OperatorMutationSplitNode(),
                new OperatorMutationSplitNode()
        };
        ChainFactory chainFactory = new ChainBinaryFactory(new BigInteger("170141183460469231731687303715884105725"));
//        ChainFactory chainFactory = new ChainBinaryFactory(new BigInteger("60"));
        int populationSize = 5000;
        long avg = 0;
        Population population = new Population(populationSize);
        population.initialize(chainFactory);

        long startTime = System.currentTimeMillis();

//        population.population.parallelStream().forEach(mutation::mutate);

        int minSize;

        for (Chain chain : population.population) {
            minSize = chain.size();
            System.out.println(chain.size() + ": START");
            for (int i = 0; i < 100000; i++) {
                if (i % 100 == 0) {
                    System.out.println(i / 1000 + "%: " + chain.size());
                    System.out.flush();
                }
                mutations[i < 60000 ? i % 2 : i % 6].mutate(chain);

                if (chain.size() < minSize) {
                    minSize = chain.size();
                    System.out.println(i + ", " + chain.size() + ": " + chain);
                    System.out.flush();
                }

                avg += chain.size();
            }

            break;
            // System.out.println("pass");

        }
        long endTime = System.currentTimeMillis();

        System.out.println("Time elapsed: " + (endTime - startTime) / 100000.0);
        System.out.println("Avg size: " + (avg / (double) 100000));

    }
}
