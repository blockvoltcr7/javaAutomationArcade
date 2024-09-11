package FileReaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * This class processes a file containing account information, ignoring header and trailer lines.
 * It compares account regions from the file with expected regions stored in a HashMap.
 */
public class IgnoreTrailerHeader {

    /** The path to the file containing account information */
    private static final String FILE_PATH = "src/test/java/FileReaders/guidedtxtfile.txt";

    /**
     * Custom exception class for region mismatches.
     */
    public static class RegionMismatchException extends Exception {
        public RegionMismatchException(String message) {
            super(message);
        }
    }

    /**
     * Main method to demonstrate file processing and error handling.
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Initialize the HashMap with account IDs and their expected regions
        Map<String, String> accountRegionMap = new HashMap<>();
        accountRegionMap.put("09832742", "US");
        accountRegionMap.put("08939423", "CA");
        accountRegionMap.put("03894239", "CA");
        accountRegionMap.put("01234567", "US");

        try {
            processFile(accountRegionMap);
        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
        } catch (RegionMismatchException e) {
            System.err.println("Region Mismatch Error: " + e.getMessage());
            // Exit the program on region mismatch
            System.exit(1);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid Line Format: " + e.getMessage());
        }
    }

    /**
     * Processes the file, comparing account regions with expected values.
     * @param accountRegionMap Map of account IDs to their expected regions
     * @throws IOException If there's an error reading the file
     * @throws RegionMismatchException If there's a mismatch between expected and actual regions
     */
    private static void processFile(Map<String, String> accountRegionMap) throws IOException, RegionMismatchException {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            boolean isFirstLine = true;
            int lineNumber = 0;
            Set<String> processedAccounts = new HashSet<>();

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                // Skip the header (first line)
                if (isFirstLine) {
                    isFirstLine = false;
                    System.out.println("Skipping header: " + line);
                    continue;
                }

                // Stop processing at the trailer
                if (line.startsWith("TRL")) {
                    System.out.println("Reached trailer, stopping processing");
                    break;
                }

                // Process lines with valid length (7 chars for account ID + 2 for region)
                if (line.length() >= 9) {
                    int accountIdInt = Integer.parseInt(line.substring(0, 7));
                    String accountId = String.valueOf(accountIdInt);
                    System.out.println("Processing account " + accountId);
                    String region = line.substring(7, 9);

                    // Check if the account ID is in the HashMap
                    if (accountRegionMap.containsKey(accountId)) {
                        String expectedRegion = accountRegionMap.get(accountId);
                        System.out.println("Processing account " + accountId + " with region " + region);

                        // Check for region mismatch
                        if (!region.equals(expectedRegion)) {
                            throw new RegionMismatchException(
                                    String.format("Line %d: Region mismatch for account %s. Expected %s, found %s",
                                            lineNumber, accountId, expectedRegion, region)
                            );
                        }

                        System.out.println("Line " + lineNumber + ": Account " + accountId + " matched with correct region " + region);
                        processedAccounts.add(accountId);
                    } else {
                        System.out.println("Line " + lineNumber + ": Account " + accountId + " not found in the HashMap");
                    }
                } else {
                    throw new IllegalArgumentException("Line " + lineNumber + ": Invalid line format: " + line);
                }
            }

            // Check for accounts in the HashMap that were not in the file
            for (String accountId : accountRegionMap.keySet()) {
                if (!processedAccounts.contains(accountId)) {
                    System.out.println("Account " + accountId + " from HashMap not found in the file");
                }
            }
        }
    }
}