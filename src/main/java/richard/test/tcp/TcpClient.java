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
            Socket s = new Socket("127.0.0.1",8081);
//            Socket s = new Socket("111.203.165.13",19112);
            final String msg = "6800CB03008188C3C00DD4998C80010100000000012820100101062B9BBC820C000C0001004C5F187658A75B93E38E359A176F3DC938D49B70AE59D0334BFEB3B0F4B6A4C95960D9779DBA6435E80FB5569B3BDBB79223DBCA2205F1FD52A101F58E226227E80CD7B1DD24D030A57ECF2780C94EA7F3AC806792CB04EF7A03857E4D2CFD9562D63CFCF9F7247E69207BD9242DC365C2272A97F072D2EA64F2115D21650E845DCDA4068D86D0524B3928A92A3EEBB330E8958021E155E26875FC3CEE02971A0E31F2AC662616594caa4bb8143ebcc18a17addfdc3ebc";
            s.getOutputStream().write(TcpClient.getBytes(msg));
//            s.getOutputStream().write("hello".getBytes());
//            s.getOutputStream().write("how".getBytes());
//            s.getOutputStream().write("are".getBytes());
//            s.getOutputStream().write("you".getBytes());

            ReceiveThread receive = new ReceiveThread(s, false);
            new Thread(receive).start();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            s.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static byte[] getBytes(String s) {
        if (null == s || "" == s) {
            return null;
        }
        try {
            return TcpClient.getBytes(s, s.length() / 2 + s.length() % 2);
        } catch (Exception ex) {
            return null;
        }
    }

    public static byte[] getBytes(String s, int size) {
        final byte[] bytes = new byte[size];
        if (null == s || "" == s) {
            return bytes;
        }
        final String str;
        if (s.length() % 2 == 0) {
            str = s;
        } else {
            str = "0" + s;
        }
        final int byteSize = str.length() / 2 + str.length() % 2 > size ?
            size : str.length() / 2 + str.length() % 2;
        for (int i = 0; i < byteSize; i ++) {
            bytes[i] = TcpClient.getByte(str.substring(i * 2, i * 2 + 2));
        }
        return bytes;
    }

    public static byte getByte(String hex) {
        if (null == hex || "" == hex) {
            return 0;
        }
        return (byte) Integer.parseInt(hex, 16);
    }

    public static void main(String[] args) {
        TcpClient client = new TcpClient();
        client.go();
    }
}