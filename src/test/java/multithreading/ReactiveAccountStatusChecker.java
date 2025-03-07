package multithreading;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.*;
import java.util.concurrent.*;

public class ReactiveAccountStatusChecker {
    public static void main(String[] args) {
        Set<String> accountIds = generateSampleAccountIds(10000);
        List<String> inactiveAccounts = Collections.synchronizedList(new ArrayList<>());

        long startTime = System.currentTimeMillis();

        // Create a backpressure-aware flow with configurable parallelism
        Flowable.fromIterable(accountIds)
                .flatMap(accountId -> Flowable.just(accountId)
                                .subscribeOn(Schedulers.io())
                                .map(id -> checkAccountStatus(id))
                                .onErrorReturn(e -> {
                                    System.err.println("Error checking account " + accountId + ": " + e.getMessage());
                                    return new AccountStatus(accountId, "Error");
                                }),
                        // Concurrency level - adjust based on API capabilities
                        20)
                .blockingSubscribe(status -> {
                    if (!"Active".equals(status.status)) {
                        inactiveAccounts.add(status.accountId);
                        System.out.println("Found inactive account: " + status.accountId);
                    }
                });

        long duration = System.currentTimeMillis() - startTime;
        System.out.println("\nTotal accounts checked: " + accountIds.size());
        System.out.println("Inactive accounts found: " + inactiveAccounts.size());
        System.out.println("Time taken: " + (duration / 1000) + " seconds");
    }

    private static AccountStatus checkAccountStatus(String accountId) {
        // API call implementation here
        // In real code, you'd make the HTTP request here
        return new AccountStatus(accountId, "Active"); // Simulated response
    }

    static class AccountStatus {
        final String accountId;
        final String status;

        AccountStatus(String accountId, String status) {
            this.accountId = accountId;
            this.status = status;
        }
    }

    // Sample account ID generator
    private static Set<String> generateSampleAccountIds(int count) {
        // Implementation as before
        return new HashSet<>();
    }
}