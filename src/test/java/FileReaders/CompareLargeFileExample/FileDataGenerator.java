package FileReaders.CompareLargeFileExample;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class FileDataGenerator {

    static String file1 = "src/test/java/FileReaders/CompareLargeFileExample/File1.txt";
    private static final int NUMBER_OF_LINES = 200000;

    public static void main(String[] args) {
        generateFileData(file1, NUMBER_OF_LINES);
    }

    private static void generateFileData(String filePath, int numberOfLines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            Random random = new Random();
            for (int i = 0; i < numberOfLines; i++) {
                String line = generateRandomLine(random);
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String generateRandomLine(Random random) {
        int part1 = 77690; // Static part as per example
        char part2 = 'S'; // Static character as per example, can be randomized if needed
        int part3 = 10000 + random.nextInt(90000); // Random number between 10000 and 99999
        int part4 = 800000 + random.nextInt(100000); // Random number between 800000 and 899999
        return String.format("%d %c %d %d", part1, part2, part3, part4);
    }
}
