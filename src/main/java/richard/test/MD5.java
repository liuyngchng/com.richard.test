package richard.test;

import java.security.MessageDigest;

public class MD5 {
    public final static String getVal(String pwd) {
        //用于加密的字符
        char md5String[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F' };
        try {
            byte[] btInput = pwd.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            System.out.println(j+"=======================");
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte bi = md[i];
                str[k++] = md5String[bi >>> 4 & 0xf]; // 5
                str[k++] = md5String[bi & 0xf]; // F
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
    public static void main(String[] args) {
        String before="20230214";
        String result = MD5.getVal(before);
        System.out.println(result + ",长度是" + result.length());
//        int key = (((((((27* (byte)'h'+27)* (byte)'e') + 27) * (byte)'l') + 27) * (byte)'l' +27) * 27 ) + (byte)'o' ;
//        System.out.println(key);
        System.out.println(before.hashCode());
    }

}
