import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// nc localhost 11111
public class Server {
    public static void main(String[] args) throws IOException {
        Logger.start();
        try (ServerSocket listener = new ServerSocket(11111)) {
            Logger.log("The server is running...\n");
            Pair<String, String> publicPrivateKeyPair = generateKeyPair();
            Logger.log("");
            ExecutorService pool = Executors.newFixedThreadPool(20);
            while (true) {
                Socket socket = listener.accept();
                Logger.log("Connection accepted.");
                ConnectionPolicy connectionPolicy = new HybridConnectionPolicy();
                if (connectionPolicy instanceof AsymmetricConnectionPolicy) {
                    ((AsymmetricConnectionPolicy) connectionPolicy).setPublicKey(publicPrivateKeyPair.getKey());
                    ((AsymmetricConnectionPolicy) connectionPolicy).setPrivateKey(publicPrivateKeyPair.getValue());
                }
                pool.execute(new ConnectionHandler(socket, connectionPolicy));
            }
        }
    }

    public static Pair<String, String> generateKeyPair() {
        Logger.log("Obtaining key pair...");

        String content = "";
        try {
            content = Files.readString(Path.of("keys.txt"));
        } catch (IOException e) {
            Logger.log(e.getMessage());
        }
        if (!content.equals("")) {
            String[] keys = content.split("\0", 2);
            return new Pair<>(keys[0], keys[1]);
        }

        KeyPairGenerator kpg;
        String publicKey = null;
        String privateKey = null;
        try {
            kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024);
            KeyPair kp = kpg.generateKeyPair();
            publicKey = Base64.getEncoder().encodeToString(kp.getPublic().getEncoded());
            privateKey = Base64.getEncoder().encodeToString(kp.getPrivate().getEncoded());

            FileWriter writer = new FileWriter("keys.txt");
            writer.write(String.join("\0", publicKey, privateKey));
            writer.close();

            return new Pair<String, String>(publicKey, privateKey);

        } catch (Exception e) {
            Logger.log(e.getMessage());
        }
        return new Pair<>("", "");
    }
}
