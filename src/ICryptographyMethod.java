import java.security.Key;

interface ICryptographyMethod {
    String encrypt(String message, Key key);
    String decrypt(String data, Key key);

    String encrypt(String message);
    String decrypt(String data);

    void init();
}
