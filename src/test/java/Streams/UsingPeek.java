package Streams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UsingPeek {


    public static void main(String[] args) {
        // Create a HashMap with some sample data
        Map<String, Integer> map = new HashMap<>();
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);

        // Process the entries of the HashMap using a stream
        List<Map.Entry<String, Integer>> processedEntries = map.entrySet().stream()
                // Use peek to perform some logic on each entry
                .peek(entry -> {
                    // Example logic: Print the entry
                    System.out.println("Processing entry: " + entry);
                    // Example logic: Modify the value (this won't affect the original map)
                    entry.setValue(entry.getValue() * 2);
                })
                // Collect the results into a list
                .collect(Collectors.toList());

        // Print the processed entries
        processedEntries.forEach(entry ->
                System.out.println("Processed entry: " + entry.getKey() + " = " + entry.getValue()));
    }
}
