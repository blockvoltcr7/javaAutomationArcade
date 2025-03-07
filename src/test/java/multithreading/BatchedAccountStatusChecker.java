package multithreading;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class BatchedAccountStatusChecker {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String BATCH_API_URL = "https://api.example.com/accounts/batch";
    private static final int BATCH_SIZE = 100; // Adjust based on API limits

    public static void main(String[] args) {
        Set<String> accountIds = generateSampleAccountIds(10000);
        List<String> inactiveAccounts = Collections.synchronizedList(new ArrayList<>());

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        HttpClient client = HttpClient.newBuilder().executor(executor).build();

        List<CompletableFuture<Void>> futures = new ArrayList<>();
        List<List<String>> batches = createBatches(accountIds, BATCH_SIZE);

        System.out.println("Starting batch status check for " + accountIds.size() +
                " accounts in " + batches.size() + " batches...");
        long startTime = System.currentTimeMillis();

        for (List<String> batch : batches) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    String requestBody = objectMapper.writeValueAsString(Map.of("accountIds", batch));

                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(BATCH_API_URL))
                            .header("Content-Type", "application/json")
                            .header("Accept", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                            .build();

                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                    if (response.statusCode() == 200) {
                        JsonNode results = objectMapper.readTree(response.body()).get("accounts");
                        for (JsonNode account : results) {
                            String id = account.get("id").asText();
                            String status = account.get("status").asText();

                            if (!"Active".equals(status)) {
                                inactiveAccounts.add(id);
                            }
                        }
                        System.out.println("Processed batch of " + batch.size() + " accounts");
                    } else {
                        System.out.println("Batch request failed with status code: " + response.statusCode());
                    }
                } catch (Exception e) {
                    System.out.println("Error processing batch: " + e.getMessage());
                }
            }, executor);

            futures.add(future);
        }

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        try {
            allFutures.get(15, TimeUnit.MINUTES);

            long duration = System.currentTimeMillis() - startTime;
            System.out.println("\n--- Account Status Check Complete ---");
            System.out.println("Total accounts checked: " + accountIds.size());
            System.out.println("Inactive accounts found: " + inactiveAccounts.size());
            System.out.println("Time taken: " + (duration / 1000) + " seconds");
        } catch (Exception e) {
            System.out.println("Error during batch processing: " + e.getMessage());
        } finally {
            executor.shutdown();
        }
    }

    private static List<List<String>> createBatches(Set<String> items, int batchSize) {
        List<String> itemList = new ArrayList<>(items);
        List<List<String>> batches = new ArrayList<>();

        for (int i = 0; i < itemList.size(); i += batchSize) {
            batches.add(itemList.subList(i, Math.min(i + batchSize, itemList.size())));
        }

        return batches;
    }

    // Helper method to generate sample account IDs (same as before)
    private static Set<String> generateSampleAccountIds(int count) {
        Set<String> accountIds = new HashSet<>(count);
        for (int i = 0; i < count; i++) {
            accountIds.add("ACC" + String.format("%07d", i));
        }
        return accountIds;
    }
}