import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Josip on 22.05.2016..
 */
public abstract class OperatorGenetic {

    protected static Exponent getRandomExponent(Chain chain) {
        Exponent current;
        do {
            current = chain.getRandom();
        } while (
                !current.hasSummandLeft() ||
                        current.getValue().equals(BigArithmetic.ONE) ||
                        current.getValue().equals(BigArithmetic.TWO)

                );
        return current;
    }

    protected void constructSubTree(Exponent current, Chain chain, Collection<Exponent> setLE, int step) {
        int counter = 0;
        Exponent summandLeft = null;

        if (current.hasSummandLeft() && current.hasSummandRight()){
            return;
        }

        for (Exponent summandLeftCandidate : setLE.stream().filter(exponent -> exponent.compareTo(current) <= 0).sorted((o1, o2) -> o2.compareTo(o1)).collect(Collectors.toList())) {
            summandLeft = summandLeftCandidate;
            if (counter == step%10) {
                break;
            }
            counter++;
        }
        if (summandLeft.equals(current)) {
            return;
        }
        BigInteger valueRight = current.getValue().subtract(summandLeft.getValue());
        Exponent summandRight = new Exponent(valueRight);
        Exponent result = chain.putIfAbsent(summandRight);
        if (result != null) {
            summandRight = result;
        } else {
            constructSubTree(summandRight, chain, chain.getExponents(), step+10);
        }

        current.setSummands(summandLeft, summandRight);

    }

    public static void removeOrphans(Chain chainNew) {
        chainNew.getExponents().stream()
                .filter(entry -> !entry.equals(chainNew.getExponent()) && entry.getParentsSize() == 0)
                .collect(Collectors.toList())
                .forEach(entry -> {
                    if (entry.hasSummandLeft())
                        entry.getSummandLeft().removeParent(entry.getValue());
                    if (entry.hasSummandRight())
                        entry.getSummandRight().removeParent(entry.getValue());
                    chainNew.remove(entry.getValue());
                });
    }

}
