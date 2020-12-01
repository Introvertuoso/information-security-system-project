//TODO: [NADER] Your code here
public class AsymmetricConnectionPolicy extends ConnectionPolicy {
    @Override
    public void init() {
        Logger.log("Initializing asymmetric connection...");
        this.cryptographyMethod = new AsymmetricCryptographyMethod();
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
