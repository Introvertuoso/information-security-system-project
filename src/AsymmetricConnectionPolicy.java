import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.spec.RSAOtherPrimeInfo;
import java.util.Base64;
import java.util.Scanner;

public class AsymmetricConnectionPolicy extends ConnectionPolicy {
    protected String publicKey;
    protected String privateKey;
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

//            Pair<String, String> keys = generateKeyPair();
//            publicKey = keys.getKey();                  //generate the public key
//            privateKey = keys.getValue();               //generate the private key

            handshake("Built_in_CA", new CSR("1", "09377", publicKey));

            String clientPublicKey = in.nextLine();
            Logger.log("Performing handshake...");
            out.println(publicKey);
            out.println(certificate);

            ((AsymmetricCryptographyMethod) cryptographyMethod).setEncryptionKey(clientPublicKey);
            ((AsymmetricCryptographyMethod) cryptographyMethod).setDecryptionKey(privateKey);

            Certificate clientCertificate = new Certificate(in.nextLine());
            if (clientCertificate.getSignature().equals("unsigned") &&
                    clientCertificate.getCsr().getExtras().equals("")
            ) {
                sign(clientCertificate);
            }
            out.println(clientCertificate.toString());

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
    public boolean validate(Certificate certificate) {
        Logger.log("Validating client certificate...");
        String assumedClientPublicKey = ((AsymmetricCryptographyMethod) this.cryptographyMethod).getEncryptionKey();

        return
                verifySignatureHash(
                        certificate.getCsr().toString(), certificate.getSignature(), publicKey, cryptographyMethod
                ) && (certificate.getCsr().getPublicKey().equals(assumedClientPublicKey));
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

    @Override
    public boolean sign(Certificate certificate) {
        try {
            CSR csr = certificate.getCsr();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] contentDigestBytes = digest.digest(csr.toString().getBytes(StandardCharsets.UTF_8));
            String contentDigest = bytesToHex(contentDigestBytes);
            String signature = cryptographyMethod.encrypt(
                    contentDigest, AsymmetricCryptographyMethod.loadPrivateKey(
                            ((AsymmetricCryptographyMethod) cryptographyMethod).getDecryptionKey()
                    )
            );
            certificate.setSignature(signature);

        } catch (Exception e) {
            e.printStackTrace();
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

    public void handshake(String CA_name, CSR csr) {
        Logger.log("Performing handshake with CA...");
        try {
            CA ca = new CA("CA/" + CA_name + ".txt");
            Socket socket = new Socket(ca.getHost(), ca.getPort());

            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            out.println(csr.toString());

            certificate = new Certificate(in.nextLine());

        } catch (IOException e) {
            Logger.log(e.getMessage());
        }
    }


    public boolean verifySignatureHash(String document, String signature, String key, ICryptographyMethod method) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(document.getBytes(StandardCharsets.UTF_8));

            String contentDigest = bytesToHex(encodedhash);
            String signatureDigest = method.decrypt(signature,
                    AsymmetricCryptographyMethod.loadPublicKey(key)
            );

            if (contentDigest.equals(signatureDigest))
                return true;

        } catch (NoSuchAlgorithmException e) {
            Logger.log(e.getMessage());
        }

        return false;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public Certificate getCertificate() {
        return certificate;
    }

    public void setCertificate(Certificate certificate) {
        this.certificate = certificate;
    }
}
