import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Created by josip on 17.12.16..
 */
public class ChainWindowFactory implements ChainFactory {

    private final BigInteger value;
    private final String pyCommand;
    private final Map<Integer, Chain> chainModel = new HashMap<>();
    private final int windowSize;

    private static final int LOWER_BOUND = 3;
    private static final int UPPER_BOUND = 8;

    public ChainWindowFactory(BigInteger value, int windowSize, String pyCommand) {
        this.value = value;
        this.windowSize = windowSize;
        this.pyCommand = pyCommand;
        if (windowSize == -1) {
            for (int i = LOWER_BOUND; i < UPPER_BOUND; i++) {
                this.chainModel.put(i, generatePrivate(i));
            }
        } else {
            this.chainModel.put(windowSize, generatePrivate(windowSize));
        }
    }

    public static void constructConnections(Chain chain, List<Exponent> sortedExponents) {
        if (sortedExponents == null) {
            sortedExponents = chain.getExponents().stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        }
        Exponent exponent = sortedExponents.get(0);
        sortedExponents.remove(0);

        if (sortedExponents.size() == 0) {
            return;
        }

        Exponent nextLeft = null;
        Exponent nextRight = null;

        for (int i = 0; i < sortedExponents.size(); i++) {
            nextLeft = sortedExponents.get(0);
            nextRight = chain.getOrNull(exponent.getValue().subtract(nextLeft.getValue()));
            if (nextRight != null) {
                break;
            }
        }
        exponent.setSummands(nextLeft, nextRight);
        constructConnections(chain, sortedExponents);
    }


    public Chain generatePrivate(int windowSize) {
        TreeSet<BigInteger> allValues = new TreeSet<>(Comparator.reverseOrder());
        Chain chain = new Chain(value);
        Exponent current = chain.getExponent();

        String s;
        try {
            Process p = Runtime.getRuntime().exec(pyCommand + " -w " + windowSize + " -eo " + this.value);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));

            // read the output from the command
            while ((s = stdInput.readLine()) != null) {
                chain.putIfAbsent(new Exponent(new BigInteger(s)));
            }

            while ((s = stdError.readLine()) != null) ;

        } catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
        }

        constructConnections(chain, null);
        OperatorGenetic.removeOrphans(chain);
        return chain;

    }

    public static void main(String[] args) {
        BigInteger value = new BigInteger("123456789");
        ChainFactory factory = new ChainWindowFactory(value, 4, "python3 /home/josip/PycharmProjects/lab/chain-solver/chains/factory.py -reo");
        Chain chain = factory.generate();
        System.out.println(chain);

    }

    @Override
    public Chain generate() {
        int w = this.windowSize;
        if (w == -1) {
            w = ThreadLocalRandom.current().nextInt(LOWER_BOUND, UPPER_BOUND);
        }
        return new Chain(chainModel.get(w));
    }
}
