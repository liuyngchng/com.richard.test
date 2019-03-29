package richard.test.classloader;

/**
 * Created by richard on 29/03/2019.
 */
public class MyLoadClassTest {
    private String names;
    private String addrs;

    public String getName() {
        return names;
    }

    public void setName(String name) {
        this.names = name;
    }

    public String getAddr() {
        return addrs;
    }

    public void setAddr(String addr) {
        this.addrs = addr;
    }

    @Override
    public String toString() {
        return "MyLoadClassTest{" +
            "name='" + names + '\'' +
            ", addr='" + addrs + '\'' +
            '}';
    }
}