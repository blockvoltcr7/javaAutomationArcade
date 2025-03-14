package Programs_IQ.FindDuplicate_Elements;

import java.util.HashMap;
import java.util.Map;

public class FindDupCharsArray {

    /**
     * write a method that can find the unique chars from the string
     *
     * string str = ""AAABBBCCCDDDEEE"; //==> "ABCDE"
     */

    public static void main(String[] args) {
        String str = "ggaafddss";
        System.out.println(findUniqueChars(str));
        System.out.println(findUniqueCharsMap(str));
        Character[] chars = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'};

    }

    //function to find unique characters
    public static String findUniqueChars(String str) {
        String uniqueChars = "";
        for (int i = 0; i < str.length(); i++) {
            if (!uniqueChars.contains("" + str.charAt(i))) {
                uniqueChars += str.charAt(i);
            }
        }
        return uniqueChars;
    }

    public static String findUniqueCharsMap(String str) {
        Map<Character, Integer> charCountMap = new HashMap<>();
        for (char c : str.toCharArray()) {
            charCountMap.put(c, charCountMap.getOrDefault(c, 0) + 1);
        }

        StringBuilder uniqueChars = new StringBuilder();
        for (Map.Entry<Character, Integer> entry : charCountMap.entrySet()) {
            if (entry.getValue() == 1) {
                uniqueChars.append(entry.getKey());
            }
        }

        return uniqueChars.toString();
    }
}
