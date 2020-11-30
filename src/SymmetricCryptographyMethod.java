public class SymmetricCryptographyMethod extends CryptographyMethod {

    public SymmetricCryptographyMethod(byte[] encryptionKey , byte[] decryptionKey , byte[] initialVector){
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
