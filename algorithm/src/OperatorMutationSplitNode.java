import java.math.BigInteger;
import java.util.*;

/**
 * Created by Josip on 17.05.2016..
 */
public class OperatorMutationSplitNode extends OperatorMutation {

    @Override
    public Chain mutate(Chain chain) {
        Chain chainNew = new Chain(chain);
        Exponent choice = getRandomExponent(chainNew);
        Exponent newSummandLeft = choice.getSummandLeft().getSummandLeft();
        Exponent newSummandRight = new Exponent(choice.getValue().subtract(newSummandLeft.getValue()));

        Exponent result = chainNew.putIfAbsent(newSummandRight);
        if (result != null) {
            newSummandRight = result;
        }

        choice.getSummandLeft().removeParent(choice);
        choice.getSummandRight().removeParent(choice);

        choice.setSummands(newSummandLeft, newSummandRight);
        constructSubTree(newSummandRight, chainNew, chainNew.getExponents(), 2);


            removeOrphans(chainNew);

        return chainNew;
    }


}
