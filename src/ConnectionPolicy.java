import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.math.BigInteger;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

public abstract class ConnectionPolicy {
    protected ICryptographyMethod cryptographyMethod;

    public abstract void init();
    public abstract boolean handshake(Socket socket);

    public String generateKey(int keySize){
        Logger.log("Generating key...");
        KeyGenerator gen;
        String SK = "";
        try {
            gen = KeyGenerator.getInstance("AES");
            gen.init(keySize); /* 128-bit AES */
            SecretKey secretKey = gen.generateKey();
            byte[] bytes = secretKey.getEncoded();
            SK = String.format("%032X", new BigInteger(+1, bytes));
            Logger.log("Done" + "\n");

        } catch (NoSuchAlgorithmException e) {
            Logger.log("Failed" + "\n");
        }
        return SK;
    }
}
