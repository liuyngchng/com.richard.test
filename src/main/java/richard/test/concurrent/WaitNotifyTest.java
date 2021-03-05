package richard.test.concurrent;

import java.util.ArrayList;
import java.util.List;

public class WaitNotifyTest {

    private static volatile List productList = new ArrayList();

    /**
     * continue to work until something happen.
     */
    public void continueWork() {
        synchronized (WaitNotifyTest.productList) {
            System.out.println(Thread.currentThread().getName() + " 线程启动生产任务……");
            for (int i = 0; i < 20; i++) {
                WaitNotifyTest.productList.add("myProduct.");
                System.out.println("线程"+Thread.currentThread().getName()+"生产货物，总数" + WaitNotifyTest.productList.size());
                try {
                    Thread.sleep(500);
                    if(WaitNotifyTest.productList.size() == 5) {
                        System.out.println("货物数量达到5，发出通知，清点货物。。");
                        productList.notify();
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
    public void startCountProduct() {
        synchronized (WaitNotifyTest.productList) {
            System.out.println(Thread.currentThread().getName() + " 线程启动清点货物任务……");
            if(productList.size() < 5) {
                try {
                    productList.wait();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            System.out.println("线程：" + Thread.currentThread().getName() + "收到通知，清点货物数量为 " + WaitNotifyTest.productList.size());
        }
    }

    /**
     * start test.
     * @param args
     */
    public static void main(String[] args) {

        WaitNotifyTest test = new WaitNotifyTest();
        Thread t1 = new Thread(() -> test.continueWork());
        t1.setName("t1");

        Thread t2 = new Thread (() -> test.startCountProduct());
        t2.setName("t2");
        t2.start();
        t1.start();
    }
}
