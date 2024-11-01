package FileReaders.FileValidationChallenge;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.io.FileUtils;

/**
 * The FileComparator class provides functionality to compare the contents of
 * two directories,
 * specifically checking for the existence and equality of files within those
 * directories.
 * It is designed to validate that the contents of an "Expected" directory match
 * those of an "Actual" directory.
 */
public class FileComparator {

    /**
     * The main method serves as the entry point for the application.
     * It initializes the paths for the expected and actual directories and triggers
     * the comparison process.
     *
     * @param args Command line arguments (not used).
     * @throws IOException If an I/O error occurs while accessing the directories.
     */
    public static void main(String[] args) throws IOException {
        // Define the paths for the expected and actual directories
        Path expectedCurrentDir = Paths.get("src/test/java/FileReaders/FileValidationChallenge/expected");
        Path actualCurrentDir = Paths.get("src/test/java/FileReaders/FileValidationChallenge/actual");

        // Trigger the comparison process between the two directories
        compareCurrentDirectories(expectedCurrentDir, actualCurrentDir);
    }

    /**
     * Compares the contents of the expected and actual directories.
     * It checks for the existence of corresponding folders and validates the files
     * within them.
     *
     * @param expectedDir The path to the expected directory.
     * @param actualDir   The path to the actual directory.
     * @throws IOException If an I/O error occurs while accessing the directories or
     *                     files.
     */
    public static void compareCurrentDirectories(Path expectedDir, Path actualDir) throws IOException {
        // Stream through the expected directory to get its folders
        try (Stream<Path> expectedFolders = Files.list(expectedDir)) {
            for (Path folder : expectedFolders.collect(Collectors.toList())) {
                // Check if the current path is a directory
                if (Files.isDirectory(folder)) {
                    Path folderName = folder.getFileName(); // Get the folder name
                    Path actualFolder = actualDir.resolve(folderName); // Resolve the corresponding actual folder

                    // Check if the actual folder exists
                    if (!Files.exists(actualFolder)) {
                        throw new RuntimeException("Folder " + folderName + " does not exist in actual directory");
                    }

                    // Compare all files in the expected and actual folders
                    compareAllFiles(folder, actualFolder);

                    // Compare the specific "xmlplan" folder within the current folder
                    Path expectedXmlplan = folder.resolve("xmlplan");
                    Path actualXmlplan = actualFolder.resolve("xmlplan");

                    // Check if only one xmlplan folder exists
                    if (Files.exists(expectedXmlplan) ^ Files.exists(actualXmlplan)) {
                        throw new RuntimeException("xmlplan folder missing in one of the directories: " + folderName);
                    }

                    // If both exist, perform the comparison
                    if (Files.exists(expectedXmlplan) && Files.exists(actualXmlplan)) {
                        XmlPlanComparator.compareXmlPlanDirectories(expectedXmlplan, actualXmlplan);
                    }
                }
            }
        }
    }

    /**
     * Compares all files in the expected folder with those in the actual folder.
     * It checks for file existence and content equality.
     *
     * @param expectedFolder The path to the expected folder.
     * @param actualFolder   The path to the actual folder.
     * @throws IOException If an I/O error occurs while accessing the files.
     */
    public static void compareAllFiles(Path expectedFolder, Path actualFolder) throws IOException {
        Set<Path> expectedElements, actualElements;

        // Stream through both expected and actual folders to get their file names
        try (Stream<Path> expectedStream = Files.list(expectedFolder);
                Stream<Path> actualStream = Files.list(actualFolder)) {

            expectedElements = expectedStream.map(Path::getFileName).collect(Collectors.toSet()); // Collect expected
                                                                                                  // file names
            actualElements = actualStream.map(Path::getFileName).collect(Collectors.toSet()); // Collect actual file
                                                                                              // names
        }

        // Compare the sets of file names from both folders
        if (!expectedElements.equals(actualElements)) {
            compareFilesSets(expectedElements, actualElements, expectedFolder.getFileName());
        }

        // Iterate through each expected file to compare contents
        for (Path elementPath : expectedElements) {
            Path expectedPath = expectedFolder.resolve(elementPath); // Resolve the expected file path
            Path actualPath = actualFolder.resolve(elementPath); // Resolve the actual file path

            // If the expected path is a directory, recursively compare its contents
            if (Files.isDirectory(expectedPath)) {
                compareAllFiles(expectedPath, actualPath);
            } else {
                // Extract the last two parts of the expected and actual paths
                int expectedNameCount = expectedPath.getNameCount();
                int actualNameCount = actualPath.getNameCount();

                Path expectedSubPath = expectedPath.subpath(Math.max(0, expectedNameCount - 2), expectedNameCount);
                Path actualSubPath = actualPath.subpath(Math.max(0, actualNameCount - 2), actualNameCount);

                System.out.println("Comparing: " + expectedSubPath + " vs " + actualSubPath);
                // Compare the last two parts of the paths
                if (!expectedSubPath.equals(actualSubPath)) {
                    throw new RuntimeException("File path mismatch: " + expectedSubPath + " vs " + actualSubPath);
                }

                // Compare the contents of the files
                if (!FileUtils.contentEquals(expectedPath.toFile(), actualPath.toFile())) {
                    throw new RuntimeException("File content mismatch: " + expectedSubPath);
                }
            }
        }
    }

