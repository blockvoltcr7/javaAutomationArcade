package Algorithms;

/**
 * Implementation of MergeSort algorithm with test automation context.
 * 
 * <p>Real-world use cases:
 * - Sorting large test result datasets
 * - Merging sorted test data from multiple sources
 * - Performance comparison with other sorting algorithms
 * 
 * <p>Time Complexity: O(n log n) in all cases
 * Space Complexity: O(n) for auxiliary array
 */
public class MergeSort {

    /**
     * Main function that sorts arr[l..r] using merge()
     * @param arr Array to be sorted
     * @param l Left index
     * @param r Right index
     */
    public static void mergeSort(int[] arr, int l, int r) {
        if (l < r) {
            // Find the middle point
            int m = l + (r - l) / 2;

            // Sort first and second halves
            mergeSort(arr, l, m);
            mergeSort(arr, m + 1, r);

            // Merge the sorted halves
            merge(arr, l, m, r);
        }
    }

    private static void merge(int[] arr, int l, int m, int r) {
        // Sizes of two subarrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;

        // Create temp arrays
        int[] L = new int[n1];
        int[] R = new int[n2];

        // Copy data to temp arrays
        System.arraycopy(arr, l, L, 0, n1);
        System.arraycopy(arr, m + 1, R, 0, n2);

        // Merge the temp arrays
        int i = 0, j = 0;
        int k = l;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }

        // Copy remaining elements of L[] if any
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        // Copy remaining elements of R[] if any
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    public static void main(String[] args) {
        int[] testResults = {38, 27, 43, 3, 9, 82, 10};
        System.out.println("Unsorted test results:");
        printArray(testResults);

        mergeSort(testResults, 0, testResults.length - 1);

        System.out.println("\nSorted test results:");
        printArray(testResults);
    }

    private static void printArray(int[] arr) {
        for (int i : arr) {
            System.out.print(i + " ");
        }
    }
}
