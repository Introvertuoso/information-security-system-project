import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.Key;
import java.util.Scanner;

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
        boolean res = false;

        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            
            super.handshake(socket);

            String sessionKey = generateKey(128); //generate session key
            String IV = generateKey(128); //generate IV key

            out.println(cryptographyMethod.encrypt(sessionKey));
            out.println(cryptographyMethod.encrypt(IV));

            cryptographyMethod = new SymmetricCryptographyMethod(sessionKey, IV);
            Logger.log("Done" + "\n");
            res = true;

        } catch (IOException e) {
            Logger.log("Failed" + "\n");
        }
        return res;
    }
}
