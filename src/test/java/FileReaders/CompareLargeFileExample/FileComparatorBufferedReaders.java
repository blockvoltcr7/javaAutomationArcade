package FileReaders.CompareLargeFileExample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileComparatorBufferedReaders {
    public static void main(String[] args) {
        // Define the names of the two files to compare
        String file1 = "src/test/java/FileReaders/CompareLargeFileExample/File1.txt";
        String file2 = "src/test/java/FileReaders/CompareLargeFileExample/File2.txt";
        List<String> mismatches = new ArrayList<>();

        // Use try-with-resources to ensure that the BufferedReaders are closed after use
        try (BufferedReader br1 = new BufferedReader(new FileReader(file1));
             BufferedReader br2 = new BufferedReader(new FileReader(file2))) {

            // Declare variables to hold the current lines being read
            String line1, line2;

            // Initialize a variable to keep track of the current line number
            int lineNumber = 1;

            // Read a line from the second file
            line2 = br2.readLine();

            // If the line contains "HDR ", skip it and read the next line
            while (line2 != null && line2.contains("HDR ") || line2.contains("TRL ")) {
                lineNumber++;
                line2 = br2.readLine();
            }

            // Read lines from both files until the end of the first file is reached
            while ((line1 = br1.readLine()) != null) {


                // If the lines are not equal, print a message and exit the loop
                if (!line1.equals(line2)) {
                    mismatches.add("Files differ at line " + lineNumber);
                }

                // Increment the line number
                lineNumber++;

                // Read the next line from the second file
                line2 = br2.readLine();
            }

            // If there are still lines left in either file, print a message
            if (line1 != null || line2 != null) {
                System.out.println("Files have different number of lines");

            } else if (mismatches.isEmpty()){
                System.out.println("Files are identical");
            }

            // Catch any IOExceptions that may occur and print the stack trace
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(String mismatch : mismatches){
            System.out.println(mismatch);
        }
    }
}
