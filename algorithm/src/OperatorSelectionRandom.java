import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Josip on 24.05.2016..
 */
public class OperatorSelectionRandom extends OperatorSelection {
    @Override
    public Chain select(Population population) {
        return population.getRandom();
    }
}
