package richard.test.concurrent;

import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InvalidObjectException;
import java.io.ObjectInputValidation;
import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * Created by richard on 4/8/18.
 */
public class Child extends Parent implements Serializable, ObjectInputValidation {

    private static final Logger LOGGER = LoggerFactory.getLogger(Child.class);

    @Override
    public void sayHello() throws InterruptedException {
        synchronized (this) {
            LOGGER.info(" child say hello");
            Thread.sleep(1000L);
        }
    }

    public static void main(String[] args) {
        Child child = new Child();
        Class c = child.getClass();
        Type[] t = c.getGenericInterfaces();
        if (child instanceof  Parent) {
            LOGGER.info("it is a parent.");
        } else {
            LOGGER.info("unkonwn.");
        }
        Parent parent = new Parent();
        if (parent instanceof Parent) {
            LOGGER.info("parent.");
        }
        if (parent instanceof Child) {
            LOGGER.info("child.");
        }
        Parent parent1 = new Child();
        if (parent1 instanceof  Child) {
            LOGGER.info("child");
        }
        if (parent1 instanceof Parent) {
            LOGGER.info("parent.");
        }
    }

    public void validateObject() throws InvalidObjectException {

    }
}
