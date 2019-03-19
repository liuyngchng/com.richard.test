package richard.test.tcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

/**
 * Created by richard on 3/7/18.
 */
public class InputThread implements Runnable{
    private static final Logger LOGGER = LoggerFactory.getLogger(InputThread.class);
    private Socket s = null;
    private DataOutputStream dos = null;
    private DataInputStream dis = null;

    public InputThread(Socket s){
        this.s = s;
        try {
            dos = new DataOutputStream(s.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        String str;
        while(true){
            System.out.println("input message:");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            try {
                str = br.readLine();
                LOGGER.info("input data is [{}]", str);
                dos.writeBytes(str);
                if("bye".equals(str))
                    break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
