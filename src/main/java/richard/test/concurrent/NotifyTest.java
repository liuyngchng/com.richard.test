package richard.test.concurrent;

public class NotifyTest {

    public Object lock = new Object();
    public void testWait() {
        synchronized (this.lock) {
            System.out.println(Thread.currentThread().getName() + " start-----");
            try {
                this.lock.wait(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " end-------");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final NotifyTest test = new NotifyTest();
        for(int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    test.testWait();
                }
            }).start();
        }

        synchronized (test.lock) {
            System.out.println("---notify---");
            test.lock.notify();
        }
        System.out.println("i am sleeping.");
        Thread.sleep(3000);

        System.out.println("----notifyAll----");

        synchronized (test.lock) {
            test.lock.notifyAll();
        }
    }
}
