import java.math.BigInteger;

/**
 * Created by josip on 14.05.16..
 */
public class main {
    public static void main(String[] args) {
        OperatorMutation mutation = new OperatorMutationSplitNode();
//        ChainFactory chainFactory = new ChainBinaryFactory(new BigInteger("170141183460469231731687303715884105725"));
        ChainFactory chainFactory = new ChainBinaryFactory(new BigInteger("60"));
        int populationSize = 5000;
        long avg = 0;
        Population population = new Population(populationSize);
        population.initialize(chainFactory);

        long startTime = System.currentTimeMillis();

//        population.population.parallelStream().forEach(mutation::mutate);

        for (Chain chain : population.population) {
            System.out.println(chain.size() + ": " + chain);
            for (int i = 0; i < 1000; i++) {
                mutation.mutate(chain);
                System.out.println(chain.size() + ": " + chain);
                avg += chain.size();
            }

            break;
            // System.out.println("pass");

        }
        long endTime = System.currentTimeMillis();

        System.out.println("Time elapsed: " + (endTime - startTime) / 1000.0);
        System.out.println("Avg size: " + (avg / (double)100));

    }
}
