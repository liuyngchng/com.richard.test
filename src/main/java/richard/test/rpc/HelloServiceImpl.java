package richard.test.rpc;

/**
 * Created by Richard on 8/15/17.
 */
public class HelloServiceImpl implements HelloService {

    public String hello(String name) {
        return "Hello " + name;
    }

}
