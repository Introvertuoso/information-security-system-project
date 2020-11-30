public class AsymmetricConnectionPolicy extends ConnectionPolicy {

    public AsymmetricConnectionPolicy(CryptographyMethod cryptographyMethod){
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
