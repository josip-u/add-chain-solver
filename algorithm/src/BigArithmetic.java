import java.math.BigInteger;
import java.util.Random;

/**
 * Created by Josip on 17.05.2016..
 */
public class BigArithmetic {
    public static BigInteger ZERO = BigInteger.valueOf(0);
    public static BigInteger ONE = BigInteger.valueOf(1);
    public static BigInteger TWO = BigInteger.valueOf(2);

    public static BigInteger nextInteger(BigInteger max) {
        Random rnd = new Random();
        BigInteger choice = new BigInteger(max.bitLength(), rnd);
        return (
                choice.compareTo(max) < 0 ?
                        choice.compareTo(BigArithmetic.ZERO) == 0 ?
                                BigArithmetic.ONE :
                                choice :
                        max.subtract(BigArithmetic.ONE)
        );
    }
}
