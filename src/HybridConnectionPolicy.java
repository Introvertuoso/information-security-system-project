import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.Key;
import java.util.Scanner;

//TODO: [ABDALLAH] Your code here
public class HybridConnectionPolicy extends AsymmetricConnectionPolicy {
    @Override
    public void init() {
        Logger.log("Initializing hybrid connection...");
        cryptographyMethod = new AsymmetricCryptographyMethod();
        cryptographyMethod.init();
        Logger.log("Done" + "\n");
    }

    @Override
    public boolean handshake(Socket socket) {
        Logger.log("Performing handshake...");

        try {
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            Pair<Key,String> keys = generateKeyPair();
            Key privateKey = keys.getKey(); //generate the public key
            String publicKey = keys.getValue() ; //generate the public key

            String clientPublicKey = in.nextLine() ;

            out.println(publicKey);

            ((AsymmetricCryptographyMethod) cryptographyMethod).setEncryptionKey(clientPublicKey);
            ((AsymmetricCryptographyMethod) cryptographyMethod).setDecryptionKey(privateKey);

            String sessionKey = generateKey(128); //generate session key
            String IV = generateKey(128); //generate IV key

            out.println(cryptographyMethod.encrypt(sessionKey));
            out.println(cryptographyMethod.encrypt(IV));

            cryptographyMethod = new SymmetricCryptographyMethod(sessionKey, IV);

        } catch (IOException e) {
            e.printStackTrace();
        }

        Logger.log("Done" + "\n");
        return false;
    }
}
