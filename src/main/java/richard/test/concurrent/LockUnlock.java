package richard.test.concurrent;

import org.slf4j.Logger;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by richard on 4/12/18.
 */
public class LockUnlock {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(LockUnlock.class);
    private final ReentrantLock lock = new ReentrantLock();
    public void doWork() {
        lock.lock();
        try {
//            LOGGER.info(
//                "{} want to sleep, queued length is {}, hold count is {}",
//                Thread.currentThread().getName(),
//                lock.getQueueLength(),
//                lock.getHoldCount()
//            );
            final Condition condition = lock.newCondition();
            condition.await();
            Thread.sleep(3000L);
            LOGGER.info("i am waken.");
        } catch (Exception ex) {

        } finally {
            lock.unlock();
        }
    }
    public void doOtherWork() throws InterruptedException {
        lock.lock();
        final Condition condition1 = lock.newCondition();
        final Condition condition2 = lock.newCondition();
        if (condition1 == condition2) {
            LOGGER.info("conditions is the same instance.");
        } else {
            LOGGER.info("condition is different.");
        }
        try {
//            LOGGER.info(
//                "{} want to sleep, queued length is {}, hold count is {}",
//                Thread.currentThread().getName(),
//                lock.getQueueLength(),
//                lock.getHoldCount()
//            );
            Thread.sleep(3000L);
            LOGGER.info("i am waken.");
        } catch (Exception ex) {

        } finally {
            condition1.signalAll();
            lock.unlock();
        }

    }

    public static void main(String[] args) throws Exception {
        final LockUnlock lockUnlock = new LockUnlock();
        lockUnlock.doOtherWork();
//        Thread thread1 = new Thread(new Runnable() {
//            public void run() {
//                lockUnlock.doWork();
//            }
//        }, "myThread1");
//        Thread thread2 = new Thread(new Runnable() {
//            public void run() {
//                lockUnlock.doWork();
//            }
//        }, "myThread2");
//        Thread.currentThread().setName("myMainThread");
//        thread1.start();
//        LOGGER.info("thread1 start.");
//        thread2.start();
//        LOGGER.info("thread2 start.");
//        Thread thread3 = new Thread(new Runnable() {
//            public void run() {
//                try {
//                    lockUnlock.doOtherWork();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, "myThread3");
//        thread3.start();
    }
}
