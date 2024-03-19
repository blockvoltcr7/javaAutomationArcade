package Trove4jPractice;

import gnu.trove.set.hash.TIntHashSet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;





public class FileComparisonHashSet {


    public static void main(String[] args) {
        TIntHashSet beforeChangesSet = new TIntHashSet();
        beforeChangesSet.add(1);
        beforeChangesSet.add(2);
        beforeChangesSet.add(3);
        beforeChangesSet.add(4);
        beforeChangesSet.add(5);

        TIntHashSet foundInBothSet = new TIntHashSet();
        foundInBothSet.add(1);
        foundInBothSet.add(2);
        foundInBothSet.add(3);
        foundInBothSet.add(4);


        boolean areEqual = beforeChangesSet.equals(foundInBothSet);
        System.out.println("Are the sets equal? " + areEqual);


        TIntHashSet intersection = new TIntHashSet(beforeChangesSet);
        intersection.retainAll(foundInBothSet);

        // Unique to beforeChangesSet
        TIntHashSet uniqueToBeforeChangesSet = new TIntHashSet(beforeChangesSet);
        uniqueToBeforeChangesSet.removeAll(foundInBothSet);

        // Unique to foundInBothSet
        TIntHashSet uniqueToFoundInBothSet = new TIntHashSet(foundInBothSet);
        uniqueToFoundInBothSet.removeAll(beforeChangesSet);

        System.out.println("Intersection: " + intersection);
        System.out.println("Unique to beforeChangesSet: " + uniqueToBeforeChangesSet);
        System.out.println("Unique to foundInBothSet: " + uniqueToFoundInBothSet);


    }


//    public TIntHashSet compareRecords(String beforeFilePath, String afterFilePath) {
//        TIntHashSet beforeChangesSet = new TIntHashSet();
//        beforeChangesSet.add(1);
//        beforeChangesSet.add(2);
//        beforeChangesSet.add(3);
//        beforeChangesSet.add(4);
//        beforeChangesSet.add(5);
//
//        TIntHashSet foundInBothSet = new TIntHashSet();
//        foundInBothSet.add(1);
//        foundInBothSet.add(2);
//        foundInBothSet.add(3);
//        foundInBothSet.add(4);
//        foundInBothSet.add(5);
//
//
//        // Process the "beforeChanges.txt"
//        try (BufferedReader reader = new BufferedReader(new FileReader(beforeFilePath))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                int key = Integer.parseInt(line.substring(7, 22).trim());
//                beforeChangesSet.add(key);
//            }
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to read before changes file", e);
//        }
//
//        // Process the "afterChangesFile.txt"
//        try (BufferedReader reader = new BufferedReader(new FileReader(afterFilePath))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                int key = Integer.parseInt(line.substring(7, 22).trim());
//                if (beforeChangesSet.contains(key)) {
//                    foundInBothSet.add(key);
//                }
//            }
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to read after changes file", e);
//        }
//
//        // Return the set of keys found in both files
//        return foundInBothSet;
//    }


}