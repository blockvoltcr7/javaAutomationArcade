package Trove4jPractice;

import gnu.trove.set.hash.TIntHashSet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class ReadFileAndCompare {



    public static void main(String[] args) {
        // Compare and find the difference
        Map<String, TIntHashSet> result = findDifferences("src/test/java/Trove4jPractice/beforeChange.txt", "src/test/java/Trove4jPractice/afterChange.txt");
        System.out.println("The records found only in the uniqueToBefore file are: " + result.get("uniqueToBefore"));
        System.out.println("The records found only in the uniqueToAfter file are: " + result.get("uniqueToAfter"));

    }

    public static Map<String, TIntHashSet> findDifferences(String beforeFilePath, String afterFilePath) {
        TIntHashSet beforeChangesSet = new TIntHashSet();
        TIntHashSet afterChangesSet = new TIntHashSet();

        // Process the "beforeChanges.txt"
        try (BufferedReader reader = new BufferedReader(new FileReader(beforeFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int key = Integer.parseInt(line.substring(1, 5).trim());
                beforeChangesSet.add(key);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read before changes file", e);
        }

        // Process the "afterChangesFile.txt"
        try (BufferedReader reader = new BufferedReader(new FileReader(afterFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int key = Integer.parseInt(line.substring(1, 5).trim());
                afterChangesSet.add(key);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read after changes file", e);
        }

        // Find the difference: records only in the before file
        beforeChangesSet.removeAll(afterChangesSet);
        afterChangesSet.removeAll(beforeChangesSet);

        Map<String, TIntHashSet> differences = new HashMap<>();
        differences.put("uniqueToBefore", beforeChangesSet);
        differences.put("uniqueToAfter", afterChangesSet);

        // Return the set of keys found only in the before file
        return differences;
    }
}
