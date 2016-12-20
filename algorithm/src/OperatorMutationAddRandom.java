import java.math.BigInteger;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by josip on 17.12.16..
 */
public class OperatorMutationAddRandom extends OperatorMutation {
    private static BigInteger TWO = new BigInteger("2");
    private static BigInteger MAX_LONG = new BigInteger(String.valueOf(Long.MAX_VALUE));

    @Override
    public Chain mutate(Chain chain) {
        Chain chainNew = new Chain(chain);
        Exponent choice;

        BigInteger newLeftValue;

        do {
            do {
                choice = getRandomExponent(chainNew);
            } while (choice.getValue().equals(BigInteger.ONE) || choice.getValue().equals(TWO));
            newLeftValue = choice.getSummandLeft().getValue().add(
                    new BigInteger(
                            String.valueOf(ThreadLocalRandom.current().nextLong(
                                    Math.abs(choice.getSummandRight().getValue().longValue())
                            ))
                    )
            );
        } while (choice.getValue().compareTo(newLeftValue) <= 0);

        Exponent newSummandLeft = new Exponent(newLeftValue);
        Exponent newSummandRight = new Exponent(choice.getValue().subtract(newSummandLeft.getValue()));


        Exponent result = chainNew.putIfAbsent(newSummandLeft);
        if (result != null) {
            newSummandLeft = result;
            choice.getSummandLeft().removeParent(choice);
        } else {
            constructSubTree(newSummandLeft, chainNew, chainNew.getExponents(), 1);
        }

        result = chainNew.putIfAbsent(newSummandRight);
        if (result != null) {
            newSummandRight = result;
        } else {
            constructSubTree(newSummandRight, chainNew, chainNew.getExponents(), 1);
        }


        choice.getSummandLeft().removeParent(choice);
        choice.getSummandRight().removeParent(choice);

        choice.setSummands(newSummandLeft, newSummandRight);


        removeOrphans(chainNew);

        return chainNew;
    }

}
