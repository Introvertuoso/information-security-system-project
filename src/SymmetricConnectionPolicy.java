//TODO: [NADER] Your code here
public class SymmetricConnectionPolicy extends ConnectionPolicy {
    @Override
    public void init() {
        Logger.log("Initializing symmetric connection...");
        this.cryptographyMethod = new SymmetricCryptographyMethod();
        this.cryptographyMethod.init();
        Logger.log("Done" + "\n");
    }

    @Override
    public boolean handshake() {
        Logger.log("Performing handshake...");
        Logger.log("Done" + "\n");
        return false;
    }
}
