import java.math.BigInteger;

/**
 * Created by Josip on 23.05.2016..
 */
public class mainMutation {
    public static void main(String[] args) {
        BigInteger value = BigInteger.valueOf(30);

//        ChainFactory factory = new ChainBinaryFactory(new BigInteger("170141183460469231731687303715884105725"));
        ChainFactory factory = new ChainBinaryFactory(value);
        OperatorMutation mutation1 = new OperatorMutationAddOne();
        Chain chain = factory.generate();
        Chain mutated = mutation1.mutate(chain);

        System.out.println(chain);
        System.out.println(mutated);
    }
}
