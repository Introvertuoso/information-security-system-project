import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

// nc localhost 11111
public class ConnectionHandler implements Runnable {
    //    private static final String initVector = "encryptionIntVec";
//    private static final String key = "aesEncryptionKey";
    public static final String currentDirectory = "files";

    private final Socket socket;
    private final ConnectionPolicy connectionPolicy;
    private String data;

    ConnectionHandler(Socket socket, ConnectionPolicy connectionPolicy) {
        this.data = "";
        this.socket = socket;
        this.connectionPolicy = connectionPolicy;
        this.connectionPolicy.init();
    }

    @Override
    public void run() {
        Logger.log("Connected: " + socket);
        try {
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            if (!connectionPolicy.handshake(socket)) {
                Logger.log("Failed to perform handshake." + "\n");
            } else {
                Logger.log("");
                while (in.hasNextLine()) {
                    Logger.log("Request received.");
                    String clientPublicKey = connectionPolicy.getClientPublicKey();
                    data = connectionPolicy.cryptographyMethod.decrypt(in.nextLine());
                    Message message = new Message(data);
                    message.unpackData();
                    if (!connectionPolicy.validate(message)) {
                        Logger.log("Signature invalid.");
                    } else {

                        String execution = message.getTask().execute(clientPublicKey);
                        Message response = new Message(
                                new Task(execution, "", ""), new Certificate("certificate"), null
                        );
                        this.connectionPolicy.sign(response);
                        response.packData();
                        out.println(connectionPolicy.cryptographyMethod.encrypt(response.getData()));
                        Logger.log("Response sent.");
                        Logger.log("");
                    }
                }
            }

        } catch (Exception e) {
            Logger.log("Error: " + socket + "\n");
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                Logger.log("Closed: " + socket + "\n");

            } catch (IOException e) {
                Logger.log("Failed to close socket.\n");
            }
        }
    }

//    private static String encrypt(String data) throws InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException {
//        IvParameterSpec iv = new IvParameterSpec(initVector.getBytes());
//        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
//
//        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
//        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv); // or Cipher.DECRYPT_MODE
//
//        byte[] encrypted = cipher.doFinal(data.getBytes());
//
//        String s = Base64.getEncoder().encodeToString(encrypted);
//        Logger.log(s + "\n");
//        return s;
//    }

}