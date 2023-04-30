package ncl.csc8019.group12.utils;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * encrypt and decrypt
 * code partly from  <a href="https://www.jianshu.com/p/f7d375b199fa">...</a>
 */
public class ConfuseUtil {

    /**
     * Key pair
     * index0: public key
     * index1: private key
     */
    private static final String[] PAIR = new String[]{
            "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBANRmCfv932yNcEc48eL3HwiUYvE3zToTWIV36WE0E48BeAjoGTpGB0/vctaWv6yVq+WkHYIiCPEZKdbgqGdA788CAwEAAQ==",
            "MIIBVgIBADANBgkqhkiG9w0BAQEFAASCAUAwggE8AgEAAkEA1GYJ+/3fbI1wRzjx4vcfCJRi8TfNOhNYhXfpYTQTjwF4COgZOkYHT+9y1pa/rJWr5aQdgiII8Rkp1uCoZ0DvzwIDAQABAkEArHvU3dvT5c6SgHRBEqTGgewm0WcUdXYia/juAG8O9xCqOgF+F3ou8Wb4hSglHo4iHZyps8PzKqSCCmHQqiPOsQIhAPN15+SpR3qqDwmum1mLLPkIQvaVQYoWsSsjXjtw0J73AiEA31aSjZjlY/EaSCYAeiRgKFChnaz+yQXELYTMijfUh+kCIQCDZPz0Uc/8Cc/3376EAzMQbD8HqeInTrvrljkDR42wXQIgIatTfLVIH8EBTU8tUzyYontFYhfqcTPFV3dlvpCcIcECIQC+I634Q+n2mJTLnS4Hu5BacgTgCXbhb6frm0U9G/JY9Q=="
    };


    private final static Cipher CIPHER;
    private final static Cipher DECIPHER;
    private final static MessageDigest MD5_CIPHER;

    static {
        //public key
        byte[] publicKeyBash64 = Base64.decodeBase64(PAIR[0]);

        //private key
        byte[] privateKeyBash64 = Base64.decodeBase64(PAIR[1]);

        try {
            RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKeyBash64));
            CIPHER = Cipher.getInstance("RSA");
            CIPHER.init(Cipher.ENCRYPT_MODE, pubKey);

            PrivateKey priKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privateKeyBash64));
            DECIPHER = Cipher.getInstance("RSA");
            DECIPHER.init(Cipher.DECRYPT_MODE, priKey);

            MD5_CIPHER = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static String encryptByRSA(String str) throws Exception {

        return Base64.encodeBase64String(CIPHER.doFinal(str.getBytes(StandardCharsets.UTF_8)));
    }

    public static String encryptByRSA(Long number) throws Exception {
        return encryptByRSA(number.toString());
    }

    public static String decryptByRSA(String str) throws Exception {
        byte[] inputByte = Base64.decodeBase64(str.getBytes(StandardCharsets.UTF_8));

        return new String(DECIPHER.doFinal(inputByte));
    }

    public static Long decryptToLongByRSA(String str) throws Exception {
        return Long.valueOf(decryptByRSA(str));
    }

    public static String md5(String str) {
        byte[] bytes = MD5_CIPHER.digest(str.getBytes());
        StringBuilder stringJoiner = new StringBuilder();
        for (byte b : bytes) {
            stringJoiner.append(Integer.toHexString((b & 0xFF) | 0x100), 1, 3);
        }
        return stringJoiner.toString();
    }
}
