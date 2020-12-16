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
    }

    @Override
    public boolean handshake(Socket socket) {
        Logger.log("Performing handshake...");
        boolean res = false;
        try {
            Scanner in = new Scanner(socket.getInputStream());

            ((SymmetricCryptographyMethod)cryptographyMethod).setIV(in.nextLine());

            res = true;
            
        } catch (IOException e) {
            Logger.log(e.getMessage());
        }
        return res;
    }

    @Override
    public boolean validate(Message message) {
        return true;
    }

    @Override
    public boolean validate(Certificate certificate) {
        return false;
    }


    @Override
    public boolean sign(Message message) {
        return true;
    }

    @Override
    public boolean sign(Certificate certificate) {
        return true;
    }

    @Override
    public String getClientPublicKey() {
        return null;
    }
}
