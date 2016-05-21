import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Josip on 17.05.2016..
 */
public class OperatorMutationSplitNode implements OperatorMutation {

    @Override
    public void mutate(Chain chain) {
        Exponent current;
        long choice;
        do {
            choice = ThreadLocalRandom.current().nextLong(0, chain.size());
            current = chain.getNth(choice);
        } while (
                !current.hasSummandLeft() ||
                        current.getValue().equals(BigArithmetic.ONE) ||
                        current.getValue().equals(BigArithmetic.TWO)

                );

        Set<Exponent> orphanCandidates = constructRandomSummands(current, chain);
        orphanCandidates.forEach(exponent -> cropBlindSubChain(exponent, chain));

    }

    private void cropBlindSubChain(Exponent exponent, Chain chain) {

        if (
                exponent.getParents().size() == 0 &&
                        !exponent.getValue().equals(BigArithmetic.TWO) &&
                        !exponent.getValue().equals(BigArithmetic.ONE)
                ) {

            chain.remove(exponent.getValue());
            if (exponent.hasSummandLeft()) {
                exponent.getSummandLeft().removeParent(exponent);
                cropBlindSubChain(exponent.getSummandLeft(), chain);
            }
            if (exponent.hasSummandRight()) {
                exponent.getSummandRight().removeParent(exponent);
                cropBlindSubChain(exponent.getSummandRight(), chain);
            }
        }
    }

    private Set<Exponent> constructRandomSummands(Exponent current, Chain chain) {
        Set<Exponent> result = new HashSet<>();
        if (current.getValue().equals(BigArithmetic.TWO) || current.getValue().equals(BigArithmetic.ONE)) {
            return result;
        }

        BigInteger value = current.getValue();

        Exponent nextCandidate = current;

        int counter = 0;
        for (Exponent exponent : chain.getExponentsDescLE(current).values()) {
            if (exponent.compareTo(current) < 0) {
                counter++;
            }
            nextCandidate = exponent;
            if (counter == 2) {

                break;
            }
        }

        if (nextCandidate.equals(current)) {
            return result;
        }

        Exponent newRight = nextCandidate;
        BigInteger newValueLeft = value.subtract(newRight.getValue());

        Exponent newLeft = chain.get(newValueLeft);

        if (newLeft == null) {
            newLeft = new Exponent(newValueLeft);
            chain.add(newLeft);
            result.addAll(constructRandomSummands(newLeft, chain));
        } else if (newLeft.equals(current.getSummandLeft())) {
            return result;
        }

        Exponent oldSummandLeft = null;
        if (current.hasSummandLeft()) {
            oldSummandLeft = current.getSummandLeft();
            oldSummandLeft.removeParent(current);

        }
        Exponent oldSummandRight = null;
        if (current.hasSummandRight()) {
            oldSummandRight = current.getSummandRight();
            oldSummandRight.removeParent(current);
        }

        current.setSummands(newLeft, newRight);

        if (oldSummandLeft != null) {
            result.add(oldSummandLeft);
        }

        if (oldSummandRight != null) {
            result.add(oldSummandRight);
        }
        return result;
    }

}
