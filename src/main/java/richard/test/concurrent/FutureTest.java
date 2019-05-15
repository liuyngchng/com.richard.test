package richard.test.concurrent;

import java.util.concurrent.*;

/**
 * Created by richard on 5/17/18.
 */
public class FutureTest {

    public String doJob() throws ExecutionException, InterruptedException {
        MyJob job = new MyJob();
        FutureTask<String> future = new FutureTask<String>(job);
        new Thread(future).start();
        return future.get();
    }

    public void work(){
        new Thread(new MyJob()).start();
    }


    class MyJob implements Callable<String>, Runnable {

        public String call() throws Exception {
            doJob();
            return "I am OK.";
        }

        public void run() {
            try {
                doJob();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void doJob() throws InterruptedException {
            System.out.println("job start.");
            Thread.sleep(5000L);
            System.out.println("job end.");
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("start future job.");
        FutureTest test = new FutureTest();
        System.out.println(test.doJob());
        System.out.println("start thread job.");
        test.work();
        System.out.println("end thread job.");

    }

}
