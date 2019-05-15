package richard.test.encrypt;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * openssl genrsa -out rsa_private_key.pem   1024  #生成私钥
 * openssl pkcs8 -topk8 -inform PEM -in rsa_private_key.pem -outform PEM -nocrypt -out rsa_private_key_pkcs8.pem #Java开发者需要将私钥转换成PKCS8格式
 * openssl rsa -in rsa_private_key.pem -pubout -out rsa_public_key.pem #生成公钥
 *
 * 加密
 * openssl rsautl -encrypt -pubin -inkey rsa_public_key.pem -in data.txt -out edata.txt
 * 解密
 * openssl rsautl -decrypt -inkey rsa_private_key_pkcs8.pem -in edata.txt -out data1.txt
 **/
public class Demo {

    private static final String PRIVATE_KEY="/Users/richard/work/study/rsa_private_key_pkcs8.pem";
    private static final String PUBLIC_KEY="/Users/richard/work/study/rsa_public_key.pem";
    private static final Logger LOGGER = LoggerFactory.getLogger(Demo.class);

    private static String publicKeyStr;
    private static String privateKeyStr;
    public static void test(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        //get the private key
        File file = new File(PRIVATE_KEY);
        FileReader fileReader = new FileReader(PRIVATE_KEY);
        BufferedReader reader = new BufferedReader(fileReader);
        StringBuffer sb = new StringBuffer(512);
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains("----")) {
                continue;
            }
            sb.append(line);
        }
        reader.close();
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.decodeBase64(sb.toString()));
        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPrivateKey privateKey = (RSAPrivateKey) kf.generatePrivate(spec);
        LOGGER.info("exponent: {}", privateKey.getPrivateExponent());
        LOGGER.info("modulus: {}", privateKey.getModulus());
        privateKeyStr = Base64.encodeBase64String(privateKey.getEncoded());
        LOGGER.info("privateKey is {}", privateKeyStr);
        LOGGER.info("file   Key is {}", sb.toString());

        fileReader = new FileReader(PUBLIC_KEY);
        reader = new BufferedReader(fileReader);
        sb = new StringBuffer(512);
        while ((line = reader.readLine()) != null) {
            if (line.contains("----")) {
                continue;
            }
            sb.append(line);
        }
        reader.close();
        BASE64Decoder base64 = new BASE64Decoder();
        byte[] buffer = base64.decodeBuffer(sb.toString());
    //        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec spec1 = new X509EncodedKeySpec(buffer);

//        X509EncodedKeySpec spec1 = new X509EncodedKeySpec(Base64.decode(sb.toString()));
        KeyFactory kf1 = KeyFactory.getInstance("RSA");
        RSAPublicKey publicKey = (RSAPublicKey) kf1.generatePublic(spec1);

        LOGGER.info("exponent: {}", publicKey.getPublicExponent());
        LOGGER.info("modulus: {}", publicKey.getModulus());
        publicKeyStr = Base64.encodeBase64String(publicKey.getEncoded());
        LOGGER.info("publicKey is {}", publicKeyStr);
        LOGGER.info("file  Key is {}", sb.toString());
        String plainText = "{\"a\":\"123\"}";
//        String plainText = "test";
        try {
            String sign = RSAUtils.sign(plainText, privateKey, "GBK");
            LOGGER.info("signText is {}", sign);
            boolean result = RSAUtils.verifySign(plainText,sign, publicKey, "GBK");
            LOGGER.info("verifySign result is {}", result);
            String encryptedText = RSAUtils.encrypt(plainText, publicKeyStr);
            LOGGER.info("encryptedText is {}", encryptedText);
            String decryptText = RSAUtils.decrypt(encryptedText, privateKeyStr);
            LOGGER.info("decryptText is {}", decryptText);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}