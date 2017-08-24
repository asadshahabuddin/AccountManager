package org.as.am.encrypt;

import java.security.Key;
import javax.crypto.Cipher;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import javax.crypto.spec.SecretKeySpec;

import static org.as.am.properties.AESProperties.*;

public class AES {
    /**
     * Generate symmetric key
     * @return
     *     Symmetric key
     */
    private static Key generateKey() {
        return new SecretKeySpec(SECRET_KEY, ALGORITHM_NAME);
    }

    /**
     * Encrypt plain text
     * @param plainText
     *     Plain text
     * @return
     *     The equivalent cipher text
     */
    public static String encrypt(String plainText) {
        if(plainText == null) {
            return "";
        }

        byte[] encrypted = null;
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance(ALGORITHM_NAME);
            c.init(Cipher.ENCRYPT_MODE, key);
            encrypted = c.doFinal(plainText.getBytes());
        } catch(Exception e) {
            e.printStackTrace();
        }
        return encrypted == null ? "" : new BASE64Encoder().encode(encrypted);
    }

    /**
     * Decrypt cipher text
     * @param cipherText
     *     Cipher text
     * @return
     *     The equivalent plain text
     */
    public static String decrypt(String cipherText) {
        if(cipherText == null) {
            return "";
        }

        byte[] decrypted = null;
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance(ALGORITHM_NAME);
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] decoded = new BASE64Decoder().decodeBuffer(cipherText);
            decrypted = c.doFinal(decoded);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return decrypted == null ? "" : new String(decrypted);
    }
}