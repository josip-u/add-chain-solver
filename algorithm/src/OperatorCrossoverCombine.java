import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Josip on 23.05.2016..
 */
public class OperatorCrossoverCombine extends OperatorCrossover {
    @Override
    Collection<Chain> crossover(Chain chain1, Chain chain2) {
        Exponent point1;
        Exponent point2;

        do {
            point1 = getRandomExponent(chain1);
            point2 = chain2.get(point1.getValue());
        } while (point2 == null || point2.equals(chain2.getExponent()));

        final BigInteger point = point1.getValue();

        List<Chain> chainsOld = new ArrayList<>();
        chainsOld.add(chain1);
        chainsOld.add(chain2);

        List<Chain> chainsNew = new ArrayList<>();
        chainsNew.add(new Chain(chain1.getExponent().getValue()));
        chainsNew.add(new Chain(chain1.getExponent().getValue()));

        for (int i = 0; i < 2; i++) {
            Chain chainOld = chainsOld.get(i);
            Chain chainNew = chainsNew.get(i);

            Boolean start = true;
            for (Map.Entry<BigInteger, Exponent> entry : chainOld.getExponentsDescGT(point1).entrySet()) {
                Exponent currentOld;
                Exponent currentNew;
                if (start) {
                    currentOld = chainOld.getExponent();
                    currentNew = chainNew.getExponent();
                    start = false;
                } else {
                    currentOld = entry.getValue();
                    currentNew = new Exponent(currentOld.getValue());
                }

                BigInteger value = currentNew.getValue();
                BigInteger leftValue = currentOld.getSummandLeft().getValue();
                BigInteger rightValue = currentOld.getSummandRight().getValue();


                Exponent newSummandLeft = new Exponent(leftValue);
                Exponent newSummandRight = new Exponent(rightValue);

                if (!chainNew.add(newSummandLeft)) {
                    newSummandLeft = chainNew.get(leftValue);
                }

                if (!chainNew.add(newSummandRight)) {
                    newSummandRight = chainNew.get(rightValue);
                }

                if (!chainNew.add(currentNew)) {
                    currentNew = chainNew.get(value);
                }

                currentNew.setSummands(newSummandLeft, newSummandRight);
            }

        }

        for (int i = 0; i < 2; i++) {
            Chain chainOld = chainsOld.get(i);
            Chain chainNew = chainsNew.get(1 - i);

            for (Map.Entry<BigInteger, Exponent> entry : chainOld.getExponentsDescLE(point1).entrySet()) {
                Exponent currentOld;
                Exponent currentNew;

                Exponent newSummandLeft = null;
                Exponent newSummandRight = null;

                currentOld = entry.getValue();
                currentNew = new Exponent(currentOld.getValue());

                BigInteger value = currentNew.getValue();

                if (currentOld.hasSummandLeft()) {
                    BigInteger leftValue = currentOld.getSummandLeft().getValue();
                    newSummandLeft = new Exponent(leftValue);

                    if (!chainNew.add(newSummandLeft)) {
                        newSummandLeft = chainNew.get(leftValue);
                    }
                }

                if (currentOld.hasSummandRight()) {
                    BigInteger rightValue = currentOld.getSummandRight().getValue();
                    newSummandRight = new Exponent(rightValue);

                    if (!chainNew.add(newSummandRight)) {
                        newSummandRight = chainNew.get(rightValue);
                    }
                }

                if (!chainNew.add(currentNew)) {
                    currentNew = chainNew.get(value);
                }

                if (newSummandLeft != null && newSummandRight != null) {
                    currentNew.setSummands(newSummandLeft, newSummandRight);
                }


            }

            removeOrphans(chainNew);
        }

        return chainsNew;
    }

}
