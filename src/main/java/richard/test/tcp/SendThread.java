package richard.test.tcp;

import java.io.*;
import java.net.Socket;

/**
 * Created by richard on 3/7/18.
 */
public class SendThread implements Runnable {
    private Socket s = null;
    private DataOutputStream dos = null;
    private DataInputStream dis = null;

    public SendThread(Socket s){
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
                dos.writeBytes(str);
                if("bye".equals(str))
                    break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
