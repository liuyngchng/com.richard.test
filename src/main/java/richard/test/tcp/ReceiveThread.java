package richard.test.tcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by richard on 3/7/18.
 */
public class ReceiveThread implements Runnable{
    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiveThread.class);
    private Socket s = null;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;
    private boolean isServer;

    private BlockingQueue<String> contentQueue = new LinkedBlockingDeque<String>(1024);

    private class Consumer implements Runnable {
        private final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);
        public void run() {
            LOGGER.info("consumer started to echo message received.");
            while (true) {
                try {
                    String content = contentQueue.take();
                    LOGGER.info("received [{}]", content);
                    if (isServer && s.isConnected()) {
//                        dos.writeBytes(content);
                    }
                } catch ( InterruptedException e) {
                    e.printStackTrace();
                }
//                catch (IOException ex) {
//                    LOGGER.error("error", ex);
//                }
            }
        }
    }

    public ReceiveThread(Socket socket, boolean isServer){
        this.s = socket;
        this.isServer = isServer;
        try {
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(new Consumer()).start();
    }

    public void run() {
        int byteCount = 0;
        byte[] buffer = new byte[10];
        StringBuffer sb = new StringBuffer(512);

        do {
            try {
                byteCount = dis.read(buffer);
//                LOGGER.debug("buffer is {}", buffer);
                if (byteCount > 0 ) {
                    sb.append(new String(buffer, 0, byteCount));
                    contentQueue.add(new String(buffer, 0, byteCount));
                } else {
                    break;
                }
            } catch (IOException e) {
                LOGGER.error("error", e);
            }
//            LOGGER.debug("byteCount = {}, buffer length = {}", byteCount, buffer.length);
        } while (true);
        LOGGER.info("Server received: {}", sb);
        try {
            dis.close();
            dos.close();
            LOGGER.debug("data stream closed.");
        } catch (IOException e) {
            LOGGER.error("error", e);
        }
    }
}
