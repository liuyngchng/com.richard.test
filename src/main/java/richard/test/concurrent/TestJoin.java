package richard.test.concurrent;


import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;


/**
 * Created by richard on 4/15/18.
 */
public class TestJoin {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(TestJoin.class);

    public synchronized void idle() throws InterruptedException {
        LOGGER.info("start idle");
//        this.wait();
        Thread.sleep(5000L);
        LOGGER.info("end idle");
    }

    public static void main(String[] args) throws Exception {
//        TestJoin testJoin = new TestJoin();
//        testJoin.idle();
//        Collection<Child> cs = new ArrayList<Child>();
//        Type[] types = cs.getClass().getGenericInterfaces();
//        for (int i = 0; i < types.length ; i++) {
//            Type t = types[i];
//            if (t instanceof ParameterizedType) {
//                ( (ParameterizedType) t).getRawType();
//            }
//        }
//
//        Thread.currentThread().wait();
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("1", "test");
//        map.put("1", "testMe");
//        map.put("1", "testMeAgain.");
//        System.out.println(map.get("1"));
//        System.out.println(1<<30);
//        int a= 10;
        final TestJoin testJoin = new TestJoin();
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    testJoin.idle();
                } catch (InterruptedException e) {
                    LOGGER.error("error", e);
                }
            }
        });
        thread.start();
//        LOGGER.info("I am waiting.");
//        Thread.sleep(5000L);
//        thread.interrupt();
//        thread.join();
//
        LOGGER.info("I am OK.");
    }
}
