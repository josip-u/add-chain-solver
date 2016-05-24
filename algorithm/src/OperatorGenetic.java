import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Created by Josip on 22.05.2016..
 */
public abstract class OperatorGenetic {

    protected static Exponent getRandomExponent(Chain chain) {
        long choice;
        Exponent current;
        do {
            choice = ThreadLocalRandom.current().nextLong(0, chain.size());
            current = chain.getNth(choice);
        } while (
                !current.hasSummandLeft() ||
                        current.getValue().equals(BigArithmetic.ONE) ||
                        current.getValue().equals(BigArithmetic.TWO)

                );
        return current;
    }

    protected void cropBlindSubChain(Exponent exponent, Chain chain) {

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

    protected void constructSubTree(Exponent current, Chain chain, SortedMap<BigInteger, Exponent> setLE) {
        int counter = 0;
        Exponent summandLeft = null;

        if (current.hasSummandLeft() && current.hasSummandRight()){
            return;
        }

        for (Exponent summandLeftCandidate : setLE.values()) {
            summandLeft = summandLeftCandidate;
            if (counter == 2) {
                break;
            }
            counter++;
        }
        if (summandLeft.equals(current)) {
            return;
        }
        BigInteger valueRight = current.getValue().subtract(summandLeft.getValue());
        Exponent summandRight = new Exponent(valueRight);

        if (!chain.add(summandRight)) {
            summandRight = chain.get(valueRight);
        } else {
            constructSubTree(summandRight, chain, setLE.tailMap(summandRight.getValue()));
        }

        current.setSummands(summandLeft, summandRight);

    }

    protected static void removeOrphans(Chain chainNew) {
        chainNew.getExponentsDesc().entrySet().stream()
                .skip(1)
                .filter(entry -> entry.getValue().getParents().size() == 0)
                .collect(Collectors.toList())
                .forEach(entry -> {
                    entry.getValue().getSummandLeft().removeParent(entry.getValue());
                    entry.getValue().getSummandRight().removeParent(entry.getValue());
                    chainNew.remove(entry.getKey());
                });
    }

    protected Set<Exponent> constructSummands(Exponent current, Chain chain) {
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
            result.addAll(constructSummands(newLeft, chain));
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
