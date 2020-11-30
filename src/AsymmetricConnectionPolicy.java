//TODO: [NADER] Your code here
public class AsymmetricConnectionPolicy extends ConnectionPolicy {
    @Override
    public void init() {
        System.out.print("Initializing asymmetric connection...");
        this.cryptographyMethod = new AsymmetricCryptographyMethod();
        this.cryptographyMethod.init();
        System.out.println("Done");
    }

    @Override
    public boolean handshake() {
        System.out.print("Performing handshake...");
        System.out.println("Done");
        return false;
    }
}
