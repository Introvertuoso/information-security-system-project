import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class AsymmetricCryptographyMethod implements ICryptographyMethod {
    String encryptionKey;
    Key decryptionKey;
    Cipher cipher;

    public void init() {
        Logger.log("Initializing asymmetric encryption...");
        try {
            cipher = Cipher.getInstance("RSA");
            Logger.log("Done" + "\n");

        } catch (Exception e) {
            Logger.log("Failed" + "\n");
        }
    }

    @Override
    public String encrypt(String data) {
        Logger.log("Encrypting asymmetrically...");
        try {
            // TODO: Fix this it doesn't work.
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(
                            encryptionKey.getBytes(),
                            0,
                            encryptionKey.getBytes().length,
                            "DES"
                    )
            );
            Logger.log("Done" + "\n");
            return Arrays.toString(cipher.doFinal(data.getBytes()));

        } catch (Exception e) {
            Logger.log("Failed" + "\n");
        }
        return null;
    }

    @Override
    public String decrypt(String data) {
        Logger.log("Decrypting asymmetrically...");
        try {
            // TODO: Check if this works after fixing the above
            cipher.init(Cipher.DECRYPT_MODE, decryptionKey);
            byte[] utf8 = cipher.doFinal(data.getBytes());
            
            Logger.log("Done" + "\n");
            return new String(utf8, StandardCharsets.UTF_8);

        } catch (Exception e) {
            Logger.log("Failed" + "\n");
        }
        return null;
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }
    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }
    public Key getDecryptionKey() {
        return decryptionKey;
    }
    public void setDecryptionKey(Key decryptionKey) {
        this.decryptionKey = decryptionKey;
    }
    public Cipher getCipher() {
        return cipher;
    }
    public void setCipher(Cipher cipher) {
        this.cipher = cipher;
    }
}
