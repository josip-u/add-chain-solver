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
        Exponent point;

        Chain chainOld;
        Chain chainNew;

        Exponent result;
        Exponent currentNew;
        Exponent newSummandLeft;
        Exponent newSummandRight;

        int counter = 0;
        int counterMax = Integer.min(chain1.size(), chain2.size());
        do {
            point = getRandomExponent(chain1);
            counter++;
        } while (counter < counterMax && (point.equals(chain2.getExponent()) || chain2.get(point.getValue()) == null));

        List<Chain> chainsOld = new ArrayList<>();
        chainsOld.add(chain1);
        chainsOld.add(chain2);

        List<Chain> chainsNew = new ArrayList<>();
        chainsNew.add(new Chain(chain1.getExponent().getValue()));
        chainsNew.add(new Chain(chain1.getExponent().getValue()));

        List<Map<BigInteger, Exponent>> maps = new ArrayList<>();
        maps.add(new HashMap<>());
        maps.add(new HashMap<>());

        Exponent pointFinal = point;

        for (int i = 0; i < 2; i++) {
            maps.get(i).putAll(chainsOld.get(i).getExponentsMap());
            maps.get(i).putAll(
                    chainsOld.get(1 - i).getExponentsMap().values().stream()
                            .filter(entry -> entry.compareTo(pointFinal) <= 0)
                            .collect(Collectors.toMap(Exponent::getValue, exponent -> exponent))
            );
        }

        for (int i = 0; i < 2; i++) {

            for (Exponent currentOld : maps.get(0).values()) {
                if (currentOld.compareTo(point) > 0) {
                    chainNew = chainsNew.get(i);
                } else {
                    chainNew = chainsNew.get(1 - i);
                }

                currentNew = new Exponent(currentOld.getValue());
                result = chainNew.putIfAbsent(currentNew);

                if (result != null) {
                    currentNew = result;
                }


                if (currentOld.hasSummandLeft()) {
                    newSummandLeft = new Exponent(currentOld.getSummandLeft().getValue());

                    result = chainNew.putIfAbsent(newSummandLeft);
                    if (result != null) {
                        newSummandLeft = result;
                    }
                } else {
                    continue;
                }


                newSummandRight = new Exponent(currentOld.getSummandRight().getValue());

                result = chainNew.putIfAbsent(newSummandRight);
                if (result != null) {
                    newSummandRight = result;
                }

                currentNew.setSummands(newSummandLeft, newSummandRight);

            }
        }

        chainsNew.forEach(OperatorGenetic::removeOrphans);

        return chainsNew;
    }

}
