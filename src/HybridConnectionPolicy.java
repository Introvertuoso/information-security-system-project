//TODO: [ABDALLAH] Your code here
public class HybridConnectionPolicy extends ConnectionPolicy {
    @Override
    public void init() {
        Logger.log("Initializing hybrid connection...");
        Logger.log("Done" + "\n");
    }

    @Override
    public boolean handshake() {
        Logger.log("Performing handshake...");
        Logger.log("Done" + "\n");
        return false;
    }
}
