package richard.test.tcp;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by richard on 3/7/18.
 */
public class TcpClient {

    public void go() {
        try {
            Socket s = new Socket("127.0.0.1",8888);
            s.getOutputStream().write(10);
            s.getOutputStream().write("hello".getBytes());
            s.getOutputStream().write("how".getBytes());
            s.getOutputStream().write("are".getBytes());
            s.getOutputStream().write("you".getBytes());
            s.close();
            ReceiveThread receive = new ReceiveThread(s, false);
            new Thread(receive).start();

            InputThread input = new InputThread(s);
            new Thread(input).start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TcpClient client = new TcpClient();
        client.go();
    }
}