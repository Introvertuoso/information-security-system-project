import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

            this.methodUsedInHandshake = cryptographyMethod;
            cryptographyMethod = new SymmetricCryptographyMethod(sessionKey, IV);
            cryptographyMethod.init();

            res = true;

        } catch (IOException e) {
            Logger.log(e.getMessage());
        }
        return res;
    }

    @Override
    public boolean validate(Message message) {
        Logger.log("Validating signature...");
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(message.getTask().toString().getBytes(StandardCharsets.UTF_8));

            String contentDigest = bytesToHex(encodedhash);
            String signatureDigest = methodUsedInHandshake.decrypt(message.getSignature(),
                    AsymmetricCryptographyMethod.loadPublicKey(
                            ((AsymmetricCryptographyMethod) methodUsedInHandshake).getEncryptionKey())
            );

            if(contentDigest.equals(signatureDigest)) {
                return true;
            }
        } catch (NoSuchAlgorithmException e) {
            Logger.log(e.getMessage());
        }

        return false;
    }

    @Override
    public boolean sign(Message message) {
        Logger.log("Signing message...");
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] contentDigestBytes = digest.digest(message.getTask().toString().getBytes(StandardCharsets.UTF_8));
            String contentDigest = bytesToHex(contentDigestBytes);
            String signature = methodUsedInHandshake.encrypt(contentDigest,
                    AsymmetricCryptographyMethod.loadPrivateKey(
                            ((AsymmetricCryptographyMethod) methodUsedInHandshake).getDecryptionKey())
            );
            message.setSignature(signature);

        } catch (NoSuchAlgorithmException e) {
            Logger.log(e.getMessage());
        }

        return true;
    }


    @Override
    public String getClientPublicKey() {
        return ((AsymmetricCryptographyMethod)this.methodUsedInHandshake).getEncryptionKey();
    }
}
