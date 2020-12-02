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
        boolean res = false;
        try {
            Scanner in = new Scanner(socket.getInputStream());

            ((SymmetricCryptographyMethod)cryptographyMethod).setIV(in.nextLine());

            Logger.log("Done" + "\n");
            res = true;
            
        } catch (IOException e) {
            Logger.log("Failed" + "\n");
        }
        return res;
    }

}
