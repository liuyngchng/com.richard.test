package richard.test.encrypt;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.File;
import java.io.FileInputStream;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 1.生成私钥文件 1.key文件
 * openssl pkcs12 -in apple_payment.p12 -nocerts -nodes -out 1.key
 * 2.导出私钥
 * openssl rsa -in 1.key -out privateKey.pem
 * writing RSA key
 * 3.导出公钥
 * openssl rsa -in 1.key -pubout -out publicKey.pem
 */

public class RSAUtils {

    private static Logger LOGGER = LoggerFactory.getLogger(RSAUtils.class);

    private static final String KEY_ALGORITHM = "PKCS12";
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    private static final String CIPHER_TYPE = "X.509";
    private static final String ENCRYPT_TYPE = "RSA";

    private static PublicKey publicKey;
    private static PrivateKey privateKey;
    //初始化证书
    public static void initCert(String priKeyPath, String alias, String password, String pubKeyPath) {
        try {
            LOGGER.info("证书初始化开始！");
            KeyStore keystore = KeyStore.getInstance(KEY_ALGORITHM);
            keystore.load(new FileInputStream(new File(priKeyPath)), password.toCharArray());
            privateKey = (PrivateKey) keystore.getKey(alias, password.toCharArray());


            CertificateFactory certificateFactory = CertificateFactory.getInstance(CIPHER_TYPE);
            X509Certificate cert = (X509Certificate) certificateFactory.generateCertificate(new FileInputStream(new File(pubKeyPath)));
            publicKey = cert.getPublicKey();
            LOGGER.info("证书初始化结束！");
        } catch (Exception e) {
            LOGGER.error("证书初始化异常！{}{}", e.getMessage(), e);
        }
    }




    /**
     * 方法描述：初始化公钥
     * @param: String str
     * @return: PublicKey
     * @version: 1.0
     * @time: 2011-12-7 上午9:11:15
     */
    public static PublicKey initPublicKey(String str) {
//        KeyFactory keyFactory;
//        try {
//            keyFactory = KeyFactory.getInstance("RSA");
//
//            X509EncodedKeySpec keySpec = new X509EncodedKeySpec((Base64.decodeBase64(str)));
//
//            PublicKey pubkey = keyFactory.generatePublic(keySpec);
//
//            return pubkey;
//
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//            LOGGER.info("初始化公钥无此算法");
//        } catch (InvalidKeySpecException e) {
//            e.printStackTrace();
//            LOGGER.info("初始化公钥无效的密钥规范");
//        } catch (Exception e) {
//            e.printStackTrace();
//            LOGGER.info("初始化公钥异常");
//        }
        return null;
    }

    /**
     * 方法描述：签名
     * @param: String content, PrivateKey key
     * @return: String
     * @version: 1.0
     * @time: 2011-12-7 上午9:22:21
     */
    public static String sign(String content, PrivateKey key) throws Exception {
        PrivateKey prikey = key;
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(prikey);
        signature.update(content.getBytes("UTF-8"));
        byte[] signBytes = signature.sign();
        String sign = new String(Base64.encodeBase64(signBytes));

        return sign;
    }

    /**
     * 方法描述：签名
     * @param: String content, PrivateKey key, String charsetSet
     * @return: String
     * @version: 1.0
     * @time: 2011-12-7 上午9:12:48
     */
    public static String sign(String content, PrivateKey key, String charsetSet) throws Exception {
        PrivateKey prikey = key;
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(prikey);
        signature.update(content.getBytes(charsetSet));
        byte[] signBytes = signature.sign();
        String sign = new String(Base64.encodeBase64(signBytes));
        return sign;
    }

