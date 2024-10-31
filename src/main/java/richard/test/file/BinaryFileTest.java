package richard.test.file;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 写二进制文件测试
 * @author rd
 *
 */
public class BinaryFileTest {

    public static void main(String[] args) throws Exception {
        final String fileName = "/home/rd/workspace/test/test1.txt";
        final FileInputStream fis = new FileInputStream(fileName);
        final DataInputStream dis = new DataInputStream(fis);

        byte[] bytes = new byte[fis.available()];
        int i = 0;
        while (dis.available() > 0) {
            byte b = dis.readByte();
            bytes[i++] = b;
            char data = (char)b;
            System.out.print(data);
        }
        dis.close();
        fis.close();
        FileOutputStream fos = new FileOutputStream("/home/rd/workspace/test/test1.bin");
        byte[] tst1 = {0x68, 0x00, (byte)0xEB, 0x03,0x00, (byte)0x81, (byte)0x88, (byte)0xc1, (byte)0xe0,
                0x00,0x40, 0x40, (byte)0xf4, 0x20,
                0x20, 0x42};
        fos.write(tst1);
        fos.close();
        FileOutputStream fos1 = new FileOutputStream("/home/rd/workspace/test/test2.bin");
        byte[] tst2 = {0x68, 0x69, 0x6A, 0x6B};
        fos1.write(tst2);
        fos1.close();
    }
}
