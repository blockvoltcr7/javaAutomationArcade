package Collections.List_vs_set;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class List_vs_set_sampl3 {

    public static void main(String[] args) {
        // Step 1: Create a List of random numbers with duplicates
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 2, 3, 6, 7, 8, 9, 1, 0, 5, 6, 7, 8, 9, 0);

        // Step 2: Print the List
        System.out.println("List: " + list);

        // Step 3: Convert the List to a Set
        Set<Integer> set = new HashSet<>(list);

        // Step 4: Print the Set
        System.out.println("Set: " + set);
    }
}
