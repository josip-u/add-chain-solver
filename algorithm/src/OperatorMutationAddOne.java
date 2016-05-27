import java.math.BigInteger;
import java.util.*;

/**
 * Created by Josip on 21.05.2016..
 */
public class OperatorMutationAddOne extends OperatorMutation {

    @Override
    public Chain mutate(Chain chain) {
        Chain chainNew = new Chain(chain.getExponent().getValue());
        Exponent choice;
        BigInteger newLeftValue;
        Exponent exponentNewRight = null;
        Exponent exponentNewLeft = null;

        do {
            choice = getRandomExponent(chain);
            newLeftValue = choice.getSummandLeft().getValue().add(BigInteger.ONE);
        } while (choice.getValue().equals(newLeftValue));

        Boolean start = true;
        List<Exponent> list = new ArrayList<>();
        list.addAll(chain.getExponents());
        list.sort((o1, o2) -> o2.compareTo(o1));
        for (Exponent entry : list) {
            Exponent currentOld;
            Exponent currentNew;
            BigInteger leftValue = null;
            BigInteger rightValue = null;
            Exponent oldSummandLeft;
            Exponent newSummandLeft;
            Exponent newSummandRight;
            Exponent result;

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
                leftValue = newLeftValue;
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
                } else if (choice.getValue().equals(value)) {
                    exponentNewLeft = newSummandLeft;
                }

                result = chainNew.putIfAbsent(newSummandRight);
                if (result != null) {
                    newSummandRight = result;
                } else if (choice.getValue().equals(value)) {
                    exponentNewRight = newSummandRight;
                }

                currentNew.setSummands(newSummandLeft, newSummandRight);
            }


        }

        if (exponentNewLeft != null) {
            constructSubTree(exponentNewLeft, chainNew, chainNew.getExponents());
        }

        if (exponentNewRight != null) {
            constructSubTree(exponentNewRight, chainNew, chainNew.getExponents());
        }

        if (exponentNewLeft != null || exponentNewRight != null){
            removeOrphans(chainNew);
        }

        return chainNew;
    }

}
