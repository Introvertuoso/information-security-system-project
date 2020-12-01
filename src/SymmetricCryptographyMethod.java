import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

//TODO: [KHALED] Your code here
public class SymmetricCryptographyMethod implements ICryptographyMethod {
    String key;
    String IV;
    Cipher cipher;

    public SymmetricCryptographyMethod(){
    }

    public SymmetricCryptographyMethod(String key , String IV){
        this.key = key;
        this.IV = IV;
    }

    public String encrypt(String data) {
        IvParameterSpec iv = new IvParameterSpec(this.IV.getBytes());
        SecretKeySpec skeySpec = new SecretKeySpec(this.key.getBytes(), "AES");
        String s = "";

        try {
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv); // or Cipher.DECRYPT_MODE
            byte[] encrypted = cipher.doFinal(data.getBytes());

             s = Base64.getEncoder().encodeToString(encrypted);
            Logger.log(s + "\n");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return s;
    }

    public String decrypt(String data) {
        System.out.print("Decrypting symmetrically...");
        try {
            IvParameterSpec iv = new IvParameterSpec(this.IV.getBytes());
            SecretKeySpec skeySpec = new SecretKeySpec(this.key.getBytes(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.getDecoder().decode(data));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("Done");
        return null;
    }

    public void init() {
        System.out.print("Initializing symmetric encryption...");
        try {
            this.cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            this.key = "KtobShuMaKan";
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
        System.out.println("Done");
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