    /**
     * 方法描述：验签
     * @param: String content, String sign,PublicKey key,String charsetSet
     * @return: boolean
     * @version: 1.0
     * @time: 2011-12-7 上午9:28:04
     */
    public static boolean verifySign(String content, String sign, PublicKey key, String charsetSet) throws Exception {
        PublicKey pubkey = key;
        byte[] signed = Base64.decodeBase64(sign.getBytes());
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubkey);
        signature.update(content.getBytes(charsetSet));
        return signature.verify(signed);
    }


    /**
     * 方法描述：验签
     * @param: String content, String sign,PublicKey key
     * @return: boolean
     * @version: 1.0
     * @time: 2011-12-7 上午9:25:05
     */
    public static boolean verifySign(String content, String sign, PublicKey key) throws Exception {
        PublicKey pubkey = key;
        byte[] signed = Base64.decodeBase64(sign.getBytes());
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubkey);
        signature.update(content.getBytes("UTF-8"));
        return signature.verify(signed);
    }

    /**
     * RSA公钥加密
     *
     * @param str
     *            加密字符串
     * @param publicKey
     *            公钥
     * @return 密文
     * @throws Exception
     *             加密过程中的异常信息
     */
    public static String encrypt( String str, String publicKey ) throws Exception{
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        byte[] data = str.getBytes("UTF-8");
        byte[] dataReturn = new byte[0];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i += 100) {
            byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(data, i, i + 100));
            sb.append(new String(doFinal));
            dataReturn = ArrayUtils.addAll(dataReturn, doFinal);
        }
        String outStr = Base64.encodeBase64String(dataReturn);
        return outStr;
    }

    /**
     * RSA私钥解密
     *
     * @param str
     *            加密字符串
     * @param privateKey
     *            私钥
     * @return 铭文
     * @throws Exception
     *             解密过程中的异常信息
     */
    public static String decrypt(String str, String privateKey) throws Exception{
        //64位解码加密后的字符串
        byte[] data = Base64.decodeBase64(str.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
//        PKCS12KeyStore keyStore = new PKCS12KeyStore();
//        java.security.cert.Certificate certificate = keyStore.engineGetCertificate(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        byte[] dataReturn = new byte[0];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i += 128) {
            byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(data, i, i + 128));
            sb.append(new String(doFinal));
            dataReturn = ArrayUtils.addAll(dataReturn, doFinal);
        }
        String outStr = new String(dataReturn);
        return outStr;
    }

    public static void test(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, Exception, Exception, Exception {
        String sr = "dasasdas";
//        initCert("/Users/richard/work/study/key/1912700web.pkcs12", "1", "*****", "/Users/richard/work/study/key/1912700web.key");


        String msg = sign(sr, privateKey,"utf-8");
        LOGGER.info("{}", msg);

        String publickey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDhaRco+RYyPOmnTmT+YO6svwP9bZoNY0mWm4/saGfRbWVeIleoxOKMII7S3/ZG1816pB2OJ04hT0DCad/kcPdyh1uCgISKQa5mhZfnzuNEV4P+IJM5GITf7h6PRIhOZD+XwpZbjYKyon+NJQyUdNtQslnJ+dOM8Gi59o7aGYXtAQIDAQAB";

        PublicKey pubk = initPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCp25k4FVKXP8uhTCykknGKRWpNWonmBWuqFx2ZdWJ+pzw9ndg957X8kAXSeyii0Bef2hXWRJwdveY//XuEdRRuQr99ycOHuiSwj4Mt6S/nKf7unDWKBeVEGcSXBQNQEciiCFOVb7stSkqQ2aBms6uHQexeTjz7MFKsXT3cFBwCpwIDAQAB");

        boolean b = verifySign(sr, msg, pubk);

        LOGGER.info("{}", b);
    }
    
    public static void main(String[] args) {
        String pubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDIsKqc264LCRedTeWPTAjxqCAJyShT8akFfVvO" +
            "UeO4c/e1eF7Sgc7J15m1E1uioZh/tm4DK2xKye/UbF3ABMmlPSBt4dmanxYmHQnVoAMTIf5j/WB0" +
            "WT4h94E5IhHXjynuSNOCJWq/1dwd+egdAkAq0OJkQpNT+B8iQCa1nle2mQIDAQAB";
        String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMiwqpzbrgsJF51N5Y9MCPGoIAnJ" +
            "KFPxqQV9W85R47hz97V4XtKBzsnXmbUTW6KhmH+2bgMrbErJ79RsXcAEyaU9IG3h2ZqfFiYdCdWg" +
            "AxMh/mP9YHRZPiH3gTkiEdePKe5I04Ilar/V3B356B0CQCrQ4mRCk1P4HyJAJrWeV7aZAgMBAAEC" +
            "gYABzbyqzjqhNMK2kjWm9Qr7iQRIsjsfjgNz3i2ODCRcywYF5oALqkj+fWaPPw5KxuME+tHAS4qc" +
            "nXX/Vb399M0PIAtnn56XOEdJcO3keYDrUxx1h4zR/YIrRwDxOuSYASz+J38/5WIE7goEngTYWqxo" +
            "Ckorn/rHTYP3RpjyDj1nAQJBAOXRhvwLre+ZdGH8dd+4EMSGngCLBcA5FUg5/mlA7lIX2CfsuSu3" +
            "yurTMTTQg5lEy/zyiJF+5TjjKSjhhBijX6ECQQDfjaI2/O9ZpM10D85pMeAEBuyqiSLUD5LpbIGj" +
            "mHP8vu1+m2wfjJKqTAINCmXl04IqVngYWPtw4hgjx0zS3tP5AkAWJ8LpnX6hMJwFrj722FnVeFzX" +
            "FI/zXg1tLxoVr4rKIz5Vav7W9hr71GxT+R4Wsokv03nS/PbAKzYWmNcRQP7BAkEAsoJ6gcY/qil7" +
            "Fa55eG6nHxAYc6TF9ufovBQ1ANl/5Nbg9jRNhnPNucav7JHdBER8sCoUA/0p6t3fwNIoekoMoQJB" +
            "AJDUgH/sDdRTqF5UNA5CZXr8EtNsftR+dHdVetAc1M7LssAsHTvGJ57Ab1qw8wVxxeR47ER0RQlV" +
            "IctckZQxMBA=";
        String plainText = pubKey; // "hello";
        LOGGER.info("plain  text is {}", plainText);
        try {
            String encrypt = encrypt(plainText, pubKey);
            String decrypt = decrypt(encrypt, privateKey);
            LOGGER.info("decryptText is {}, {}", decrypt, plainText.equals(decrypt));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}