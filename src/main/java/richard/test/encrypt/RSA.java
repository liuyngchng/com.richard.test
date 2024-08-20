package richard.test.encrypt;

import java.util.Random;

/**
 * RSA算法的基本步骤
 * (1)密钥生成：
 *      选择两个大素数 p 和 q。这两个素数越大，安全性越高。
 *      计算它们的乘积n=p∗q
 *      𝑛 = 𝑝 ∗𝑞
 *      ，这将成为公开 modulus（模数）。
 *      计算欧拉函数值 φ(n) = (p-1) * (q-1)，这是关键的一个值，所有小于 n 并且与 n 互质的数的个数。
 *      选择一个整数 e，满足 1 < e < φ(n) 并且 e 与 φ(n) 互质。通常 e 选择为65537或其他较小的质数以提高效率。
 *      根据扩展欧几里得算法计算 e， 对于 φ(n) 的模反元素 d，即找到一个整数 d 使得 (e * d) mod φ(n) = 1。
 * (2)公钥和私钥：
 *      公钥由一对数值组成：n 和 e。
 *      私钥由另外一对数值组成：n 和 d。
 * (3)加密：
 *      将明文消息表示为一个整数 M（通常需要通过某种方式将原始数据编码为整数，如使用ASCII或某种约定的转换方式），并确保 0 <= M < n。
 *      加密过程：密文 C 通过计算 \(C \equiv M^e mod n\) 得到。
 * (4)解密：
 *      解密时用私钥 (n 和 d) 来还原原始消息：\(M \equiv C^d mod n\)。
 */
public class RSA {

    private static final int max = 20000;

    // 互为质数的2个大素数
//    private static int P = 10601;
//    private static int Q = 10607;

//    private static int P = 971;
//    private static int Q = 997;

    private static int P = 11;
    private static int Q = 17;

    // modulus  modulus（模数）
    private static int N=P * Q;
//    private static long N = 323;
//    private static long N=112444807;
    // 欧拉函数值 φ(n) = (p-1) * (q-1)
//    private static long T =(P-1)*(Q-1);
////    private static long T = 112423600;

    // 明文数据长度
//    private static int plain_txt_size;
//    private static int []cypher = new int[20000];			//为加密后的数字密文

    /**
     * 公约数只有1的两个数叫做互质数
     * 判断两个数是否互为素数  eg:p和q e和 t
     */
    private static boolean is_co_prime_number(long p, long q) {
        long num1, num2;
        if(p < q) {
            num1 = q;
            num2 = p;
        } else {
            num1 = p;
            num2 = q;
        }
        while (num2 != 0) {
            long temp = num2;
            num2 = num1 % num2;
            num1 = temp;
        }
        return num1 == 1;
    }
    /**
     * 判断一个数是不是素数
     */
    private static boolean is_prime(int s) {
        for (int i=2; i<s; i++) {
            if (s % i == 0) {
                String info  = String.format("%d 不是一个素数(%d is not a prime)", s, s);
                System.out.println(info);
                return false;
            }
        }
        return true;
    }
    /**
     * 求私钥d
     * t:欧拉函数素数性质
     */
    private static int get_private_key(int e, long t) {
        int d;
        for (d=0; d<t; d++) {
            if (e * d % t==1) {
                return d;
            }
        }
        String info  = "获取私钥发生错误(get_private_key_err)";
        throw new RuntimeException(info);
    }
    /**
     * 随机生成与 t互质的随机数e
     */
    private static int get_random(int p,int q) {
        int t=(p-1)*(q-1);
        Random random = new Random();
        while(true) {
            int r = random.nextInt();
            if (r < 0)
                continue;
            int e=r % t;
            if (is_co_prime_number(e, t)) {
                return e;
            }
            //	if(e<=2)
            //	e=3;
        }
    }
    private static int[] encrypt_dt(int e, int n, char[]plain_txt, int size) {
        System.out.println(String.format(
            "encrypt_dt(%d, %d, %s, %d)",
            e, n, new String(plain_txt), size)
        );
        int []cypher = new int[size];
        int []plain_num = new int[size];
//        System.out.print("plain_num=");
        for (int i = 0; i < size; i++) {
            plain_num[i] = plain_txt[i];
//            System.out.print(String.format("%d ", plain_num[i]));
        }
        System.out.println(" ");

        for (int i = 0; i < size; i++) {
            int flag = 1;
            for(int j=0;j<e;j++) {
                flag=(flag * plain_num[i]) % n;
            }
            cypher[i] = flag;
        }
//        final String pln_str = new String(plain_txt);

        return cypher;
    }
    private static char[] decrypt_dt(int d,int n, int[] cypher, int size) {
        long []dec_num = new long[size];

        for (int i = 0; i< size; i++) {
            long flag = 1;
            for (int j=0;j<d;j++) {
                flag = flag * cypher[i] % n;
            }
            dec_num[i] = flag;
        }
        char []dec_str = new char[size];
        for (int i = 0; i< size; i++) {
            dec_str[i]=(char)dec_num[i];
        }
        return dec_str;
    }

    public static void main(String[] args) {

        // public key, e ,N
//        int e;
        // private key
//        int d;
        boolean tep;

        tep=is_prime(RSA.P);
        if(!tep) {
            String info = String.format("p=%d 不是素数(p=%d is not a prime)", RSA.P, RSA.P);
            throw new RuntimeException(info);
        }
        tep = is_prime(RSA.Q);
        if (!tep) {
            String info  = String.format("q=%d 不是素数(q=%d is not a prime)", RSA.Q, RSA.Q);
            throw new RuntimeException(info);
        }

        tep = RSA.is_co_prime_number(RSA.P, RSA.Q);
        if (!tep) {
            String info  = String.format("%d 和 %d 互为质数不成立(%d and %d is not with a co-prime relation)",
                RSA.P, RSA.Q, RSA.P, RSA.Q
            );
            throw new RuntimeException(info);
        }
        System.out.println(String.format("p=%d, q=%d", RSA.P, RSA.Q));
        int e = RSA.get_random(RSA.P, RSA.Q);
//        int e = 480317;
        int n=RSA.N;
//        int n = 968087;
        System.out.println(String.format("公钥(public_key)(e=%d n=%d)", e, n));
        int d = RSA.get_private_key(e, (RSA.P-1)*(RSA.Q-1));
//        int d = 537653;
        System.out.println(String.format("私钥(private_key)d=%d",d));
        String dt = "test1test2test3test4";
        System.out.println(String.format("plain_txt is %s", dt));
        char[] dt_char_array = dt.toCharArray();
        int[] cypher = RSA.encrypt_dt(e, n, dt_char_array, dt_char_array.length);
        System.out.println("加密后的密文为(plain text be encrypted as following)：");
        for(int i = 0; i< dt_char_array.length; i++) {
            System.out.print(String.format("%08d ", cypher[i]));
        }
        System.out.println(" ");
        char []dec_str = RSA.decrypt_dt(d, n, cypher, cypher.length);
        System.out.println(String.format("解密后的明文为(cypher be decrypted as following)： %s", new String(dec_str)));
    }
}
