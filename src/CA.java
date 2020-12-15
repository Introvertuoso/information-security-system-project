import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CA {
    private String host;
    private int port;
    private String publicKey;

    public CA(String path){
        try {
            List<String> CA_info  = Files.readAllLines(Path.of(path));
            host = CA_info.get(0);
            port = Integer.valueOf(CA_info.get(1));
            publicKey = CA_info.get(2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
