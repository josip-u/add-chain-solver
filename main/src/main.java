/**
 * Created by josip on 14.05.16..
 */
public class main {
    public static void main(String[] args) {
        Chain chain = new Chain(20);
        Exponent exponent1 = new Exponent(10);
        Exponent exponent2 = new Exponent(2);
        Exponent exponent3 = new Exponent(8);
        Exponent exponent4 = new Exponent(1);
        Exponent exponent5 = new Exponent(4);

        chain.first().setSummand(exponent1);

        exponent1.setSummand(exponent3);
        chain.add(exponent1);

        exponent2.setSummand(exponent4);
        chain.add(exponent2);

        exponent3.setSummand(exponent5);
        chain.add(exponent3);

        chain.add(exponent4);

        exponent5.setSummand(exponent2);
        chain.add(exponent5);


        System.out.println(chain);

    }
}
