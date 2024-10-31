# Parallel HTTP Requests with Java's HttpClient

## Overview

This project demonstrates how to send parallel HTTP requests using Java's `HttpClient` and `CompletableFuture`. The implementation is designed to efficiently handle a large number of HTTP requests in parallel, improving performance and resource utilization.

## Key Concepts

1. **Objective**:
   - Efficiently handle a large number of HTTP requests in parallel.

2. **Approach**:
   - Use Java's `CompletableFuture` for asynchronous task management.
   - Use an `ExecutorService` with a fixed thread pool to control the number of concurrent threads.

3. **Implementation Details**:
   - **Custom Executor**: Create a custom executor with a thread pool size based on the number of available CPU cores.
   - **Semaphore**: Use a semaphore to limit the number of concurrent requests to avoid overwhelming the system.
   - **Asynchronous Requests**: Send each HTTP request asynchronously using `CompletableFuture.runAsync()`.
   - **Error Handling**: Catch and log exceptions, and release the semaphore to ensure proper resource management.
   - **Completion Handling**: Use `CompletableFuture.allOf()` to wait for all requests to complete before shutting down the executor.

4. **Benefits**:
   - **Improved Performance**: Parallel execution reduces the overall time required to process all requests.
   - **Efficient Resource Utilization**: Leverages available CPU cores and manages I/O operations efficiently.
   - **Scalability**: Can handle a large number of requests without a linear increase in execution time.
   - **Controlled Concurrency**: The fixed thread pool and semaphore prevent system overload.

## Example Code

```java
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