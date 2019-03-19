package richard.test.proxy;

/**
 * Created by Richard on 10/9/16.
 */
public class Task {

    public static void main(String [] args) throws IllegalAccessException, InstantiationException {
        System.out.println();

        Vehicle car = Car.class.newInstance();
        VehicleProxy proxy = new VehicleProxy(car);

        Vehicle proxyObj = proxy.create();
        proxyObj.run();
    }
}
