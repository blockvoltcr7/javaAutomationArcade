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
        HashSet<String> fetchedLines = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean fetchNextLine = false;

            while ((line = reader.readLine()) != null) {
                if (fetchNextLine) {
                    fetchedLines.add(line);
                    fetchNextLine = false;
                }

                if (line.trim().equals(searchText)) {
                    fetchNextLine = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fetchedLines;
    }
}