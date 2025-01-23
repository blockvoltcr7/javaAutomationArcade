package StringManipulation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FindUniqueCharsFromString {

    /**
     * write a method tha t can find the unique characters from the String
     * String str = "abbxxfggvvaad"
     * answer = "fd"
     */


    public static void main(String[] args) {
        String str = "abbxxfggvvaad";
        String unique = findUniqueChars(str);
        System.out.println("Unique characters: " + unique); // Output: "fd"
    }

    /**
     * Finds the unique characters in a given string.
     * A character is considered unique if it appears only once in the string.
     *
     * @param str the input string
     * @return a string containing the unique characters
     */
    public static String findUniqueChars(String str) {
        // Map to store the count of each character
        Map<Character, Integer> charCountMap = new HashMap<>();
        // StringBuilder to collect unique characters
        StringBuilder uniqueChars = new StringBuilder();

        // Count the occurrences of each character
        for (char c : str.toCharArray()) {
            charCountMap.put(c, charCountMap.getOrDefault(c, 0) + 1);
        }

        // Collect characters that appear only once
        for (char c : str.toCharArray()) {
            if (charCountMap.get(c) == 1) {
                uniqueChars.append(c);
            }
        }

        // Return the string of unique characters
        return uniqueChars.toString();
    }

    //create another method that returns the unique characters from the string that uses for loop
    public static String findUniqueCharsUsingForLoop(String str) {
        // Map to store the count of each character
        Map<Character, Integer> charCountMap = new HashMap<>();
        // StringBuilder to collect unique characters
        StringBuilder uniqueChars = new StringBuilder();

        // Count the occurrences of each character
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            charCountMap.put(c, charCountMap.getOrDefault(c, 0) + 1);
        }

        // Collect characters that appear only once
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (charCountMap.get(c) == 1) {
                uniqueChars.append(c);
            }
        }

        // Return the string of unique characters
        return uniqueChars.toString();
    }

    /**
     * Finds the unique characters in a given string using a HashSet.
     * A character is considered unique if it appears only once in the string.
     *
     * @param str the input string
     * @return a string containing the unique characters
     */
    public static String findUniqueCharsUsingHashSet(String str) {
        // Set to store characters that have been seen once
        Set<Character> seenOnce = new HashSet<>();
        // Set to store characters that have been seen more than once
        Set<Character> seenMoreThanOnce = new HashSet<>();
        // StringBuilder to collect unique characters
        StringBuilder uniqueChars = new StringBuilder();

        // Iterate over each character in the input string
        for (char c : str.toCharArray()) {
            if (!seenOnce.add(c)) {
                seenMoreThanOnce.add(c);
            }
        }

        // Collect characters that appear only once
        for (char c : str.toCharArray()) {
            if (!seenMoreThanOnce.contains(c)) {
                uniqueChars.append(c);
            }
        }

        // Return the string of unique characters
        return uniqueChars.toString();
    }


}
