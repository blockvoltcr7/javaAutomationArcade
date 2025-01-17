package Algorithms;

/**
 * Implementation of QuickSort algorithm with relevance to test automation.
 * 
 * <p>Real-world use cases:
 * - Sorting test data for data-driven tests
 * - Preparing ordered test datasets for boundary value analysis
 * - Performance testing of sorting large datasets
 * 
 * <p>Time Complexity: O(n log n) average case, O(n^2) worst case
 * Space Complexity: O(log n) due to recursion stack
 */
public class QuickSort {

    /**
     * Sorts an array using QuickSort algorithm
     * @param arr Array to be sorted
     * @param low Starting index
     * @param high Ending index
     */
    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            // Partition the array around pivot
            int pi = partition(arr, low, high);

            // Recursively sort elements before and after partition
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = (low - 1); // Index of smaller element
        
        for (int j = low; j < high; j++) {
            // If current element is smaller than the pivot
            if (arr[j] < pivot) {
                i++;
                // Swap arr[i] and arr[j]
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        // Swap arr[i+1] and arr[high] (or pivot)
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }

    public static void main(String[] args) {
        int[] testData = {10, 7, 8, 9, 1, 5};
        System.out.println("Unsorted test data:");
        printArray(testData);

        quickSort(testData, 0, testData.length - 1);

        System.out.println("\nSorted test data:");
        printArray(testData);
    }

    private static void printArray(int[] arr) {
        for (int i : arr) {
            System.out.print(i + " ");
        }
    }
}
