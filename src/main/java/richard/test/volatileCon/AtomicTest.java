package richard.test.volatileCon;

import java.util.concurrent.CountDownLatch;

public class AtomicTest {

    public static int increaseCount = 10000000;

    public static int threadCount = 200;

    public static CountDownLatch latch = new CountDownLatch(AtomicTest.increaseCount);

    public volatile int inc = 0;

    public void increase() {
        inc ++;
    }

    public static void main(String[] args) {
        final AtomicTest test = new AtomicTest();
        for (int i = 0; i < AtomicTest.threadCount; i++) {
            new Thread("test" + i) {
                public void run() {
                    for (int j = 0; j < AtomicTest.increaseCount/AtomicTest.threadCount; j++) {
                        test.increase();
                        latch.countDown();
                    }
                }
            }.start();
        }

        try {
            System.out.println("I am waiting.");
            latch.await();
        } catch (Exception ex) {
            System.out.println("error");
        }
        System.out.println(test.inc);
    }
}