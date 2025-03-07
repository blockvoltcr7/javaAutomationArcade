//package multithreading;
//
//import com.google.common.util.concurrent.RateLimiter;
//
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.Semaphore;
//import java.util.concurrent.TimeUnit;
//import java.util.Set;
//import java.util.HashSet;
//import java.util.List;
//import java.util.ArrayList;
//import java.util.Collections;
//
//public class RateLimitedAccountChecker {
//    private static final String API_URL = "https://api.example.com/accounts/";
//    // Allow only N concurrent requests
//    private static final Semaphore RATE_LIMITER = new Semaphore(50);
//    // Optional: time-based rate limit (e.g., 100 requests per second)
//    private static final RateLimiter TIME_LIMITER = RateLimiter.create(100.0);
//
//    public static void main(String[] args) {
//        Set<String> accountIds = generateSampleAccountIds(10000);
//        List<String> inactiveAccounts = Collections.synchronizedList(new ArrayList<>());
//
//        ExecutorService executor = Executors.newFixedThreadPool(
//                Math.min(100, Runtime.getRuntime().availableProcessors() * 4));
//
//        List<CompletableFuture<Void>> futures = new ArrayList<>();
//
//        for (String accountId : accountIds) {
//            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
//                try {
//                    // Acquire permit and potentially wait based on rate limit
//                    RATE_LIMITER.acquire();
//                    TIME_LIMITER.acquire();
//
//                    // Make API call here
//                    // ...
//
//                    // Check if inactive and add to list
//                    // ...
//
//                } catch (Exception e) {
//                    System.err.println("Error with account " + accountId + ": " + e.getMessage());
//                } finally {
//                    // Release permit for next request
//                    RATE_LIMITER.release();
//                }
//            }, executor);
//
//            futures.add(future);
//        }
//
//        // Wait for completion and process results as before
//    }
//}