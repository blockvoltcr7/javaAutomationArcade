package HttpClient;

import HttpClient.Utils.HttpClientUtils;
import org.json.JSONObject;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class RegresApiTest {
    private static final String BASE_URL = "https://reqres.in/api/users";
    private static final int MAX_CONCURRENT_REQUESTS = 100; // Adjust this value based on your system's capacity

    public static void main(String[] args) {
        int numberOfCores = Runtime.getRuntime().availableProcessors();
        int threadPoolSize = 2 * numberOfCores; // Start with 2 * number of cores
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);
        Semaphore semaphore = new Semaphore(MAX_CONCURRENT_REQUESTS);

        List<CompletableFuture<?>> futures = new ArrayList<>();

        try {
            for (int i = 0; i < 36000; i++) {
                semaphore.acquire(); // Acquire a permit before submitting a request
                CompletableFuture<?> future = CompletableFuture.runAsync(() -> {
                    try {
                        // Set up headers
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json");

                        // Send GET request using HttpClientUtils
                        CompletableFuture<HttpResponse<String>> getResponse = HttpClientUtils.sendGetRequest(BASE_URL + "?page=2", headers);
                        getResponse.thenAccept(response -> {
                            String responseBody = response.body();

                            // Parse JSON response
                            JSONObject jsonResponse = new JSONObject(responseBody);

                            // Verify "page" value
                            int page = jsonResponse.getInt("page");
                            if (page == 2) {
                                System.out.println("Page value is correct: " + page);
                            } else {
                                System.err.println("Page value is incorrect: " + page);
                            }

                            // Verify "total" value
                            int total = jsonResponse.getInt("total");
                            if (total == 12) {
                                System.out.println("Total value is correct: " + total);
                            } else {
                                System.err.println("Total value is incorrect: " + total);
                            }
                        }).exceptionally(ex -> {
                            ex.printStackTrace();
                            return null;
                        }).whenComplete((result, ex) -> semaphore.release()); // Release the permit after the request completes
                    } catch (Exception e) {
                        e.printStackTrace();
                        semaphore.release(); // Ensure the permit is released in case of an exception
                    }
                }, executor);
                futures.add(future);
            }

            // Wait for all futures to complete
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }
        }
    }
}