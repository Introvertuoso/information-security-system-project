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

//TODO: [KHALED] Your code here
public class AsymmetricCryptographyMethod implements ICryptographyMethod {
    String encryptionKey;
    Key decryptionKey;
    Cipher cipher ;

    @Override
    public String encrypt(String data) {
        Logger.log("Encrypting asymmetrically...");
        try {
            cipher.init(Cipher.ENCRYPT_MODE,new SecretKeySpec(encryptionKey.getBytes(),0,encryptionKey.getBytes().length, "DES"));
            return Arrays.toString(cipher.doFinal(data.getBytes()));
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        Logger.log("Done" + "\n");
        return null;
    }

    @Override
    public String decrypt(String data) {
        Logger.log("Decrypting asymmetrically...");
        try {
            cipher.init(Cipher.DECRYPT_MODE, decryptionKey);
            byte[] utf8 = cipher.doFinal(data.getBytes());
            return new String(utf8, StandardCharsets.UTF_8);
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }
        Logger.log("Done" + "\n");
        return null;
    }

    public void init() {
        Logger.log("Initializing asymmetric encryption...");
        try {
            cipher = Cipher.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        Logger.log("Done" + "\n");
    }

    public void setKeys(String encryptionKey , Key decryptionKey){
        this.encryptionKey = encryptionKey;
        this.decryptionKey = decryptionKey;
    }


}
