package ParallelHttpRequests;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class demonstrates parallel HTTP requests using Java's HttpClient and CompletableFuture.
 * It reads test data from a file, sends POST requests to a specified endpoint for each data entry,
 * and processes the responses concurrently.
 */
public class ParallelHttpRequests {
    private static final String ENDPOINT = "https://reqres.in/api/users";
    private static final String TEST_DATA_FILE = "src/test/java/ParallelHttpRequests/test_data.txt";

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        try {
            // Read test data from file
            List<String> names = readTestData();
            
            // Create an executor service with a fixed thread pool
            ExecutorService executor = Executors.newFixedThreadPool(10);
            
            // Initialize HttpClient and ObjectMapper
            HttpClient client = HttpClient.newBuilder().build();
            ObjectMapper objectMapper = new ObjectMapper();

            List<CompletableFuture<Void>> futures = new ArrayList<>();

            // Process each name in the test data
            for (String name : names) {
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    try {
                        // Create request payload
                        String requestBody = objectMapper.writeValueAsString(new RequestPayload(name, "leader"));
                        
                        // Build HTTP request
                        HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create(ENDPOINT))
                                .header("Content-Type", "application/json")
                                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                                .timeout(Duration.ofSeconds(30))
                                .build();

                        // Send request and get response
                        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                        int statusCode = response.statusCode();
                        System.out.println(response.body());

                        // Process response
                        if (statusCode == 200) {
                            System.out.println("Request for " + name + " successful. Status code: " + statusCode);
                        } else {
                            System.err.println("Request for " + name + " failed. Status code: " + statusCode);
                        }
                    } catch (Exception e) {
                        System.err.println("Error processing request for " + name + ": " + e.getMessage());
                    }
                }, executor);

                futures.add(future);
            }

            // Wait for all futures to complete
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            executor.shutdown();

        } catch (IOException e) {
            System.err.println("Error reading test data: " + e.getMessage());
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime) + " ms");
    }

    /**
     * Reads test data from a file and returns a list of names.
     *
     * @return List of names read from the file
     * @throws IOException if there's an error reading the file
     */
    private static List<String> readTestData() throws IOException {
        List<String> names = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(TEST_DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                names.add(line.trim());
            }
        }
        return names;
    }

    /**
     * Inner class representing the payload for HTTP requests.
     */
    private static class RequestPayload {
        public String name;
        public String job;

        public RequestPayload(String name, String job) {
            this.name = name;
            this.job = job;
        }
    }

    //create a function that gets the response from the API
    public static String getResponse(String name) {
        try {
            // Create request payload
            String requestBody = new ObjectMapper().writeValueAsString(new RequestPayload(name, "leader"));

            // Build HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ENDPOINT))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .timeout(Duration.ofSeconds(30))
                    .build();

            // Send request and get response
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            return "Error processing request for " + name + ": " + e.getMessage();
        }
    }



}
