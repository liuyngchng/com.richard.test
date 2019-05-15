package richard.test;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * Created by richard on 3/23/18.
 */
public class TimeOutTest {
    public static void main(String[] argv) {
//        int a= 1;
//        int b = 2;
//        int c = a | b;
//
//        final Timer timer = new HashedWheelTimer(5, TimeUnit.MILLISECONDS, 8    );
//        System.out.println("start timeout 5, " + System.currentTimeMillis()/1000);
//        timer.newTimeout(new TimerTask() {
//            public void run(Timeout timeout) throws Exception {
//                System.out.println("timeout 5, " + System.currentTimeMillis()/1000);
//            }
//        }, 5, TimeUnit.SECONDS);
//        System.out.println("start timeout 10, " + System.currentTimeMillis()/1000);
//        timer.newTimeout(new TimerTask() {
//            public void run(Timeout timeout) throws Exception {
//                System.out.println("timeout 10, " + System.currentTimeMillis()/1000);
//            }
//        }, 10, TimeUnit.SECONDS);
//        System.out.println("end.");
//        timer.stop();

        Object [] a1 = new Object[]{"test", 1, 2.5, new java.util.Timer("test1")};
        for (Object obj : a1) {
            Class cls = obj.getClass();
            String className = cls.getCanonicalName();
            System.out.println("className is " + className);
            try {
                Method f = cls.getDeclaredMethod("toString", new Class[0]);
                String result = (String)f.invoke(obj, null);
                System.out.println("result is " + result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
