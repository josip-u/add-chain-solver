import java.math.BigInteger;
import java.util.*;

/**
 * Created by Josip on 17.05.2016..
 */
public class OperatorMutationSplitNode extends OperatorMutation {

    @Override
    public Chain mutate(Chain chain) {
        Chain chainNew = new Chain(chain.getExponent().getValue());
        Exponent choice = getRandomExponent(chain);
        Exponent exponentNew = null;

        Boolean start = true;
        List<Exponent> list = new ArrayList<>();
        list.addAll(chain.getExponents());
        list.sort((o1, o2) -> o2.compareTo(o1));
        for (Exponent entry : list) {
            Exponent currentOld;
            Exponent currentNew;
            Exponent result;
            BigInteger leftValue = null;
            BigInteger rightValue = null;
            Exponent oldSummandLeft;
            Exponent newSummandLeft;
            Exponent newSummandRight;

            if (start) {
                currentOld = chain.getExponent();
                currentNew = chainNew.getExponent();
                start = false;
            } else {
                currentOld = entry;
                currentNew = new Exponent(currentOld.getValue());
            }

            BigInteger value = currentNew.getValue();

            if (choice.getValue().equals(value)) {
                oldSummandLeft = currentOld.getSummandLeft();
                if (oldSummandLeft.hasSummandLeft()) {
                    oldSummandLeft = oldSummandLeft.getSummandLeft();
                }
                leftValue = oldSummandLeft.getValue();
                rightValue = value.subtract(leftValue);

            } else if (!value.equals(BigArithmetic.ONE)) {
                leftValue = currentOld.getSummandLeft().getValue();
                rightValue = currentOld.getSummandRight().getValue();
            }

            result = chainNew.putIfAbsent(currentNew);
            if (result != null) {
                currentNew = result;
            }

            if (leftValue != null && rightValue != null) {
                newSummandLeft = new Exponent(leftValue);
                newSummandRight = new Exponent(rightValue);

                result = chainNew.putIfAbsent(newSummandLeft);
                if (result != null) {
                    newSummandLeft = result;
                }

                result = chainNew.putIfAbsent(newSummandRight);
                if (result != null) {
                    newSummandRight = result;
                } else if (choice.getValue().equals(value)) {
                    exponentNew = newSummandRight;
                }

                currentNew.setSummands(newSummandLeft, newSummandRight);
            }


        }

        if (exponentNew != null) {
            constructSubTree(exponentNew, chainNew, chainNew.getExponents());
            removeOrphans(chainNew);
        }

        return chainNew;
    }


}
