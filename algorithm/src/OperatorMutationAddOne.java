import java.math.BigInteger;
import java.util.*;

/**
 * Created by Josip on 21.05.2016..
 */
public class OperatorMutationAddOne extends OperatorMutation {

    @Override
    public Chain mutate(Chain chain) {
        Chain chainNew = new Chain(chain);
        Exponent choice;

        BigInteger newLeftValue;

        do {
            choice = getRandomExponent(chainNew);
            newLeftValue = choice.getSummandLeft().getValue().add(BigInteger.ONE);
        } while (choice.getValue().equals(newLeftValue));

        Exponent newSummandLeft = new Exponent(newLeftValue);
        Exponent newSummandRight = new Exponent(choice.getValue().subtract(newSummandLeft.getValue()));


        Exponent result = chainNew.putIfAbsent(newSummandLeft);
        if (result != null) {
            newSummandLeft = result;
            choice.getSummandLeft().removeParent(choice);
        } else{
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
