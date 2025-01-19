package Algorithms;

/**
 * Implementation of Longest Common Subsequence (LCS) algorithm.
 * 
 * <p>Real-world use cases in test automation:
 * - Comparing expected vs actual test results
 * - Analyzing test case similarity for optimization
 * - Version comparison in test data management
 * 
 * <p>Time Complexity: O(mn) where m and n are lengths of input strings
 * Space Complexity: O(mn) for the DP table
 */
public class LongestCommonSubsequence {

    /**
     * Finds length of LCS for two strings
     * @param text1 First string
     * @param text2 Second string
     * @return Length of LCS
     */
    public static int longestCommonSubsequence(String text1, String text2) {
        int m = text1.length();
        int n = text2.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        return dp[m][n];
    }

    public static void main(String[] args) {
        String expectedOutput = "Login successful";
        String actualOutput = "Login failed";
        
        int lcsLength = longestCommonSubsequence(expectedOutput, actualOutput);
        System.out.println("Longest Common Subsequence length: " + lcsLength);
        System.out.println("Similarity percentage: " + 
            (lcsLength * 100.0 / Math.max(expectedOutput.length(), actualOutput.length())) + "%");
    }
}
