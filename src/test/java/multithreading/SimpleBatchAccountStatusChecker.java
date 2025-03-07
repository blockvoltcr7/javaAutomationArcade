package multithreading;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.*;

public class SimpleBatchAccountStatusChecker {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String BATCH_API_URL = "https://api.example.com/accounts/batch";
    private static final int BATCH_SIZE = 100; // Adjust based on API limits

    public static void main(String[] args) {
        // Generate account IDs (in real scenario, this would come from your data source)
        Set<String> accountIds = new HashSet<>();
        for (int i = 0; i < 10000; i++) {
            accountIds.add("ACC" + String.format("%07d", i));
        }

        // Create thread pool and HTTP client
        ExecutorService executor = Executors.newFixedThreadPool(10); // Modest number of threads
        HttpClient client = HttpClient.newBuilder().executor(executor).build();

        // Create batches
        List<List<String>> batches = new ArrayList<>();
        List<String> currentBatch = new ArrayList<>();

        for (String id : accountIds) {
            currentBatch.add(id);
            if (currentBatch.size() >= BATCH_SIZE) {
                batches.add(new ArrayList<>(currentBatch));
                currentBatch.clear();
            }
        }

        if (!currentBatch.isEmpty()) {
            batches.add(currentBatch);
        }

        // Process batches
        List<String> inactiveAccounts = Collections.synchronizedList(new ArrayList<>());
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        System.out.println("Starting batch processing of " + accountIds.size() +
                " accounts in " + batches.size() + " batches");
        long startTime = System.currentTimeMillis();

        for (List<String> batch : batches) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    // Create JSON with batch of IDs
                    String requestBody = MAPPER.writeValueAsString(Map.of("accountIds", batch));

                    // Make batch request
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(BATCH_API_URL))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                            .build();

                    // Process response
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                    if (response.statusCode() == 200) {
                        JsonNode accounts = MAPPER.readTree(response.body()).get("accounts");
                        for (JsonNode account : accounts) {
                            String id = account.get("id").asText();
                            String status = account.get("status").asText();

                            if (!"Active".equals(status)) {
                                inactiveAccounts.add(id);
                            }
                        }
                    } else {
                        System.err.println("Batch request failed: " + response.statusCode());
                    }
                } catch (Exception e) {
                    System.err.println("Error processing batch: " + e.getMessage());
                }
            }, executor);

            futures.add(future);
        }

        // Wait for all batches to complete
        try {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get(10, TimeUnit.MINUTES);

            // Print results
            long totalTime = System.currentTimeMillis() - startTime;
            System.out.println("\nResults:");
            System.out.println("- Total accounts: " + accountIds.size());
            System.out.println("- Inactive accounts: " + inactiveAccounts.size());
            System.out.println("- Time taken: " + (totalTime / 1000) + " seconds");

            if (!inactiveAccounts.isEmpty()) {
                System.out.println("\nFirst 10 inactive accounts:");
                inactiveAccounts.stream().limit(10).forEach(System.out::println);
                if (inactiveAccounts.size() > 10) {
                    System.out.println("... and " + (inactiveAccounts.size() - 10) + " more");
                }
            }
        } catch (Exception e) {
            System.err.println("Error waiting for batch completion: " + e.getMessage());
        } finally {
            executor.shutdown();
        }
    }
}