package richard.test.rpc;

/**
 * Created by Richard on 8/15/17.
 */
public class EchoServiceImpl implements EchoService {

    public String echo(String name) {
        return "Hello " + name;
    }

}
