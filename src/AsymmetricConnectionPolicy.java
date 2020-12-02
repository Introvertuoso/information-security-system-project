import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.*;
import java.util.Base64;
import java.util.Scanner;

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
        boolean res = false;
        try {
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            Pair<String, String> keys = generateKeyPair();
            String publicKey = keys.getKey();         //generate the public key
            String privateKey = keys.getValue();     //generate the private key

            String clientPublicKey = in.nextLine();
            out.println(publicKey);

            ((AsymmetricCryptographyMethod) cryptographyMethod).setEncryptionKey(clientPublicKey);
            ((AsymmetricCryptographyMethod) cryptographyMethod).setDecryptionKey(privateKey);
            
            Logger.log("Done" + "\n");
            res = true;

        } catch (IOException e) {
            Logger.log("Failed" + "\n");
        }
        return res;
    }

    public Pair<String, String> generateKeyPair(){
        Logger.log("Generating key pair...");
        KeyPairGenerator kpg;
        String publicKey = null;
        String privateKey = null;
        try {
            kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024);
            KeyPair kp = kpg.generateKeyPair();
            publicKey = Base64.getEncoder().encodeToString(kp.getPublic().getEncoded());
            privateKey = Base64.getEncoder().encodeToString(kp.getPrivate().getEncoded());
            Logger.log("Done" + "\n");
            return new Pair<String, String>(publicKey, privateKey);

        } catch (Exception e) {
            Logger.log("Failed" + "\n");
        }
        return null;
    }


}
