package Trove4jPractice;

import gnu.trove.set.hash.TIntHashSet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class ReadFileAndCompare2 {

    public static void main(String[] args) {
        Map<String, TIntHashSet> result = findDifferences("src/test/java/Trove4jPractice/beforeChange.txt", "src/test/java/Trove4jPractice/afterChange.txt");
        System.out.println("Records unique to the before file: " + result.get("uniqueToBefore"));
        System.out.println("Records unique to the after file: " + result.get("uniqueToAfter"));
    }

    public static Map<String, TIntHashSet> findDifferences(String beforeFilePath, String afterFilePath) {
        TIntHashSet beforeChangesSet = new TIntHashSet();
        TIntHashSet afterChangesSet = new TIntHashSet();

        // Process "beforeChanges.txt"
        try (BufferedReader reader = new BufferedReader(new FileReader(beforeFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int key = Integer.parseInt(line.substring(1, 5).trim());
                beforeChangesSet.add(key);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read before changes file", e);
        }

        // Process "afterChangesFile.txt"
        try (BufferedReader reader = new BufferedReader(new FileReader(afterFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int key = Integer.parseInt(line.substring(1, 5).trim());
                afterChangesSet.add(key);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read after changes file", e);
        }

        // Find differences
        TIntHashSet uniqueToBefore = new TIntHashSet(beforeChangesSet);
        uniqueToBefore.removeAll(afterChangesSet); // Records only in the before file

        TIntHashSet uniqueToAfter = new TIntHashSet(afterChangesSet);
        uniqueToAfter.removeAll(beforeChangesSet); // Records only in the after file

        Map<String, TIntHashSet> differences = new HashMap<>();
        differences.put("uniqueToBefore", uniqueToBefore);
        differences.put("uniqueToAfter", uniqueToAfter);

        return differences;
    }
}
