import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Josip on 23.05.2016..
 */
public class OperatorSelectionProportional extends OperatorSelection {

    @Override
    public Chain select(Population population) {
        int choice = (int)Math.floor(Math.abs(ThreadLocalRandom.current().nextGaussian() * population.size()/3));
        if (choice >= population.size() ){
            choice = population.size() - 1;
        }
        return population.getNth(choice);
    }
}
