package CSVUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * CSV Processor to find duplicate account IDs in a CSV file.
 * 
 * <p>Real-world use cases:
 * - Detecting duplicate accounts in user databases
 * - Validating test data integrity
 * - Identifying data quality issues in production systems
 * 
 * <p>Time Complexity: O(n) where n is number of rows
 * Space Complexity: O(n) for storing account IDs
 */
public class ReadColumnsFromCSVOnly {

    /**
     * Finds duplicate account IDs in a CSV file
     * @param filePath Path to CSV file
     * @param accountIdColumn Name of column containing account IDs
     * @return Set of duplicate account IDs
     */
    public static Set<String> findDuplicateAccountIds(String filePath, String accountIdColumn) {
        Set<String> uniqueIds = new HashSet<>();
        Set<String> duplicates = new HashSet<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Read header to find account ID column index
            String header = br.readLine();
            if (header == null) {
                throw new IllegalArgumentException("Empty CSV file");
            }
            
            String[] headers = header.split(",");
            int accountIdIndex = -1;
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].trim().equalsIgnoreCase(accountIdColumn)) {
                    accountIdIndex = i;
                    break;
                }
            }
            
            if (accountIdIndex == -1) {
                throw new IllegalArgumentException("Account ID column not found");
            }

            // Process each row
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length > accountIdIndex) {
                    String accountId = values[accountIdIndex].trim();
                    if (!uniqueIds.add(accountId)) {
                        duplicates.add(accountId);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return duplicates;
    }

    public static void main(String[] args) {
        String filePath = "src/test/java/CSVUtils/testCsvFile1.csv";
        String accountIdColumn = "account_id";
        
        Set<String> duplicates = findDuplicateAccountIds(filePath, accountIdColumn);
        
        if (duplicates.isEmpty()) {
            System.out.println("No duplicate account IDs found");
        } else {
            System.out.println("Duplicate account IDs found:");
            duplicates.forEach(System.out::println);
        }
    }
}


