package JavaUtilsTests;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BiFunctionsEx {

    public static void main(String[] args) {
        BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
        int result = add.apply(5, 3);
        System.out.println("Result: " + result); // Output: Result: 8
        flatMapBiFunc();
        concatenateLists();
    }


    /**
     * This method demonstrates the use of a BiFunction to flatten two lists of lists into a single list.
     * It uses Java Streams and the flatMap method to achieve this.
     */
    public static void flatMapBiFunc() {
        // BiFunction to flatten two lists of lists into a single list
        BiFunction<List<List<Integer>>, List<List<Integer>>, List<Integer>> flattenLists = (list1, list2) ->
                list1.stream()
                        .flatMap(List::stream)
                        .collect(Collectors.toList());

        // Sample input lists of lists
        List<List<Integer>> listOfLists1 = List.of(List.of(1, 2, 3), List.of(4, 5, 6));
        List<List<Integer>> listOfLists2 = List.of(List.of(7, 8, 9), List.of(10, 11, 12));

        // Apply the BiFunction to flatten the lists
        List<Integer> result = flattenLists.apply(listOfLists1, listOfLists2);

        // Print the flattened list
        System.out.println("Flattened List: " + result); // Output: Flattened List: [1, 2, 3, 4, 5, 6]
    }

    public static void  concatenateLists() {
        // BiFunction to concatenate two lists of strings
        /**
         * the first two parameters are the input lists, and the third parameter is the output list.
         * the benefit of this is that we can use the same BiFunction to concatenate any two lists of strings.
         * this is faster than using the + operator to concatenate strings in a loop.
         * the arrow allows us to define the lambda expression that takes two lists of strings as input and returns a single list of strings as output.
         * this is easier than writing a separate method to concatenate the lists.
         */
        BiFunction<List<String>, List<String>, List<String>> concatenateLists = (list1, list2) ->
                Stream.concat(list1.stream(), list2.stream())
                        .collect(Collectors.toList());

        // Sample input lists
        List<String> list1 = List.of("Hello", "World");
        List<String> list2 = List.of("Java", "Streams");

        // Apply the BiFunction to concatenate the lists
        List<String> result = concatenateLists.apply(list1, list2);

        // Print the concatenated list
        System.out.println("Concatenated List: " + result); // Output: Concatenated List: [Hello, World, Java, Streams]
    }

}
