package StringManipulation;

import java.util.HashSet;
import java.util.Set;

public class FindUniqueElements {

    public static void main(String[] args) {
        // Input string with duplicate characters
        String str = "aabbccddeeff";
        // String to store unique characters
        String unique = "";

        // Iterate over each character in the input string
        for (int i = 0; i < str.length(); i++) {
            // Check if the character is not already in the unique string
            if (!unique.contains(String.valueOf(str.charAt(i)))) {
                // Append the character to the unique string
                unique += str.charAt(i);
            }
        }

        System.out.println(findUniqueElements(str));
        // Print the string with unique characters
        System.out.println(unique);
    }

    public static String findUniqueElements(String str) {
        // Create a set to store unique characters
        Set<Character> uniqueChars = new HashSet<>();
        // Use a StringBuilder to build the result string
        StringBuilder unique = new StringBuilder();

        // Iterate over each character in the input string
        for (int i = 0; i < str.length(); i++) {
            char currentChar = str.charAt(i);
            // If the character is not already in the set, add it to the set and append to the result
            if (uniqueChars.add(currentChar)) {
                unique.append(currentChar);
            }
        }

        // Return the result string containing unique characters
        return unique.toString();
    }

}