    /**
     * Compares the sets of files in the expected and actual folders to identify
     * discrepancies.
     * It generates an error message if files are missing in either folder.
     *
     * @param expectedElements The set of file names in the expected folder.
     * @param actualElements   The set of file names in the actual folder.
     * @param folderName       The name of the folder being compared.
     */
    private static void compareFilesSets(Set<Path> expectedElements, Set<Path> actualElements,
            Path folderName) {
        Set<Path> onlyInExpected = new HashSet<>(expectedElements); // Files only in expected
        Set<Path> onlyInActual = new HashSet<>(actualElements); // Files only in actual

        onlyInExpected.removeAll(actualElements); // Remove files found in actual
        onlyInActual.removeAll(expectedElements); // Remove files found in expected

        StringBuilder errorMessage = new StringBuilder(); // Prepare error message
        if (!onlyInExpected.isEmpty()) {
            errorMessage.append("Files only in Expected folder ").append(folderName).append(": ").append(onlyInExpected)
                    .append("\n");
        }

        if (!onlyInActual.isEmpty()) {
            errorMessage.append("Files only in Actual folder ").append(folderName).append(": ").append(onlyInActual);
        }

        // If there are discrepancies, throw an exception with the error message
        if (errorMessage.length() > 0) {
            throw new RuntimeException(errorMessage.toString());
        }
    }

    /**
     * Compares all files in the xmlplan folder without checking if they are
     * directories.
     * It checks for file existence and content equality.
     *
     * @param expectedXmlplan The path to the expected xmlplan folder.
     * @param actualXmlplan   The path to the actual xmlplan folder.
     * @throws IOException If an I/O error occurs while accessing the files.
     */
    public static void compareXmlPlanFiles(Path expectedXmlplan, Path actualXmlplan) throws IOException {
        Set<Path> expectedFiles, actualFiles;

        // Stream through both expected and actual xmlplan folders to get their file
        // names
        try (Stream<Path> expectedStream = Files.list(expectedXmlplan);
                Stream<Path> actualStream = Files.list(actualXmlplan)) {

            expectedFiles = expectedStream.map(Path::getFileName).collect(Collectors.toSet()); // Collect expected file
                                                                                               // names
            actualFiles = actualStream.map(Path::getFileName).collect(Collectors.toSet()); // Collect actual file names
        }

        // Compare the sets of file names from both xmlplan folders
        if (!expectedFiles.equals(actualFiles)) {
            compareFilesSets(expectedFiles, actualFiles, expectedXmlplan.getFileName());
        }

        // Iterate through each expected file to compare contents
        for (Path filePath : expectedFiles) {
            Path expectedFilePath = expectedXmlplan.resolve(filePath); // Resolve the expected file path
            Path actualFilePath = actualXmlplan.resolve(filePath); // Resolve the actual file path

            // Compare the contents of the files
            if (!FileUtils.contentEquals(expectedFilePath.toFile(), actualFilePath.toFile())) {
                throw new RuntimeException("File content mismatch in xmlplan: " + filePath);
            }
        }
    }
}
