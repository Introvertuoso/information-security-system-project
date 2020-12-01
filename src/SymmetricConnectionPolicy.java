import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

//TODO: [NADER] Your code here
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

            String IV = generateKey(128) ; //generate IV key
            out.println(cryptographyMethod.encrypt(IV));
            ((SymmetricCryptographyMethod)cryptographyMethod).setIV(IV);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Logger.log("Done" + "\n");
        return false;
    }

}
