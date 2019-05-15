package richard.test.design.patten.observer;

/**
 * Created by Richard on 1/9/18.
 */
public class Client
{
    public static void main(final String[] args) {
        final ConcreteSubject sb = new ConcreteSubject();
        sb.setTemperature((float) 20.00);

        final Observer o = new ConcreteObserver(sb);
        sb.setTemperature((float) 30.00);
    }
}
