package com.kltrq.comon.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * 1.生成私钥文件 1.key文件
 * openssl pkcs12 -in apple_payment.p12 -nocerts -nodes -out 1.key
 * 2.导出私钥
 * openssl rsa -in 1.key -out privateKey.pem
 * writing RSA key
 * 3.导出公钥
 * openssl rsa -in 1.key -pubout -out publicKey.pem
 */
public class RSAUtl {


    public static final String PUB_KY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDIsKqc264LCRedTeWPTAjxqCAJyShT8akFfVvO" +
        "UeO4c/e1eF7Sgc7J15m1E1uioZh/tm4DK2xKye/UbF3ABMmlPSBt4dmanxYmHQnVoAMTIf5j/WB0" +
        "WT4h94E5IhHXjynuSNOCJWq/1dwd+egdAkAq0OJkQpNT+B8iQCa1nle2mQIDAQAB";
    static final String PRI_KY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMiwqpzbrgsJF51N5Y9MCPGoIAnJ" +
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

    public static String encrypt( String str, String pubKy) throws Exception{
        byte[] decoded = Base64.getDecoder().decode(pubKy);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
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
        String outStr = Base64.getEncoder().encodeToString(dataReturn);
        return outStr;
    }

    public static String decrypt(String str, String priKy) throws Exception{
        byte[] data = Base64.getDecoder().decode(str.getBytes("UTF-8"));
        byte[] decoded = Base64.getDecoder().decode(priKy);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
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

}