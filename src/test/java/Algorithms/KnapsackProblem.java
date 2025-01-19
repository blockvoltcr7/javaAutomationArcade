package Algorithms;

/**
 * Implementation of the 0-1 Knapsack problem using dynamic programming.
 * 
 * <p>Real-world use cases in test automation:
 * - Optimizing test case selection for regression testing
 * - Resource allocation for parallel test execution
 * - Maximizing test coverage with limited execution time
 * 
 * <p>Time Complexity: O(nW) where n is number of items and W is capacity
 * Space Complexity: O(nW) for the DP table
 */
public class KnapsackProblem {

    /**
     * Solves the 0-1 Knapsack problem
     * @param values Array of item values
     * @param weights Array of item weights
     * @param capacity Maximum weight capacity
     * @return Maximum value that can be obtained
     */
    public static int knapsack(int[] values, int[] weights, int capacity) {
        int n = values.length;
        int[][] dp = new int[n + 1][capacity + 1];

        // Build table dp[][] in bottom up manner
        for (int i = 1; i <= n; i++) {
            for (int w = 0; w <= capacity; w++) {
                if (weights[i - 1] <= w) {
                    dp[i][w] = Math.max(
                        values[i - 1] + dp[i - 1][w - weights[i - 1]],
                        dp[i - 1][w]
                    );
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }

        return dp[n][capacity];
    }

    public static void main(String[] args) {
        // Example: Selecting test cases with time constraints
        int[] testValues = {60, 100, 120}; // Test coverage values
        int[] testWeights = {10, 20, 30};  // Test execution times
        int maxTime = 50;                  // Total available time
        
        int maxCoverage = knapsack(testValues, testWeights, maxTime);
        System.out.println("Maximum test coverage achievable: " + maxCoverage);
    }
}
