public abstract class CryptographyMethod {

    protected byte [] encryptionKey;
    protected byte [] decryptionKey;
    protected byte [] initialVector;

    public CryptographyMethod(byte[] encryptionKey , byte[] decryptionKey , byte[] initialVector){
        this.encryptionKey = encryptionKey;
        this.decryptionKey = decryptionKey;
        this.initialVector = initialVector;
    }

    public abstract byte [] encrypt(Message message);
    public abstract Message decrypt(byte[] data);

}
