package la.niub.abcapi.invest.platform.component.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RsaUtil {

    private static final String KEY_ALGORITHM = "RSA";

    private final static String defaultPublicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJLrS7/xW+zabN+LC/HPuNJcmRO1Hf9ffWSyNfQWTF/wHt+e3z+0AKDKFz332JdozI55hOM/BHEPHB12phGqaUUCAwEAAQ==";
    private final static String defaultPrivateKey = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAkutLv/Fb7Nps34sL8c+40lyZE7Ud/199ZLI19BZMX/Ae357fP7QAoMoXPffYl2jMjnmE4z8EcQ8cHXamEappRQIDAQABAkARF/E7RtCkMRGjvKDKJJnjV8Szp0nzMV/8fEOMv59pNfCww9AmjrFYW5LlfejxmjPaDD7mFG1uX0zdUb6fzY7BAiEAwkzqGr3t9VsrTs+/dBbZODW3akhSkPa2ESML8axh/HECIQDBkqfYM8l4ybPCDKC5qWGz1JvMTNqKAzyy3Bzh79X0FQIgEJZ2agAK/6Zf5SORSbzloInddcJk4iFd28qtK123I9ECIBvY0TSVHqK1wZpk3qpW56tLJq6ZT8cS+CRy7eTC7/CRAiEAu/3Lx6EGTVQSKpzeTQ1d73y9GNAusnkmXn6fuJe0G8A=";

    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    /**
     * luoguohui
     * 2015-12-26
     * RSA加密
     */
    public static String encryptData(String data) {
        return encryptData(data,defaultPublicKey);
    }
    public static String encryptData(String data, String publicKey) {
        try {
            X509EncodedKeySpec pubX509 = new X509EncodedKeySpec(Base64.decodeBase64(publicKey.getBytes()));
            KeyFactory keyf = KeyFactory.getInstance(KEY_ALGORITHM, "BC");
            PublicKey pubKey = keyf.generatePublic(pubX509);

            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] dataToEncrypt = data.getBytes("utf-8");
            byte[] encryptedData = cipher.doFinal(dataToEncrypt);
            String encryptString = Base64.encodeBase64String(encryptedData);
            return encryptString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * luoguohui
     * 2015-12-26
     * RSA解密
     */
    public static String decryptData(String data) {
        return decryptData(data,defaultPrivateKey);
    }
    public static String decryptData(String data, String privateKey) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey.getBytes()));
            KeyFactory keyf = KeyFactory.getInstance(KEY_ALGORITHM, "BC");
            PrivateKey privKey = keyf.generatePrivate(priPKCS8);

            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privKey);
            byte[] descryptData = Base64.decodeBase64(data);
            byte[] descryptedData = cipher.doFinal(descryptData);
            String srcData = new String(descryptedData, "utf-8");
            return srcData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成key对
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static Map<String,String> genKey() throws NoSuchAlgorithmException {
        KeyPairGenerator keygen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        SecureRandom random = new SecureRandom();
        // 初始加密，512位已被破解，用1024位,最好用2048位
        keygen.initialize(1024, random);
        // 取得密钥对
        KeyPair kp = keygen.generateKeyPair();
        PrivateKey privateKey = kp.getPrivate();
        String privateKeyString = Base64.encodeBase64String(privateKey.getEncoded());
        PublicKey publicKey = kp.getPublic();
        String publicKeyString = Base64.encodeBase64String(publicKey.getEncoded());

        String testString = "testString";
        String encrypt = encryptData(testString,publicKeyString);
        if (decryptData(encrypt,privateKeyString).equals(testString)){
            System.out.println("PUBLIC_KEY: "+publicKeyString);
            System.out.println("PRIVATE_KEY: "+privateKeyString);
            Map<String,String> keyMap = new HashMap<>();
            keyMap.put("PUBLIC_KEY", publicKeyString);
            keyMap.put("PRIVATE_KEY", privateKeyString);
            return keyMap;
        }else{
            System.out.println("genKey failed");
            return null;
        }
    }
}
