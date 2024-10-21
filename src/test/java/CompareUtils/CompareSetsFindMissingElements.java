package CompareUtils;

import java.util.HashSet;
import java.util.Set;

public class CompareSetsFindMissingElements {

    public static void main(String[] args) {
        // Create two sets of data
        Set<String> set1 = new HashSet<>(Set.of("A", "B", "C"));
        Set<String> set2 = new HashSet<>(Set.of("B", "C", "D"));

        // Find elements in set1 that are not in set2
        Set<String> missingFromSet2 = findMissing(set1, set2);
        System.out.println("Missing from set2: " + missingFromSet2);  // [A]

        // Find elements in set2 that are not in set1
        Set<String> missingFromSet1 = findMissing(set2, set1);
        System.out.println("Missing from set1: " + missingFromSet1);  // [D]
    }

    public static <T> Set<T> findMissing(Set<T> set1, Set<T> set2) {
        Set<T> difference = new HashSet<>(set1);
        difference.removeAll(set2);
        return difference;
    }
}