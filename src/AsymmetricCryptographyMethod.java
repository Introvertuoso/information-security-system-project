//TODO: [KHALED] Your code here
public class AsymmetricCryptographyMethod implements ICryptographyMethod {
    byte [] encryptionKey;
    byte [] decryptionKey;

    public String encrypt(Message message) {
        Logger.log("Encrypting asymmetrically...");
        Logger.log("Done" + "\n");
        return null;
    }

    public Message decrypt(String data) {
        Logger.log("Decrypting asymmetrically...");
        Logger.log("Done" + "\n");
        return null;
    }

    public void init() {
        Logger.log("Initializing asymmetric encryption...");
        Logger.log("Done" + "\n");
    }
}
