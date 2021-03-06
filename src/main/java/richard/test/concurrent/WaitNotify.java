package richard.test.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by richard on 4/10/18.
 */
public class WaitNotify {
    private static final Logger LOGGER = LoggerFactory.getLogger(Work.class);

    private volatile boolean status = true;

    /**
     * wait方法的调用会释放锁.
     */
    public void doWait() {
        int i = 0;
        synchronized (this) {
            while (status) {
                try {
                    LOGGER.info("I am waiting in thread {}.", Thread.currentThread().getName());
                    try {
                        if (i ++ == 10) {
                            status = false;
                        }
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
//                    LOGGER.info("run wait() in thread {}.", Thread.currentThread().getName());
//                    this.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            LOGGER.info("finish wait in thread ()", Thread.currentThread().getName());
        }
    }

    /**
     * notify方法通知处于wait状态的线程可以竞争锁了.
     */
    public void doNotify() {
        synchronized (this) {
            while (status) {
                LOGGER.info("I need notify.");
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.notify();
                status = false;
            }
            status = true;
            LOGGER.info("finish notify");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final WaitNotify waitNotify = new WaitNotify();
        Thread thread1 = new Thread(() -> waitNotify.doWait());
        thread1.setName("thread1");

        Thread thread2 = new Thread(new Runnable() {
            public void run() {
                waitNotify.doNotify();
            }
        });
        thread2.setName("thread2");

        thread2.start();
        thread1.start();

        LOGGER.info("i am going to join.");
//        Thread.sleep(10000L);
//        LOGGER.info("start to call do wait.");
//        waitNotify.doWait();
        LOGGER.info("i am finished.");
    }
}
