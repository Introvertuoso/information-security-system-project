import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;

//TODO: [NADER] Your code here
public class AsymmetricConnectionPolicy extends ConnectionPolicy {
    @Override
    public void init() {
        Logger.log("Initializing asymmetric connection...");
        this.cryptographyMethod = new AsymmetricCryptographyMethod();
        this.cryptographyMethod.init();
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

            ((AsymmetricCryptographyMethod) cryptographyMethod).setKeys(clientPublicKey,privateKey);

        } catch (IOException e) {
            e.printStackTrace();
        }

        Logger.log("Done" + "\n");
        return false;
    }

    public Pair<Key,String> generateKeyPair(){

        KeyPairGenerator kpg;
        Key decryptionKey = null;
        String publicKey = "";
        try {
            kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            KeyPair kp = kpg.generateKeyPair();
            decryptionKey = kp.getPrivate();
            publicKey = Base64.getMimeEncoder().encodeToString( kp.getPublic().getEncoded());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return new Pair<Key,String>(decryptionKey,publicKey) ;

    }


}
