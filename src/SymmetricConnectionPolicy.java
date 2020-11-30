public class SymmetricConnectionPolicy extends ConnectionPolicy {

    public SymmetricConnectionPolicy(CryptographyMethod cryptographyMethod){
        super(cryptographyMethod);
    }
    @Override
    public void init() {

    }

    @Override
    public boolean handshake() {
        return false;
    }
}
