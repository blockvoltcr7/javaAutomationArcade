package FileReaders;

public class getLastIndex {

    public static void main(String[] args) {
        String x = "38974298342A";

        // Get the last character using length() - 1
        char lastChar = x.charAt(x.length() - 1);
        System.out.println("Last character: " + lastChar);
    }
}