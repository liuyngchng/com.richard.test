package richard.test.pojo;

/**
 * Created by Richard on 11/15/16.
 */
public class MyCar {

    private String name;
    private String function;

    private MyCar(String name, String function) {
        this.name = name;
        this.function = function;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
