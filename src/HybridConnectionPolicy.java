import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.Key;
import java.util.Scanner;

public class HybridConnectionPolicy extends AsymmetricConnectionPolicy {
    ICryptographyMethod methodUsedInHandshake;

    @Override
    public void init() {
        Logger.log("Initializing hybrid connection...");
        cryptographyMethod = new AsymmetricCryptographyMethod();
        cryptographyMethod.init();
    }

    @Override
    public boolean handshake(Socket socket) {
        boolean res = false;

        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            
            super.handshake(socket);

            String sessionKey = generateKey(128); //generate session key
            String IV = generateIV(); //generate IV key

            out.println(cryptographyMethod.encrypt(sessionKey));
            out.println(cryptographyMethod.encrypt(IV));

            methodUsedInHandshake = cryptographyMethod;
            cryptographyMethod = new SymmetricCryptographyMethod(sessionKey, IV);
            cryptographyMethod.init();

            res = true;

        } catch (IOException e) {
            Logger.log(e.getMessage());
        }
        return res;
    }

    @Override
    public String getClientPublicKey() {
        return ((AsymmetricCryptographyMethod)this.methodUsedInHandshake).getEncryptionKey();
    }
}
