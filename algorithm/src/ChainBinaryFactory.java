import java.math.BigInteger;

/**
 * Created by Josip on 17.05.2016..
 */
public class ChainBinaryFactory implements ChainFactory {

    private final BigInteger value;

    public ChainBinaryFactory(BigInteger value) {
        this.value = value;
    }

    @Override
    public Chain generate() {
        BigInteger valueLeft = value;
        BigInteger valueRight;
        Chain chain = new Chain(valueLeft);
        Exponent current = chain.getExponent();
        Exponent nextLeft;
        Exponent nextRight;
        while (valueLeft.compareTo(BigArithmetic.ONE) > 0) {
            if (valueLeft.mod(BigArithmetic.TWO).equals(BigArithmetic.ONE)) {
                valueLeft = valueLeft.subtract(BigInteger.ONE);
                valueRight = BigInteger.ONE;
                nextLeft = chain.get(valueLeft);
                nextRight = chain.get(valueRight);
            } else {
                valueLeft = valueLeft.divide(BigArithmetic.TWO);
                valueRight = valueLeft;
                nextLeft = chain.get(valueLeft);
                nextRight = nextLeft;
            }

            if (nextLeft == null) {
                nextLeft = new Exponent(valueLeft);
                chain.add(nextLeft);
            }
            if (valueLeft.equals(valueRight)) {
                nextRight = nextLeft;
            } else if (nextRight == null) {
                nextRight = new Exponent(valueRight);
                chain.add(nextRight);
            }

            current.setSummands(nextLeft, nextRight);
            current = nextLeft;

        }
        return chain;
    }
}
