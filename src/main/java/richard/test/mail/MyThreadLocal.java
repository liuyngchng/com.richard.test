package richard.test.mail;

import java.text.SimpleDateFormat;

/**
 * Created by Richard on 8/9/17.
 */
public class MyThreadLocal<T> extends ThreadLocal<SimpleDateFormat> {

    @Override
    protected SimpleDateFormat initialValue() {
        System.out.println("init value.");
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
    }
}
