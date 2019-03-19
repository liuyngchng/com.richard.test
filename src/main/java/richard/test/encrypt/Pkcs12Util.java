package richard.test.encrypt;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Enumeration;

/**
 * Created by richard on 12/03/2019.
 * 在api项目中，我们通常是直接利用公私钥要进行数据验证的。
 如果直接得到了一个证书，需要对证书进行转换。

 格式说明

 PKCS 全称是 Public-Key Cryptography Standards ，是由 RSA 实验室与其它安全系统开发商为促进公钥密码的发展而制订的一系列标准，PKCS 目前共发布过 15 个标准。 常用的有：
 PKCS#7 Cryptographic Message Syntax Standard
 PKCS#10 Certification Request Standard
 PKCS#12 Personal Information Exchange Syntax Standard
 X.509是常见通用的证书格式。所有的证书都符合为Public Key Infrastructure (PKI) 制定的 ITU-T X509 国际标准。
 PKCS#7 常用的后缀是： .P7B .P7C .SPC
 PKCS#12 常用的后缀有： .P12 .PFX
 X.509 DER 编码(ASCII)的后缀是： .DER .CER .CRT
 X.509 PAM 编码(Base64)的后缀是： .PEM .CER .CRT
 .cer/.crt是用于存放证书，它是2进制形式存放的，不含私钥。
 .pem跟crt/cer的区别是它以Ascii来表示。
 pfx/p12用于存放个人证书/私钥，他通常包含保护密码，2进制方式
 p10是证书请求
 p7r是CA对证书请求的回复，只用于导入
 p7b以树状展示证书链(certificate chain)，同时也支持单个证书，不含私钥。
 用openssl创建CA证书的RSA密钥(PEM格式)：
 openssl genrsa -des3 -out ca.key 1024
 用openssl创建CA证书(PEM格式,假如有效期为一年)：
 openssl req -new -x509 -days 365 -key ca.key -out ca.crt -config openssl.cnf
 x509转换为pfx
 openssl pkcs12 -export -out server.pfx -inkey server.key -in server.crt
 PEM格式的ca.key转换为Microsoft可以识别的pvk格式
 pvk -in ca.key -out ca.pvk -nocrypt -topvk
 PKCS#12 到 PEM 的转换
 openssl pkcs12 -nocerts -nodes -in cert.p12 -out private.pem 验证 openssl pkcs12 -clcerts -nokeys -in cert.p12 -out cert.pem
 从 PFX 格式文件中提取私钥格式文件 (.key)
 openssl pkcs12 -in mycert.pfx -nocerts -nodes -out mycert.key
 转换 pem 到到 spc
 openssl crl2pkcs7 -nocrl -certfile venus.pem -outform DER -out venus.spc
 用 -outform -inform 指定 DER 还是 PAM 格式。例如：
 openssl x509 -in Cert.pem -inform PEM -out cert.der -outform DER
 PEM 到 PKCS#12 的转换
 openssl pkcs12 -export -in Cert.pem -out Cert.p12 -inkey key.pem
 IIS 证书
 cd c:\openssl set OPENSSL_CONF=openssl.cnf openssl pkcs12 -export -out server.pfx -inkey server.key -in server.crt
 server.key和server.crt文件是Apache的证书文件，生成的server.pfx用于导入IIS
 Convert PFX Certificate to PEM Format for SOAP
 $ openssl pkcs12 -in test.pfx -out client.pem Enter Import Password: MAC verified OK Enter PEM pass phrase: Verifying - Enter PEM pass phrase:
 DER文件（.crt .cer .der）转为PEM格式文件
 转换DER文件(一般后缀名是.crt .cer .der的文件)到PEM文件 openssl x509 -inform der -in certificate.cer -out certificate.pem 转换PEM文件到DER文件 openssl x509 -outform der -in certificate.pem -out certificate.der

 cer文件中导出公钥

 cer转pem
 openssl x509 -inform der -in ***pds.cer -out certificate.pem



 p12 文件导出公钥私钥

 生成key文件
 openssl pkcs12 -in demo.p12 -nocerts -nodes -out demo.key
 导出私钥
 openssl rsa -in demo.key -out demo_pri.pem
 导出公钥
 openssl rsa -in demo.key -pubout -out demo_pub.pem
 ---------------------
 原文：https://blog.csdn.net/qq_37049781/article/details/84837342
 * 查看 PEM 证书内容 openssl x509 -in cert.pem -text -noout
 */
public class Pkcs12Util {

    private static Logger LOGGER = LoggerFactory.getLogger(Pkcs12Util.class);

    private static PublicKey publicKey;
    private static PrivateKey privateKey;

    private static String publicKeyStr;

    private static String privateKeyStr;

    public static void initCert(String certPath, String password)throws Exception {
        KeyStore ks = KeyStore.getInstance("PKCS12");
        FileInputStream fis = new FileInputStream(certPath);
        ks.load(fis, password.toCharArray());
        LOGGER.info("证书类型___: {}", ks.getType());

        Enumeration enumas = ks.aliases();
        String aliases = null;
        while (enumas.hasMoreElements()) {
            aliases = (String) enumas.nextElement();
            LOGGER.info("证书别名Alias___: {}" ,aliases);
        }
        Certificate cert = ks.getCertificate(aliases);
        publicKey = cert.getPublicKey();
        publicKeyStr = Base64.encodeBase64String(publicKey.getEncoded());
        StringBuilder sb = new StringBuilder(publicKeyStr.length());
        sb.append("-----BEGIN PUBLIC KEY-----\r\n");
        formatKey(publicKeyStr, sb);
        sb.append("-----END PUBLIC KEY-----\r\n");
        LOGGER.info("公钥___: {}" , publicKeyStr);
        LOGGER.info("公钥(format)___:\r\n {}" ,sb.toString());

        LOGGER.info("is key entry= {}" ,ks.isKeyEntry(aliases));
        privateKey = (PrivateKey) ks.getKey(aliases, password.toCharArray());
        privateKeyStr = Base64.encodeBase64String(privateKey.getEncoded());
        StringBuilder sb1 = new StringBuilder(privateKeyStr.length());
        sb1.append("-----BEGIN RSA PRIVATE KEY-----\r\n");
        formatKey(privateKeyStr, sb1);
        sb1.append("-----END RSA PRIVATE KEY-----\r\n");
        LOGGER.info("私钥___: {}" ,privateKeyStr);
        LOGGER.info("私钥(format)___:\r\n {}" ,sb1.toString());
    }


    private static String formatKey(String key, StringBuilder sb) {
        while (key.length() > 0) {
            String tmp;
            if (key.length() > 64) {
                tmp = key.substring(0, 64);
                sb.append(tmp + "\r\n");
                key = key.substring(64);
            } else {
                tmp = key;
                sb.append(tmp + "\r\n");
                key = "";
            }
        }
        return key;
    }

    public static void test(String[] args) throws Exception {
        initCert("/Users/richard/work/study/key/1912700web.pkcs12", "****");
        String plainText = "test";
        LOGGER.info("plainText is {}", plainText);
        String sign = RSAUtils.sign(plainText, privateKey);
        LOGGER.info("signed is {}", sign);
        boolean result = RSAUtils.verifySign(plainText, sign, publicKey);
        LOGGER.info("verifySign result is {}", result);
        String encryptText = RSAUtils.encrypt(plainText, publicKeyStr);
        LOGGER.info("encrypt is {}", encryptText);
        String decryptText = RSAUtils.decrypt(encryptText, privateKeyStr);
        LOGGER.info("decrypt is {}", decryptText);
    }
}
