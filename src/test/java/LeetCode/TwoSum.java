package LeetCode;
import java.util.HashMap;
public class TwoSum {

    /**
     * explain the solution
     * two sum - find two numbers in an array that add up to a target using a hashmap
     * for efficient lookups and return the indices of the two numbers in an array
     *
     * @param args
     */
    public static void main(String[] args) {

        int nums[] = {2, 7, 11, 15};
        int target = 9;
        int[] result = twoSum(nums, target);
        System.out.println("Indices of the two numbers: " + result[0] + ", " + result[1]); // Output: 0, 1
    }

    public static int[] twoSum(int[] nums, int target) {
        // Create a hashmap to store the number and its index
        HashMap<Integer, Integer> map = new HashMap<>();
        // Iterate over the array
        for (int i = 0; i < nums.length; i++) {
            // Calculate the complement of the current number
            int complement = target - nums[i];
            // Check if the complement is in the map
            if (map.containsKey(complement)) {
                // Return the indices of the two numbers
                return new int[]{map.get(complement), i};
            }
            // Add the number and its index to the map
            map.put(nums[i], i);
        }
        // Return an empty array if no solution is found
        return new int[]{};
    }

}
