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
        Logger.log("Connected: " + socket + "\n");
        try {
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            if (!connectionPolicy.handshake(socket)) {
                Logger.log("Failed to perform handshake." + "\n");
            } else {
                while (in.hasNextLine()) {
                    data = connectionPolicy.cryptographyMethod.decrypt(in.nextLine());
//                    System.out.println(data);
                    Message message = new Message(data);
                    message.unpackData();
                    out.println(connectionPolicy.cryptographyMethod.encrypt(message.getTask().execute()));
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