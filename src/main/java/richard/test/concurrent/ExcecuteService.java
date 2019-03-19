package richard.test.concurrent;

import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by richard on 3/28/18.
 */
public class ExcecuteService {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ExcecuteService.class);

    public static void main(String[] args) throws Exception {
        System.out.println("availableProcessors is " + Runtime.getRuntime().availableProcessors());
        String str = "this is my test";
        System.out.println(str.hashCode());
        ExecutorService service = new ThreadPoolExecutor(4, 4, 1, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(1024 * 1024), new ThreadPoolExecutor.AbortPolicy());
//        service.submit(new Runnable() {
//            public void run() {
//                System.out.println("task done" + new Date());
//                try {
//                    Thread.sleep(1000L);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        Future<String> future = service.submit(new Callable<String>() {
            public String call() throws Exception {
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    LOGGER.error("error", e);
                }
                System.out.println("task done, " + new Date());
                return "FINISH";
            }
        });
        service.submit(new Thread("test1") {
            public void run() {
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    LOGGER.info("error 1, {}", Thread.currentThread().getName());
                }
                LOGGER.info("hello");
            }
        });
//        int time = 0;
//        while (true) {
//            System.out.println("time is "  + time++ + ", status is " + future.isDone());
//            if (future.isDone()) {
//                System.out.println("result is " + future.get());
//                System.exit(0);
//            }
//            Thread.sleep(1000L);
//        }
        Thread.sleep(1000L);
        LOGGER.info("start shut down.");
        List<Runnable> task = service.shutdownNow();
        LOGGER.info("task size {}", task.size());
        LOGGER.info("start wait");
        boolean result = service.awaitTermination(6L, TimeUnit.SECONDS);
//        LOGGER.info("finish wait, {}", result);
        Thread.currentThread().setDaemon(true);

    }
}
