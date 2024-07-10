package FileReaders.CompareLargeFileExample;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileCompareBufferedreader_v2 {

    public static void main(String[] args) {
        String file1 = "src/test/java/FileReaders/CompareLargeFileExample/File1.txt";
        String file2 = "src/test/java/FileReaders/CompareLargeFileExample/File2.txt";
        compareFiles(file1, file2);
    }

    public static void compareFiles(String file1Path, String file2Path) {
        List<String> mismatches = new ArrayList<>();

        try (BufferedReader br1 = new BufferedReader(new FileReader(file1Path));
             BufferedReader br2 = new BufferedReader(new FileReader(file2Path))) {

            String line1, line2;
            int lineNumber = 0;

            // Skip headers and trailers in file2
            while ((line2 = br2.readLine()) != null && (line2.contains("HDR ") || line2.contains("TRL "))) {
                lineNumber++;
            }

            while ((line1 = br1.readLine()) != null) {
                lineNumber++;

                if (line2 == null) {
                    System.out.println("File2 is shorter than File1. Mismatch at line " + lineNumber);
                    break;
                }

                if (!line1.equals(line2)) {
                    mismatches.add(String.format("Mismatch at line %d:%nFile1: %s%nFile2: %s", lineNumber, line1, line2));
                }

                line2 = br2.readLine();
            }

            if (line2 != null) {
                System.out.println("File2 is longer than File1. Extra content starts at line " + (lineNumber + 1));
            }

            if (mismatches.isEmpty()) {
                System.out.println("Files are identical (excluding headers and trailers).");
            } else {
                System.out.println("Mismatches found:");
                mismatches.forEach(System.out::println);
            }

        } catch (IOException e) {
            System.err.println("An error occurred while reading the files:");
            e.printStackTrace();
        }
    }
}
