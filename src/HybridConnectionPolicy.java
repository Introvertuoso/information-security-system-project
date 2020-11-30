//TODO: [ABDALLAH] Your code here
public class HybridConnectionPolicy extends ConnectionPolicy {
    @Override
    public void init() {
        System.out.print("Initializing hybrid connection...");
        System.out.println("Done");
    }

    @Override
    public boolean handshake() {
        System.out.print("Performing handshake...");
        System.out.println("Done");
        return false;
    }
}
