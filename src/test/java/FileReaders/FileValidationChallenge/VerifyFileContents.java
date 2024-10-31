package FileReaders.FileValidationChallenge;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.io.FileUtils;

/**
 * This class verifies the contents of two directories (Expected and Actual) by
 * comparing the files
 * within corresponding subdirectories. It checks for the existence of folders
 * in both directories
 * and validates the contents of the files within those folders.
 */
public class VerifyFileContents {

    /**
     * The main method that initiates the verification process.
     * 
     * @param args Command line arguments (not used).
     * @throws IOException If an I/O error occurs while accessing the directories.
     */
    public static void main(String[] args) throws IOException {
        // Define the paths for the Expected and Actual directories
        Path expectedCurrentDir = Paths.get("src/test/java/FileReaders/FileValidationChallenge/expected");
        Path actualCurrentDir = Paths.get("src/test/java/FileReaders/FileValidationChallenge/actual");

        // Stream through the folders in the expected directory
        try (Stream<Path> expectedFolders = Files.list(expectedCurrentDir)) {
            for (Path folder : expectedFolders.collect(Collectors.toList())) {
                // Check if the current path is a directory
                if (Files.isDirectory(folder)) {
                    Path folderName = folder.getFileName();
                    // The resolve function combines the current path with the given path, creating
                    // a new path that points to the specified folder within the actual directory.
                    Path actualFolder = actualCurrentDir.resolve(folderName);

                    // Check if corresponding folder exists in actual
                    if (!Files.exists(actualFolder)) {
                        throw new RuntimeException("Folder " + folderName + " does not exist in actual directory");
                    }

                    // Compare all files in the folders
                    compareAllFiles(folder, actualFolder);

                    // Check for xmlplan folder specifically
                    Path expectedXmlplan = folder.resolve("xmlplan");
                    Path actualXmlplan = actualFolder.resolve("xmlplan");

                    if (!Files.exists(expectedXmlplan) || !Files.exists(actualXmlplan)) {
                        throw new RuntimeException("xmlplan folder missing in " + folderName);
                    }

                    // Compare xmlplan folder contents
                    compareAllFiles(expectedXmlplan, actualXmlplan);
                }
            }
        }
    }

    /**
     * Validates the contents of two folders by comparing the files within them.
     * 
     * @param expectedFolder The path to the expected folder.
     * @param actualFolder The path to the actual folder.
     * @throws IOException If an I/O error occurs while accessing the files.
     */
    private static void compareAllFiles(Path expectedFolder, Path actualFolder) throws IOException {
        Set<Path> expectedElements, actualElements;

        try (Stream<Path> expectedStream = Files.list(expectedFolder);
                Stream<Path> actualStream = Files.list(actualFolder)) {

            expectedElements = expectedStream.map(Path::getFileName).collect(Collectors.toSet());
            actualElements = actualStream.map(Path::getFileName).collect(Collectors.toSet());
        }

        // Compare file sets
        if (!expectedElements.equals(actualElements)) {
            compareFilesSetsManually(expectedElements, actualElements, expectedFolder.getFileName());
        }

        // Compare contents of each file
        for (Path elementPath : expectedElements) {
            Path expectedPath = expectedFolder.resolve(elementPath);
            Path actualPath = actualFolder.resolve(elementPath);

            if (Files.isDirectory(expectedPath)) {
                // Recursively compare directories
                compareAllFiles(expectedPath, actualPath);
            } else {
                // Compare file contents
                if (!FileUtils.contentEquals(expectedPath.toFile(), actualPath.toFile())) {
                    throw new RuntimeException("File content mismatch: " + elementPath);
                }
            }
        }
    }

    private static void compareFilesSetsManually(Set<Path> expectedElements, Set<Path> actualElements, Path folderName) {
        Set<Path> onlyInExpected = new HashSet<>(expectedElements);
        onlyInExpected.removeAll(actualElements);

        Set<Path> onlyInActual = new HashSet<>(actualElements);
        onlyInActual.removeAll(expectedElements);

        StringBuilder errorMessage = new StringBuilder();
        if (!onlyInExpected.isEmpty()) {
            errorMessage.append("Files only in Expected folder ").append(folderName).append(": ").append(onlyInExpected)
                    .append("\n");
        }

        if (!onlyInActual.isEmpty()) {
            errorMessage.append("Files only in Actual folder ").append(folderName).append(": ").append(onlyInActual);
        }

        if (errorMessage.length() > 0) {
            throw new RuntimeException(errorMessage.toString());
        }
    }
}
