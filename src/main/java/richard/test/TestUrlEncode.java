package richard.test;

import java.io.UnsupportedEncodingException;

/**
 * Created by richard on 5/25/18.
 */
public class TestUrlEncode {

    public static void main(String[] args) throws UnsupportedEncodingException {
        String test ="aEN4B1FkDoBv5cFsO7Rd5DhH828DkAt7AUWFv5YVi+26xZPxzURqAkx7bECnu7iUC9KM0yyb4q7SAy9Unpq+kQ==";
        String encodeStr = java.net.URLEncoder.encode(test,"UTF-8");
        System.out.println(encodeStr);
    }
}
