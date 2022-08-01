package richard.test.encrypt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;


/**
 * AES 对称加密.
 */
public class EncryptAES {

    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptAES.class);

    //KeyGenerator 提供对称密钥生成器的功能，支持各种算法
    private KeyGenerator keyGenerator;
    //SecretKey 负责保存对称密钥
    private SecretKey key;
    //Cipher负责完成加密或解密工作
    private Cipher cipher;
    //该字节数组负责保存加密的结果
    private byte[] cipherByte;

    public EncryptAES() throws NoSuchAlgorithmException, NoSuchPaddingException{
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
        //实例化支持DES算法的密钥生成器(算法名称命名需按规定，否则抛出异常)
        keyGenerator = KeyGenerator.getInstance("AES");
        //生成密钥
        key = keyGenerator.generateKey();
        LOGGER.info("key is {}", java.util.Base64.getEncoder().encode(key.getEncoded()) );
        //生成Cipher对象,指定其支持的DES算法
        cipher = Cipher.getInstance("AES");
    }

    /**
     * 对字符串加密
     */
    public byte[] encrypt(String str) throws InvalidKeyException,
        IllegalBlockSizeException, BadPaddingException {
        // 根据密钥，对Cipher对象进行初始化，ENCRYPT_MODE表示加密模式
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] src = str.getBytes();
        // 加密，结果保存进cipherByte
        cipherByte = cipher.doFinal(src);
        return cipherByte;
    }

    /**
     * 对字符串解密
     */
    public byte[] decrypt(byte[] buff) throws InvalidKeyException,
        IllegalBlockSizeException, BadPaddingException {
        // 根据密钥，对Cipher对象进行初始化，DECRYPT_MODE表示加密模式
        cipher.init(Cipher.DECRYPT_MODE, key);
        cipherByte = cipher.doFinal(buff);
        return cipherByte;
    }

    /**
     * 主函数
     */
    public static void main(String[] args) throws Exception {
        EncryptAES de1 = new EncryptAES();
        String msg ="You are not alone.";
        byte[] enContent = de1.encrypt(msg);
        byte[] deContent = de1.decrypt(enContent);
        System.out.println("明文是:" + msg);
        System.out.println("加密后:" + new String(enContent));
        System.out.println("解密后:" + new String(deContent));
    }

}