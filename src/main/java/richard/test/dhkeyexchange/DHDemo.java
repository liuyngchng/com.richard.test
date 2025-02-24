package richard.test.dhkeyexchange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by richard on 19/03/2019.
 * online doc
 * https://en.wikipedia.org/wiki/Diffie–Hellman_key_exchange
 */
public class DHDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(DHDemo.class);

    private static final String MITM = "Man in the middle";
    /**
     * A, B, public prime.
     */
    private static Integer P = 23;

    /**
     * A, B, public primitive root modulo P.
     */
    private static Integer G = 5;

    /**
     * Private key for A.
     */
    private static Integer s1 = 6;

    /**
     * Private key for B.
     */
    private static Integer s2 = 15;


    /**
     * 公共大素数 p, g
     * m, n, a, b 为正整数
     * A 的秘钥对, 公钥 p1，私钥 s1; p1 = g ^ s1 % p; 则 p1 = g ^ s1 - p ^ m;
     * B 的秘钥对, 公钥 p2，私钥 s2; p2 = g ^ s2 % p; 则 p2 = g ^ s2 - p ^ n;
     * A 的秘钥 k1 = p2 ^ s1 % p; k1 = p2 ^ s1 - p ^ a;
     * B 的秘钥 k2 = p1 ^ s2 % p; k2 = p1 ^ s2 - p ^ b;
     * (2 ^ 2) ^ 3 = 64 = 2 ^( 2 * 3) = 2 ^ 6
     * 2 ^ 4 + 2 ^ 1 = 16 + 2 = 14 = 2 ^ ?
     * k1   =   p2 ^ s1 - p ^ a
     * k2   =   p1 ^ s2 - p ^ b
     *      =   (g ^ s1 - p ^ m) ^ s2 - p ^ b
     *      =   (g ^ s1) ^ s2 - (p ^ m) ^ s2 - p ^ b
     *      =   (g ^ s2) ^ s1 - (p ^ m) ^ s2 - p ^ b
     *      =   (p2 + p ^ n) ^ s1 - (p ^ m) ^ s2 - p ^ b
     *      =   p2 ^ s1 + (p ^ n) ^ s1 - (p ^ m) ^ s2 - p ^ b
     *      =   p2 ^ s1 + p ^ (n * s1) - p ^ (m * s2) - p ^ b
     *      =   k1 + p ^ a + p ^ (n * s1) - p ^ (m * s2) - p ^ b
     *      =   k1 + p ^ a +


     * @param args
     */
    public static void main(String[]  args) {
        LOGGER.info("public params, p = {}, g = {}", P, G);
        double p1 = Math.pow(G, s1) % P;
        LOGGER.info("A's public key p1 is {}, transport to B, {} knows", p1, MITM);
        double p2 = Math.pow(G, s2) % P;
        LOGGER.info("B's public key p2 is {}, transport to A, {} knows", p2, MITM);
        double k1 = Math.pow(p2, s1) % P;
        LOGGER.info("A's secret key k1 is {}", k1);
        double k2 = Math.pow(p1, s2) % P;
        LOGGER.info("B's secret key k2 is {}", k2);
        k1 = Math.pow(Math.pow(G, s2) % P, s1) % P;
        k2 = Math.pow(Math.pow(G, s1) % P, s2) % P;
    }
}
