import java.math.BigInteger;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Josip on 21.05.2016..
 */
public class OperatorMutationAddOne extends OperatorMutation {

    @Override
    public Chain mutate(Chain chain) {
        Chain chainNew = new Chain(chain.getExponent().getValue());
        Exponent choice;
        BigInteger newLeftValue;
        Exponent exponentNew = null;

        do {
            choice = getRandomExponent(chain);
            newLeftValue = choice.getSummandLeft().getValue().add(BigInteger.ONE);
        } while(choice.getValue().equals(newLeftValue));

        Boolean start = true;
        for (Map.Entry<BigInteger, Exponent> entry : chain.getExponentsDesc().entrySet()) {
            Exponent currentOld;
            Exponent currentNew;
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
                currentOld = entry.getValue();
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


            if (!chainNew.add(currentNew)) {
                currentNew = chainNew.get(value);
            }

            if (leftValue != null && rightValue != null) {
                newSummandLeft = new Exponent(leftValue);
                newSummandRight = new Exponent(rightValue);

                if (!chainNew.add(newSummandLeft)) {
                    newSummandLeft = chainNew.get(leftValue);
                }

                if (!chainNew.add(newSummandRight)) {
                    newSummandRight = chainNew.get(rightValue);
                } else if (choice.getValue().equals(value)) {
                    exponentNew = newSummandRight;
                }

                currentNew.setSummands(newSummandLeft, newSummandRight);
            }


        }

        if (exponentNew != null) {
            constructSubTree(exponentNew, chainNew, chainNew.getExponentsDescLE(exponentNew));
            removeOrphans(chainNew);
        }

        return chainNew;
    }

}
