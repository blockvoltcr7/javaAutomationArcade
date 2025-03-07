package multithreading;

import java.util.concurrent.*;
import java.util.*;



public class EventDrivenAccountChecker {
    public static void main(String[] args) throws Exception {
        Set<String> accountIds = generateSampleAccountIds(10000);

        // Create work queue
        BlockingQueue<String> workQueue = new LinkedBlockingQueue<>(accountIds);
        List<String> inactiveAccounts = Collections.synchronizedList(new ArrayList<>());

        // Create workers
        int workerCount = Runtime.getRuntime().availableProcessors() * 2;
        ExecutorService executor = Executors.newFixedThreadPool(workerCount);
        CountDownLatch completionLatch = new CountDownLatch(workerCount);

        // Start workers
        for (int i = 0; i < workerCount; i++) {
            executor.submit(() -> {
                try {
                    while (true) {
                        String accountId = workQueue.poll();
                        if (accountId == null) break;

                        // Check account status
                        boolean isActive = checkAccountStatus(accountId);
                        if (!isActive) {
                            inactiveAccounts.add(accountId);
                        }
                    }
                } finally {
                    completionLatch.countDown();
                }
            });
        }

        // Wait for completion
        completionLatch.await();
        executor.shutdown();

        // Report results
        System.out.println("Inactive accounts: " + inactiveAccounts.size());
    }

    private static boolean checkAccountStatus(String accountId) {
        // API call implementation
        return true; // Sample return
    }
}