package FileReaders;

public class HashSet_DivideAndConqour {

    /**
     * The binarySearch function is a classic example of a divide and conquer algorithm. Here's how it works:
     * The function takes four parameters: the array to be searched, the left and right indices defining the current segment of the array being searched, and the key value we're searching for.
     * The base case for the recursion is when the left index is greater than the right index. If this is the case, it means the key is not present in the array, and the function returns -1.
     * If the base case is not met, the function calculates the middle index of the current segment of the array.
     * It then checks if the key is equal to the value at the middle index. If it is, the function returns the middle index, indicating the position of the key in the array.
     * If the key is not equal to the middle value, the function checks if the key is less than the middle value. If it is, the function recursively calls itself, but this time the right index is updated to be one less than the middle index. This effectively narrows the search to the lower half of the current segment.
     * If the key is greater than the middle value, the function recursively calls itself, but this time the left index is updated to be one more than the middle index. This effectively narrows the search to the upper half of the current segment.
     * This process continues, with the search space being halved at each step, until either the key is found (and its index is returned), or the search space is empty (and -1 is returned).
     */

    public static void main(String[] args) {
        int[] array = {2, 3, 4, 10, 40};
        int key = 10;
        int result = binarySearch(array, 0, array.length - 1, key);
        if (result == -1) {
            System.out.println("Element not present in array");
        } else {
            System.out.println("Element found at index " + result);
        }
    }

    static int binarySearch(int[] array, int left, int right, int key) {
        if (right >= left) {
            int mid = left + (right - left) / 2;

            // If the element is present at the middle itself
            if (array[mid] == key) {
                return mid;
            }

            // If element is smaller than mid, then it can only be present in left subarray
            if (array[mid] > key) {
                return binarySearch(array, left, mid - 1, key);
            }

            // Else the element can only be present in right subarray
            return binarySearch(array, mid + 1, right, key);
        }

        // We reach here when element is not present in array
        return -1;
    }
}
