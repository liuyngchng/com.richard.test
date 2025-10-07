package richard.test.tcp;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by richard on 3/7/18.
 */
public class TcpServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(TcpServer.class);

    public void go(){
        ServerSocket ss = null;
        Socket s;
        try {
            ss = new ServerSocket(8888);
//            s.setSoLinger();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                s = ss.accept();
                s.setKeepAlive(false);
                LOGGER.info("data flowed in.");
                ReceiveThread recv = new ReceiveThread(s, true);
                new Thread(recv).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public static void main(String args []) {
        LOGGER.info("Server started .");
        TcpServer server = new TcpServer();
        server.go();

    }
}
