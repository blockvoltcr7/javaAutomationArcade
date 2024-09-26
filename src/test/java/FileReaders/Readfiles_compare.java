package FileReaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Readfiles_compare {

    public static void main(String[] args) {
        String filePath1 = "src/test/java/FileReaders/data-file-compare1.txt";
        String filePath2 = "src/test/java/FileReaders/data-file-compare2.txt";

        try (BufferedReader reader1 = new BufferedReader(new FileReader(filePath1));
             BufferedReader reader2 = new BufferedReader(new FileReader(filePath2))) {

            String line1 = reader1.readLine();
            String line2 = reader2.readLine();
            int lineNumber = 1;

            while (line1 != null && line2 != null) {
                if (!compareLines(line1, line2)) {
                    System.out.println("Difference found at line " + lineNumber);
                    System.out.println("File1: " + line1);
                    System.out.println("File2: " + line2);
                }

                line1 = reader1.readLine();
                line2 = reader2.readLine();
                lineNumber++;
            }

            if (line1 != null || line2 != null) {
                System.out.println("Files have different lengths.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // This method compares two strings, line1 and line2, to determine if they are equivalent
    // while ignoring specific characters at certain positions (indices 6 to 9).
    // It first checks if the lengths of the two lines are the same; if not, it returns false.
    // Then, it iterates through each character of the lines. For each index, if the index is between
    // 6 and 9 (inclusive), it skips the comparison for that index, treating those characters as wildcards.
    // If any other characters at the same index in line1 and line2 do not match, it returns false.
    // If all relevant characters match, it returns true, indicating the lines are considered equal.
    private static boolean compareLines(String line1, String line2) {
        if (line1.length() != line2.length()) {
            return false;
        }

        for (int i = 0; i < line1.length(); i++) {
            if (i >= 2 && i <= 9) {
                continue; // Skip wildcard indices
            }
            if (line1.charAt(i) != line2.charAt(i)) {
                return false;
            }
        }

        return true;
    }
}
