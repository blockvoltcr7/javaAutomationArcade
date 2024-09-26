package FileReaders.FlatFileComplexFileReading;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ReadComplexFile {
    public static void main(String[] args) {
        String filePath = "src/test/java/FileReaders/FlatFileComplexFileReading/datafile.txt";
        HashSet<String> linesSet = fetchLinesAfterText(filePath, "CHANGED NEW REC");
        boolean exists = linesSet.contains("        pos.    1- 86   9842840000000ca428394298429daniel samuel             8");
        for (String line : linesSet) {
            System.out.println(line);
        }
        System.out.println("Does the line exist in the fetched lines? " + exists);
    }



    public static HashSet<String> fetchLinesAfterText(String filePath, String searchText) {
        // Create a new HashSet to store the lines
        HashSet<String> fetchedLines = new HashSet<>();

        // Use a try-with-resources statement to open the file and automatically close it when done
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean fetchNextLine = false;

            // Read the file line by line
            while ((line = reader.readLine()) != null) {
                // If the previous line was the search text, add the current line to the HashSet
                if (fetchNextLine) {
                    fetchedLines.add(line);
                    fetchNextLine = false;
                }

                // If the current line is the search text, set fetchNextLine to true
                if (line.trim().equals(searchText)) {
                    fetchNextLine = true;
                }
            }
        } catch (IOException e) {
            // Print the stack trace for any IOExceptions
            e.printStackTrace();
        }

        // Return the HashSet of lines
        return fetchedLines;
    }
}