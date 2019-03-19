package richard.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by richard on 5/15/18.
 */
public class TestGzip {

    public static void main(String[] args) {
        String s = "this is a text.this is a text.this is a text.this is a text.this is a text.this is a text.this is a text.this is a text.this is a text.this is a text.this is a text.this is a text.";
        char[] c = s.toCharArray();
        byte[] bytes = s.getBytes();
        sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
        String cipher = encoder.encode(bytes);
        System.out.println(cipher);
        byte[] compressed = TestGzip.compress(s, "utf-8");
        System.out.println(compressed);
        byte[] decompressed = TestGzip.uncompress(compressed);
        System.out.println(decompressed);

    }
    public static byte[] compress(String str, String encoding) {
        if (str == null || str.length() == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes(encoding));
            gzip.close();
        } catch (IOException e) {
            System.err.println("gzip compress error.");
        }
        return out.toByteArray();
    }

    public static byte[] uncompress(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try {
            GZIPInputStream ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
        } catch (IOException e) {
            System.err.println("gzip uncompress error.");
        }

        return out.toByteArray();
    }
}
