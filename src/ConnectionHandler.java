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
                        Certificate clientCertificate = message.getCertificate();
                        if (!this.connectionPolicy.validate(clientCertificate)) {
                            Logger.log("Client certificate invalid.");
                        } else {
                            Pair<String, Certificate> execution = message.getTask().execute(clientCertificate);
                            clientCertificate = execution.getValue();
                            String executionOutput = execution.getKey();
                            this.connectionPolicy.sign(clientCertificate);
                            Message response = new Message(
                                    new Task(executionOutput, "", ""),
                                    clientCertificate,
                                    null
                            );
                            this.connectionPolicy.sign(response);
                            response.packData();
                            out.println(connectionPolicy.cryptographyMethod.encrypt(response.getData()));
                            Logger.log("Response sent.");

                            if (executionOutput.equals(Logger.TERMINATE)) {
                                throw new Exception("");
                            }
                        }
                        Logger.log("");
                    }
                }
            }

        } catch (Exception e) {
            Logger.log(e.getMessage());
        } finally {
            try {
                socket.close();
                Logger.log("Connection terminated.\n");

            } catch (IOException e) {
                Logger.log("Connection termination failed.\n");
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