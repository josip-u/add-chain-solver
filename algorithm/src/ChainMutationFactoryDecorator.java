import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Josip on 24.05.2016..
 */
public class ChainMutationFactoryDecorator implements ChainFactory {
    private final ChainFactory factory;
    private final OperatorMutation mutation;

    public ChainMutationFactoryDecorator(ChainFactory factory, OperatorMutation mutation){
        this.factory = factory;
        this.mutation = mutation;
    }

    @Override
    public Chain generate() {
        if (ThreadLocalRandom.current().nextBoolean()){
            return mutation.mutate(factory.generate());
        }else{
            return factory.generate();
        }
    }
}
