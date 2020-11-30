//TODO: [NADER] Your code here
public class SymmetricConnectionPolicy extends ConnectionPolicy {
    @Override
    public void init() {
        System.out.print("Initializing symmetric connection...");
        this.cryptographyMethod = new SymmetricCryptographyMethod();
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
