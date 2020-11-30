public class Test {
    public static void main(String[] args) {
        String test = "read /repos/project/src/main.java ";
        String[] result = test.split("\0");
        System.out.println(result.length);
    }
}
