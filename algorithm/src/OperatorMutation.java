import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Josip on 17.05.2016..
 */
public abstract class OperatorMutation extends OperatorGenetic {
    abstract Chain mutate(Chain chain);
}
