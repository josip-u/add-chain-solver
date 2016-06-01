import com.sun.org.apache.bcel.internal.generic.BIPUSH;

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
        Chain chain1New = new Chain(chain1);
        Chain chain2New = new Chain(chain2);


        List<Chain> chainsNew = new ArrayList<>();
        chainsNew.add(chain1New);
        chainsNew.add(chain2New);

        int counter = 0;
        int counterMax = Integer.min(chain1New.size(), chain2New.size());
        do {
            point1 = getRandomExponent(chain1New);
            point2 = chain2New.get(point1.getValue());
            counter++;
        } while (counter < counterMax && (point1.equals(chain1New.getExponent()) || point2 == null));


        for (int i = 0; i < 2; i++) {

            Chain chainFrom = chainsNew.get(i);
            Chain chainTo = chainsNew.get(1 - i);
            for (Exponent current1 : chainFrom.getExponents()) {
                if (current1.compareTo(point1) > 0) {
                    continue;
                }

                Exponent summandLeft2 = null;
                Exponent summandRight2 = null;

                Exponent current2 = new Exponent(current1.getValue());
                Exponent result = chainTo.putIfAbsent(current2);
                if (result != null) {
                    current2 = result;
                }

                if (current1.hasSummandLeft()) {
                    Exponent summandLeft1 = current1.getSummandLeft();
                    summandLeft2 = new Exponent(summandLeft1.getValue());
                    result = chainTo.putIfAbsent(summandLeft2);
                    if (result != null) {
                        summandLeft2 = result;
                    }

                }

                if (current1.hasSummandRight()) {
                    Exponent summandRight1 = current1.getSummandRight();
                    summandRight2 = new Exponent(summandRight1.getValue());
                    result = chainTo.putIfAbsent(summandRight2);
                    if (result != null) {
                        summandRight2 = result;
                    }

                }

                if (summandLeft2 != null) {
                    if (current2.hasSummandLeft()) {
                        current2.getSummandLeft().removeParent(current2);
                        current2.getSummandRight().removeParent(current2);
                    }
                    current2.setSummands(summandLeft2, summandRight2);
                }

            }

        }


        chainsNew.forEach(OperatorGenetic::removeOrphans);

        return chainsNew;
    }

}
