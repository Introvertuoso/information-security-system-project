import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SymmetricConnectionPolicy extends ConnectionPolicy {
    @Override
    public void init() {
        Logger.log("Initializing symmetric connection...");
        this.cryptographyMethod = new SymmetricCryptographyMethod();
        this.cryptographyMethod.init();
        Logger.log("Done" + "\n");
    }

    @Override
    public boolean handshake(Socket socket) {
        Logger.log("Performing handshake...");
        try {
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // This should be sent by Client since Client initiates the connection
            String IV = generateKey(128); //generate IV key
            out.println(IV);
            ((SymmetricCryptographyMethod)cryptographyMethod).setIV(IV);

            Logger.log("Done" + "\n");
            
        } catch (IOException e) {
            Logger.log("Failed" + "\n");
        }
        return false;
    }

}
