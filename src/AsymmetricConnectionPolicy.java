import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

public class AsymmetricConnectionPolicy extends ConnectionPolicy {
    protected Certificate certificate;
    @Override
    public void init() {
        Logger.log("Initializing asymmetric connection...");
        this.cryptographyMethod = new AsymmetricCryptographyMethod();
        this.cryptographyMethod.init();
    }

    @Override
    public boolean handshake(Socket socket) {
        boolean res = false;
        try {
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            Pair<String, String> keys = generateKeyPair();
            String publicKey = keys.getKey();         //generate the public key
            String privateKey = keys.getValue();     //generate the private key

            CAHandshake("Built_in_CA",new CSR("1","09377",publicKey));

            String clientPublicKey = in.nextLine();
            Logger.log("Performing handshake...");
            out.println(publicKey);

            ((AsymmetricCryptographyMethod) cryptographyMethod).setEncryptionKey(clientPublicKey);
            ((AsymmetricCryptographyMethod) cryptographyMethod).setDecryptionKey(privateKey);

            res = true;

        } catch (IOException e) {
            Logger.log(e.getMessage());
        }
        return res;
    }

    @Override
    public String getClientPublicKey() {
        return ((AsymmetricCryptographyMethod) cryptographyMethod).getEncryptionKey();
    }

    public Pair<String, String> generateKeyPair() {
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
            return new Pair<String, String>(publicKey, privateKey);

        } catch (Exception e) {
            Logger.log(e.getMessage());
        }
        return null;
    }

    @Override
    public boolean validate(Message message) {
        Logger.log("Validating signature...");
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(message.getTask().toString().getBytes(StandardCharsets.UTF_8));

            String contentDigest = bytesToHex(encodedhash);
            String signatureDigest = cryptographyMethod.decrypt(message.getSignature(),
                    AsymmetricCryptographyMethod.loadPublicKey(
                            ((AsymmetricCryptographyMethod) cryptographyMethod).getEncryptionKey())
            );

            if (contentDigest.equals(signatureDigest))
                return true;
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
            String signature = cryptographyMethod.encrypt(contentDigest,
                    AsymmetricCryptographyMethod.loadPrivateKey(
                            ((AsymmetricCryptographyMethod) cryptographyMethod).getDecryptionKey())
            );
            message.setSignature(signature);

        } catch (NoSuchAlgorithmException e) {
            Logger.log(e.getMessage());
        }

        return true;
    }

    public String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public void CAHandshake(String CA_name,CSR csr){
        try {
            System.out.println("performing CA handshake");
            CA ca  = new CA("CA/"+CA_name+".txt");
            Socket socket = new Socket(ca.getHost(),ca.getPort());

            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);


//            String encryptedCSR = cryptographyMethod.encrypt(csr.toString(),
//                    AsymmetricCryptographyMethod.loadPublicKey(ca.getPublicKey()));


            out.println(csr.toString());

            certificate = new Certificate(in.nextLine());
            System.out.println(certificate.content);

        } catch (IOException e) {
            e.printStackTrace();
        }




    }
}
