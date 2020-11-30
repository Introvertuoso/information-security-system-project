public class AsymmetricCryptographyMethod extends CryptographyMethod {


    public AsymmetricCryptographyMethod(byte[] encryptionKey , byte[] decryptionKey , byte[] initialVector){
        super(encryptionKey,decryptionKey,initialVector);
    }

    @Override
    public byte[] encrypt(Message message) {
        return new byte[0];
    }

    @Override
    public Message decrypt(byte[] data) {
        return null;
    }
}
