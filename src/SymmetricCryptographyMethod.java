import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

// TODO: Find a solution for empty strings returned by encrypt and null by decrypt
public class SymmetricCryptographyMethod implements ICryptographyMethod {
    String key;
    String IV;
    Cipher cipher;

    public SymmetricCryptographyMethod() { }

    public SymmetricCryptographyMethod(String key, String IV) {
        this.key = key;
        this.IV = IV;
    }

    public void init() {
        System.out.print("Initializing symmetric encryption...");
        try {
            this.cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            this.key = "KtobShuMaKan";
            Logger.log("Done" + "\n");

        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            Logger.log("Failed" + "\n");
        }
    }

    public String encrypt(String data) {
        Logger.log("Encrypting symmetrically...");
        IvParameterSpec iv = new IvParameterSpec(this.IV.getBytes());
        SecretKeySpec skeySpec = new SecretKeySpec(this.key.getBytes(), "AES");
        String res = "";

        try {
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv); // or Cipher.DECRYPT_MODE
            byte[] encrypted = cipher.doFinal(data.getBytes());

            res = Base64.getEncoder().encodeToString(encrypted);
            Logger.log("Done" + "\n");

        } catch (Exception e) {
            Logger.log("Failed" + "\n");
        }
        return res;
    }

    public String decrypt(String data) {
        Logger.log("Decrypting symmetrically...");
        try {
            IvParameterSpec iv = new IvParameterSpec(this.IV.getBytes());
            SecretKeySpec skeySpec = new SecretKeySpec(this.key.getBytes(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.getDecoder().decode(data));

            Logger.log("Done" + "\n");
            return new String(original);

        } catch (Exception ex) {
            Logger.log("Failed" + "\n");
        }
        return null;
    }

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getIV() {
        return IV;
    }
    public void setIV(String IV) {
        this.IV = IV;
    }
    public Cipher getCipher() {
        return cipher;
    }
    public void setCipher(Cipher cipher) {
        this.cipher = cipher;
    }
}
