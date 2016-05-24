import java.util.Collection;

/**
 * Created by Josip on 22.05.2016..
 */
public abstract class OperatorCrossover extends OperatorGenetic {
    abstract Collection<Chain> crossover(Chain chain1, Chain chain2);
}
