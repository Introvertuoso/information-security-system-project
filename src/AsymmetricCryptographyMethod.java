//TODO: [KHALED] Your code here
public class AsymmetricCryptographyMethod implements ICryptographyMethod {
    byte [] encryptionKey;
    byte [] decryptionKey;

    public String encrypt(Message message) {
        System.out.print("Encrypting asymmetrically...");
        System.out.println("Done");
        return null;
    }

    public Message decrypt(String data) {
        System.out.print("Decrypting asymmetrically...");
        System.out.println("Done");
        return null;
    }

    public void init() {
        System.out.print("Initializing asymmetric encryption...");
        System.out.println("Done");
    }
}
