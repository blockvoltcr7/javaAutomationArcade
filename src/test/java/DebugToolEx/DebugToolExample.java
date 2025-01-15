package DebugToolEx;

import java.util.Arrays;
import java.util.List;

public class DebugToolExample {

    public static void main(String[] args) {
        // Example 1: Simple arithmetic error
        int a = 5;
        int b = 0;
        int result = divide(a, b); // This will cause an ArithmeticException
        System.out.println("Result of division: " + result);

        // Example 2: Logical error in a loop
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        printNames(names);
    }

    public static int divide(int x, int y) {
        return x / y; // Intentional bug: division by zero
    }

    public static void printNames(List<String> names) {
        for (int i = 0; i <= names.size(); i++) { // Intentional bug: should be i < names.size()
            System.out.println(names.get(i)); // This will cause an IndexOutOfBoundsException
        }
    }
}