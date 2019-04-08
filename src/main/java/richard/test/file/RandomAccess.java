package richard.test.file;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * Created by richard on 08/04/2019.
 */
public class RandomAccess {

    public static void main(String[] args) throws Exception {
        reSizeFile();
    }

    public static void reSizeFile() throws Exception {
        File file = new File("/Users/richard/test/test.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        long length = randomAccessFile.length();
        randomAccessFile.setLength(10L);
//        randomAccessFile.seek(length);
//        randomAccessFile.write("\r\nHello, how are you\r\nI am OK.".getBytes());
//        randomAccessFile.readLine();
        randomAccessFile.close();
    }
}
