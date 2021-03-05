package richard.test.concurrent;

import java.util.ArrayList;
import java.util.List;

public class WaitNotifyTest {

    private static volatile List products = new ArrayList();

    private static final int SIZE_LIMIT = 10;

    /**
     * continue to work until something happen.
     */
    public void produceProduct() {
        synchronized (WaitNotifyTest.products) {
            System.out.println(Thread.currentThread().getName() + " 线程启动生产任务");
            while (true) {
                WaitNotifyTest.products.add("myProduct.");
                System.out.println(Thread.currentThread().getName() + " 线程生产货物，总数" + WaitNotifyTest.products.size());
                try {
                    Thread.sleep(100);
                    if (WaitNotifyTest.products.size() == SIZE_LIMIT) {
                        System.out.println("货物数量达到仓库上限，停止生产，等待消费。");
                        WaitNotifyTest.products.wait();
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * start count product until something happen.
     */
    public void consumeProduct() {
        synchronized (WaitNotifyTest.products) {
            while (!products.isEmpty()) {
                products.remove(0);
                System.out.println(Thread.currentThread().getName() + " 线程消费产品， " + products.size());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (products.size() < SIZE_LIMIT) {
                    try {
                        products.notify();
                        System.out.println(Thread.currentThread().getName() + " 线程,仓库有空余位置，通知进行生产， " + products.size());
                        Thread.sleep(1000);
                        Thread.yield();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * start test.
     * @param args
     */
    public static void main(String[] args) {

        WaitNotifyTest test = new WaitNotifyTest();
        Thread t1 = new Thread(() -> test.produceProduct());
        t1.setName("t1");

        Thread t2 = new Thread (() -> test.consumeProduct());
        t2.setName("t2");
        t1.start();
        t2.start();
    }
}
