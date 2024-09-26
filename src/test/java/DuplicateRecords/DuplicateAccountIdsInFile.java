package DuplicateRecords;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DuplicateAccountIdsInFile {
    public static void main(String[] args) {
        String filePath = "src/test/java/DuplicateRecords/accountids.txt"; // Replace with the path to your text file

        Set<String> duplicateAccountIds = findDuplicateAccountIds(filePath);

        // Print duplicate account IDs
        if (!duplicateAccountIds.isEmpty()) {
            System.out.println("Duplicate Account IDs:");
            for (String accountId : duplicateAccountIds) {
                System.out.println(accountId);
            }
        } else {
            System.out.println("No duplicate account IDs found.");
        }
    }

    public static Set<String> findDuplicateAccountIds(String filePath) {
        Set<String> duplicateAccountIds = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            Set<String> accountSet = new HashSet<>();

            while ((line = reader.readLine()) != null) {
                // Extract the account ID from position 0 to 7
                String accountId = line.substring(0, 7);

                // Check for duplicate account IDs
                if (!accountSet.add(accountId)) {
                    duplicateAccountIds.add(accountId);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return duplicateAccountIds;
    }

    public static void writeAccountIdsToFile(String filePath, Set<Integer> accountIds) {
        try {
            List<String> accountIdsAsString = accountIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.toList());

            Files.write(Paths.get(filePath), accountIdsAsString);

            System.out.println("Duplicate Account IDs written to: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
