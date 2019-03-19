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
    private static Integer PUB_P = 31;

    /**
     * A, B, public primitive root modulo PUB_P.
     */
    private static Integer PUB_G = 23;

    /**
     * Private key for A.
     */
    private static Integer privateKeyA = 4;

    /**
     * Private key for B.
     */
    private static Integer privateKeyB = 3;

    public static void main(String[]  args) {
        LOGGER.info("public params, {}, {}", PUB_P, PUB_G);
        double publicKeyA = Math.pow(PUB_G, privateKeyA) % PUB_P;
        LOGGER.info("A's public key is {}, transport to B, {} knows", publicKeyA, MITM);
        double publicKeyB = Math.pow(PUB_G, privateKeyB) % PUB_P;
        LOGGER.info("B's public key is {}, transport to A, {} knows", publicKeyB, MITM);
        double keyA = Math.pow(publicKeyB, privateKeyA) % PUB_P;
        LOGGER.info("A's secret key is {}", keyA);
        double keyB = Math.pow(publicKeyA, privateKeyB) % PUB_P;
        LOGGER.info("B's secret key is {}", keyB);
    }
}
