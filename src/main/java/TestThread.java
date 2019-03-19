/**
 * Created by richard on 4/18/18.
 */
public class TestThread {
    public synchronized void idle() throws InterruptedException {
        System.out.println("start idle");
        this.wait();
        Thread.sleep(10000L);
        System.out.println("end idle");
    }

    public static void main(String[] args) throws InterruptedException {
        TestThread testThread = new TestThread();
        testThread.idle();
//        int i = 0;
//        while (i++ < 500000) {
//            new Thread(new Runnable() {
//                public void run() {
//                    System.out.println("I am " + Thread.currentThread().getName());
//                    try {
////                        this.wait();
//                        Thread.sleep(100000L);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println(Thread.currentThread().getName() + "finished.");
//                }
//            }, "thread "+ i).start();
//        }
    }
}
