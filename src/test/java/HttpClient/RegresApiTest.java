package HttpClient;

import HttpClient.Utils.HttpClientUtils;
import org.json.JSONObject;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RegresApiTest {
    private static final String BASE_URL = "https://reqres.in/api/users";

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(25);

        try {
            for (int i = 0; i < 25; i++) {
                executor.submit(() -> {
                    try {
                        // Set up headers
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json");

                        // Send GET request
                        CompletableFuture<HttpResponse<String>> getResponse = HttpClientUtils.sendGetRequest(BASE_URL + "?page=2", headers);
                        String responseBody = getResponse.get().body();
//                        System.out.println("GET Response: " + responseBody);

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

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
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