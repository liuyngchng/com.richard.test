package richard.test.concurrent;

import org.omg.CORBA.*;
import org.omg.CORBA.Object;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by richard on 4/8/18.
 */
public class Work {

    private static final Logger LOGGER = LoggerFactory.getLogger(Work.class);

    public static void main(String[] args) throws InterruptedException {
        int m = 1;
        int n = m  << 4;
        final Parent parent = new Parent();
        parent.getClass();
        final Child child = new Child();
        if (parent instanceof Parent) {
            LOGGER.info("a parent instance");
        }

        for (int i = 0 ; i < 5; i++) {
            Thread a = new Thread(
                new Runnable() {
                    public void run() {
                        try {
                            parent.sayHello();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            );
            a.start();
            Thread a1 = new Thread(
                new Runnable() {
                    public void run() {
                        try {
                            Parent.say();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            );
            a1.start();
//            System.out.println("new parent job");
        }
//        System.out.println("parent start work");
//        for (int i = 0 ; i < 5; i++) {
//            Thread b = new Thread(
//                new Runnable() {
//                    public void run() {
//                        try {
//                            child.sayHello();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            );
//            b.start();
////            System.out.println("new child job");
//        }
//        System.out.println("child start work");
    }
}
