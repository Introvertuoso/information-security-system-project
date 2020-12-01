import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;

public class Client {
    private static final String key = "aesEncryptionKey";
    private static final String initVector = "encryptionIntVec";

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println();
        }
        try (Socket socket = new Socket("127.0.0.1", 11111)) {
            System.out.println("Enter your commands below (Ctrl+D or Ctrl+C to quit)");
            Scanner scanner = new Scanner(System.in);
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            while (scanner.hasNextLine()) {
                out.println(scanner.nextLine());    // Sends user input on the socket output stream
                System.out.println(decrypt(in.nextLine()));  // Displays server response from socket input stream
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static String decrypt(String data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] encrypted = Base64.getDecoder().decode(data);

        IvParameterSpec iv = new IvParameterSpec(initVector.getBytes());
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

        byte[] decrypted = cipher.doFinal(encrypted);

        return new String(decrypted, StandardCharsets.UTF_8);
    }
}