package richard.test.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Richard on 10/9/16.
 */
public class VehicleProxy {

    private Vehicle vehicle;

    public VehicleProxy(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Vehicle create(){
        final Class<?>[] interfaces = new Class[]{Vehicle.class};
        final VehicleInvocationHandler handler = new VehicleInvocationHandler(vehicle);

        return (Vehicle) Proxy.newProxyInstance(Vehicle.class.getClassLoader(), interfaces, handler);
    }

    public class VehicleInvocationHandler implements InvocationHandler {

        private final Vehicle vehicle;

        public VehicleInvocationHandler(Vehicle vehicle) {
            this.vehicle = vehicle;
        }

        public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {

            System.out.println("--before running...");
            Object ret = method.invoke(vehicle, args);
            System.out.println("--after running...");

            return ret;
        }

    }
}