package ParallelHttpRequests;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class demonstrates how to send parallel HTTP requests using Java's HttpClient.
 */
public class ParallelHttpRequests {
    private static final String ENDPOINT = "https://reqres.in/api/users?page=2";

    /**
     * Main method to execute the parallel HTTP requests.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        try {
            // Create a custom SSLContext that trusts all certificates
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }}, new java.security.SecureRandom());

            // Create an HttpClient instance with the custom SSLContext
            HttpClient client = HttpClient.newBuilder()
                    .sslContext(sslContext)
                    .build();

            // Create a thread pool with 10 threads
            ExecutorService executor = Executors.newFixedThreadPool(10);

            // Initialize a list to hold CompletableFuture objects for managing asynchronous tasks
            List<CompletableFuture<Void>> futures = new ArrayList<>();

            // Loop 25 times to send the HTTP requests
            for (int i = 0; i < 25; i++) {
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    try {
                        // Build the HTTP GET request
                        HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create(ENDPOINT))
                                .header("Content-Type", "application/json")
                                .timeout(Duration.ofSeconds(30))
                                .GET()
                                .build();

                        // Send the HTTP request and get the response
                        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                        int statusCode = response.statusCode();
                        System.out.println(response.body());

                        // Check the response status code
                        if (statusCode == 200) {
                            System.out.println("Request successful. Status code: " + statusCode);
                        } else {
                            System.err.println("Request failed. Status code: " + statusCode);
                        }
                    } catch (Exception e) {
                        System.err.println("Error processing request: " + e.getMessage());
                    }
                }, executor);

                futures.add(future);
            }

            // Wait for all futures to complete
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            // Shutdown the executor service
            executor.shutdown();

            long endTime = System.currentTimeMillis();
            System.out.println("Total execution time: " + (endTime - startTime) + " ms");
        } catch (Exception e) {
            System.err.println("Error setting up SSL context: " + e.getMessage());
        }
    }
}