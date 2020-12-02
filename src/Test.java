import javax.crypto.Cipher;
import java.util.Scanner;

public class Test {
    private static Cipher cipher;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNextLine()) {
            String temp = scanner.nextLine();
            System.out.println(temp.substring(1));
        }
    }
}
