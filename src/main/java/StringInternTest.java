import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StringInternTest {

    public static void main(String[] args) {
        List<Thread> threadList = new ArrayList<>(8);
        for (int i = 0; i < 2; i ++) {
            // 两个线程是两个不同的Job instance
            final Job job = new Job("sameId", "sameName");
            Thread thread = new Thread(job);
            thread.setName("thread" + i);
            threadList.add(thread);
        }

        for (int i = 2; i < 4; i ++) {
            // 两个线程是两个不同的Job instance
            final Job job = new Job("diffId" + i , "diffName" + i);
            Thread thread = new Thread(job);
            thread.setName("thread" + i);
            threadList.add(thread);
        }

        for (int i = 0; i < threadList.size(); i ++) {
            threadList.get(i).start();
            System.out.println(new Date() + " " + threadList.get(i).getName()   + " start run");
        }
    }

    private static class Job implements Runnable {
        Job(String id, String name) {
            this.id = id;
            this.name = name;
        }

        private String name;

        private String id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public void run() {
            final String key = this.getName() + "_" + this.getId();
            // for test
            // synchronized (key) {
            synchronized (key.intern()) {
                System.out.println(new Date() + " " + Thread.currentThread().getName() +" get key: " + key);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(new Date() + " " + Thread.currentThread().getName() +" release key: " + key);
            }
        }
    }

}
