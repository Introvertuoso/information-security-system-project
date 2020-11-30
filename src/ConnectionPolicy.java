public abstract class ConnectionPolicy {
    protected CryptographyMethod cryptographyMethod;


    public ConnectionPolicy(CryptographyMethod cryptographyMethod){
        this.cryptographyMethod = cryptographyMethod;
    }

    public abstract void init();
    public abstract boolean handshake();

}
