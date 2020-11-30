//TODO: [KHALED] Your code here
public class SymmetricCryptographyMethod implements ICryptographyMethod {
    byte [] key;

    public String encrypt(Message message) {
        System.out.print("Encrypting symmetrically...");
        System.out.println("Done");
        return null;
    }

    public Message decrypt(String data) {
        System.out.print("Decrypting symmetrically...");
        System.out.println("Done");
        return null;
    }

    public void init() {
        System.out.print("Initializing symmetric encryption...");
        System.out.println("Done");
    }
}
