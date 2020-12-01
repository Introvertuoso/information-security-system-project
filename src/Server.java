import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// nc localhost 11111
public class Server {
    public static void main(String[] args) throws IOException {
        Logger.start();
        try (ServerSocket listener = new ServerSocket(11111)) {
            Logger.log("The server is running..." + "\n");
            ExecutorService pool = Executors.newFixedThreadPool(20);
            while (true) {
                pool.execute(new ConnectionHandler(listener.accept()));
            }
        }
    }
}
