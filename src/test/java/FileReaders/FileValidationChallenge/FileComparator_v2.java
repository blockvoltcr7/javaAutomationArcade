package FileReaders.FileValidationChallenge;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The FileComparator class provides functionality to compare the contents of
 * two directories, specifically checking for the existence and equality of files within those directories.
 * It is designed to validate that the contents of an "Expected" directory match those of an "Actual" directory.
 */
public class FileComparator_v2 {

    /**
     * The main method serves as the entry point for the application.
     * It initializes the paths for the expected and actual directories and triggers the comparison process.
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
     * It checks for the existence of corresponding folders and validates the files within them.
     *
     * @param expectedDir The path to the expected directory.
     * @param actualDir   The path to the actual directory.
     * @throws IOException If an I/O error occurs while accessing the directories or files.
     */
    public static void compareCurrentDirectories(Path expectedDir, Path actualDir) throws IOException {
        try (Stream<Path> expectedFolders = Files.list(expectedDir)) {
            expectedFolders.forEach(folder -> {
                if (Files.isDirectory(folder)) {
                    Path folderName = folder.getFileName();
                    Path actualFolder = actualDir.resolve(folderName);

                    if (!Files.exists(actualFolder)) {
                        throw new RuntimeException("Folder " + folderName + " does not exist in actual directory");
                    }

                    try {
                        compareAllFiles(folder, actualFolder);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    Path expectedXmlplan = folder.resolve("xmlplan");
                    Path actualXmlplan = actualFolder.resolve("xmlplan");

                    if (Files.exists(expectedXmlplan) ^ Files.exists(actualXmlplan)) {
                        throw new RuntimeException("xmlplan folder missing in one of the directories: " + folderName);
                    }

                    if (Files.exists(expectedXmlplan) && Files.exists(actualXmlplan)) {
                        try {
                            XmlPlanComparator.compareXmlPlanDirectories(expectedXmlplan, actualXmlplan);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
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
        Set<Path> expectedElements = new HashSet<>();
        Set<Path> actualElements = new HashSet<>();

        try (Stream<Path> expectedStream = Files.list(expectedFolder);
             Stream<Path> actualStream = Files.list(actualFolder)) {
            expectedStream.map(Path::getFileName).forEach(expectedElements::add);
            actualStream.map(Path::getFileName).forEach(actualElements::add);
        }

        if (!expectedElements.equals(actualElements)) {
            compareFilesSets(expectedElements, actualElements, expectedFolder.getFileName());
        }

        for (Path elementPath : expectedElements) {
            Path expectedPath = expectedFolder.resolve(elementPath);
            Path actualPath = actualFolder.resolve(elementPath);

            if (Files.isDirectory(expectedPath)) {
                compareAllFiles(expectedPath, actualPath);
            } else {
                Path expectedSubPath = expectedPath.subpath(Math.max(0, expectedPath.getNameCount() - 2), expectedPath.getNameCount());
                Path actualSubPath = actualPath.subpath(Math.max(0, actualPath.getNameCount() - 2), actualPath.getNameCount());

                System.out.println("Comparing: " + expectedSubPath + " vs " + actualSubPath);

                if (!expectedSubPath.equals(actualSubPath)) {
                    throw new RuntimeException("File path mismatch: " + expectedSubPath + " vs " + actualSubPath);
                }

                if (Files.size(expectedPath) != Files.size(actualPath)) {
                    throw new RuntimeException("File size mismatch: " + expectedSubPath);
                }

                if (Files.mismatch(expectedPath, actualPath) != -1) {
                    throw new RuntimeException("File content mismatch: " + expectedSubPath);
                }
            }
        }
    }

    /**
     * Compares the sets of files in the expected and actual folders to identify discrepancies.
     * It generates an error message if files are missing in either folder.
     *
     * @param expectedElements The set of file names in the expected folder.
     * @param actualElements   The set of file names in the actual folder.
     * @param folderName       The name of the folder being compared.
     */
    private static void compareFilesSets(Set<Path> expectedElements, Set<Path> actualElements, Path folderName) {
        Set<Path> onlyInExpected = new HashSet<>(expectedElements);
        onlyInExpected.removeAll(actualElements);
        Set<Path> onlyInActual = new HashSet<>(actualElements);
        onlyInActual.removeAll(expectedElements);

        StringBuilder errorMessage = new StringBuilder();
        if (!onlyInExpected.isEmpty()) {
            errorMessage.append("Files only in Expected folder ").append(folderName).append(": ").append(onlyInExpected).append("\n");
        }
        if (!onlyInActual.isEmpty()) {
            errorMessage.append("Files only in Actual folder ").append(folderName).append(": ").append(onlyInActual);
        }
        if (errorMessage.length() > 0) {
            throw new RuntimeException(errorMessage.toString());
        }
    }
}