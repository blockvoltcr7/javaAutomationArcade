package multithreading;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AccountStatusChecker {

    /**
     *
     */

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String API_URL = "https://api.example.com/accounts/";

    public static void main(String[] args) {
        // Sample set of 10,000 account IDs (in real scenario, this would be loaded from somewhere)
        Set<String> accountIds = generateSampleAccountIds(10000);

        // Thread-safe list to store inactive accounts
        List<String> inactiveAccounts = new CopyOnWriteArrayList<>();

        // Create a custom executor service with more threads for better parallelism
        // Adjust thread count based on your system's capacity and API rate limits
        int processors = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(processors * 2);

        // Create HttpClient
        HttpClient client = HttpClient.newBuilder()
                .executor(executor)
                .build();

        // List to store our CompletableFutures
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        System.out.println("Starting status check for " + accountIds.size() + " accounts...");
        long startTime = System.currentTimeMillis();

        // Create a CompletableFuture for each account ID
        for (String accountId : accountIds) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    String accountUrl = API_URL + accountId;
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(accountUrl))
                            .header("Accept", "application/json")
                            .GET()
                            .build();

                    HttpResponse<String> response = client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

                    if (response.statusCode() == 200) {
                        // Parse JSON response
                        JsonNode jsonNode = objectMapper.readTree(response.body());
                        String status = jsonNode.get("status").asText();

                        if (!"Active".equals(status)) {
                            // Add to inactive accounts list if not active
                            inactiveAccounts.add(accountId);
                            System.out.println("Found inactive account: " + accountId + " (Status: " + status + ")");
                        }
                    } else {
                        System.out.println("Error checking account " + accountId +
                                ", status code: " + response.statusCode());
                        // Optionally add to a separate list for accounts with API errors
                    }
                } catch (Exception e) {
                    System.out.println("Error processing account " + accountId + ": " + e.getMessage());
                }
            }, executor);

            futures.add(future);
        }

        // Combine all futures into one that completes when all complete
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        );

        // Wait for all futures to complete
        try {
            // Allow reasonable timeout for 10,000 API calls
            allFutures.get(30, TimeUnit.MINUTES);

            long duration = System.currentTimeMillis() - startTime;

            System.out.println("\n--- Account Status Check Complete ---");
            System.out.println("Total accounts checked: " + accountIds.size());
            System.out.println("Inactive accounts found: " + inactiveAccounts.size());
            System.out.println("Time taken: " + (duration / 1000) + " seconds");

            if (!inactiveAccounts.isEmpty()) {
                System.out.println("\nList of inactive accounts:");
                inactiveAccounts.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("Error waiting for all account checks to complete: " + e.getMessage());
        }

        // Shutdown the executor service
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.MINUTES)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }

    // Helper method to generate sample account IDs
    private static Set<String> generateSampleAccountIds(int count) {
        Set<String> accountIds = new HashSet<>(count);
        for (int i = 0; i < count; i++) {
            accountIds.add("ACC" + String.format("%07d", i));
        }
        return accountIds;
    }
}