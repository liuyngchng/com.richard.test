package richard.test.design.patten.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * Created by Richard on 1/9/18.
 */
public class ConcreteObserver implements Observer {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(ConcreteObserver.class);

    private String id = UUID.randomUUID().toString();

    private float temperature;

    private final Subject subject;

    public ConcreteObserver(final Subject subject) {
        this.subject = subject;
        this.subject.registerObserver(this);
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(final float temperature) {
        this.temperature = temperature;
    }

    public String getId() {
        return this.id;
    }

    //    @Override
    public void update(final float temperature) {
        LOGGER.info("observed temperature updated to {}", temperature);
        this.temperature = temperature;
    }

    public void test() {
        Type gc = this.getClass().getGenericSuperclass();
        String s = gc.toString();
        s = s.substring(s.indexOf("<") + 1, s.length() - 1);
        String[] clzs = s.split(",");
        System.out.println(clzs);
    }

    public static void main(String[] args) {
        ConcreteObserver observer = new ConcreteObserver(new ConcreteSubject());
        observer.test();
    }
}
