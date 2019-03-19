package richard.test.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by richard on 4/8/18.
 */
public class Parent {
    private static final Logger LOGGER = LoggerFactory.getLogger(Parent.class);

    public void sayHello() throws InterruptedException {
        synchronized (this) {
            LOGGER.info("parent say hello");

            Thread.sleep(1000L);
        }
    }

    public static synchronized void say() throws InterruptedException {
        LOGGER.info("I am parent");
        Thread.sleep(1000L);
    }
}
