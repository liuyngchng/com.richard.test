package richard.test.concurrent;

import java.util.ArrayList;
import java.util.List;

public class WaitNotifyTest {

    private static volatile List productList = new ArrayList();

    private static final int SIZE_LIMIT = 10;

    /**
     * continue to work until something happen.
     */
    public void produceProduct() {
        System.out.println(Thread.currentThread().getName() + " 线程启动生产任务");
        while (true) {
            WaitNotifyTest.productList.add("myProduct.");
            System.out.println("线程" + Thread.currentThread().getName() + "生产货物，总数" + WaitNotifyTest.productList.size());
            try {
                Thread.sleep(100);
                if (WaitNotifyTest.productList.size() == SIZE_LIMIT) {
                    System.out.println("货物数量达到仓库上限，停止生产，等待消费。");
                    productList.wait();
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * start count product until something happen.
     */
    public void consumeProduct() {
        synchronized (WaitNotifyTest.productList) {
            while (true) {

                if (!productList.isEmpty()) {
                    productList.remove(1);
                    System.out.println(Thread.currentThread().getName() + " 线程消费产品， " + productList.size());
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (productList.size() < SIZE_LIMIT) {
                    try {
                        productList.notify();
                        System.out.println(Thread.currentThread().getName() + " 线程,仓库有空余位置，通知进行生产， " + productList.size());
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                System.out.println("线程：" + Thread.currentThread().getName()
                        + "收到通知，清点货物数量为 " + WaitNotifyTest.productList.size());
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
        t2.start();
        t1.start();
    }
}
