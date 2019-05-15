package richard.test.rpc;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * Created by Richard on 8/15/17.
 */
public class RpcProvider {

    public static void main(String[] args) throws Exception {
        HelloService service = new HelloServiceImpl();
        RpcFramework.export(service, 1234);
        ServerSocket server = null;
        RpcProvider.class.newInstance();
        try {
            server = new ServerSocket(12345);
            int a = 0;
            while (true) {
                a++;
                System.out.println("listening..." + a);
                final Socket socket = server.accept();
                byte[] buffer = new byte[512];
                int length = socket.getInputStream().read(buffer);
                String test = new String(buffer, 0, length);

                System.out.println("received something...," + test);
                socket.getOutputStream().write("are you ok?".getBytes());
                socket.close();
            }
        } finally {
            server.close();
        }

    }

}
