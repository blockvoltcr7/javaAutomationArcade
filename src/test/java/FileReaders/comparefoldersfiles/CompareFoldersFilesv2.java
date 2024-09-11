package FileReaders.comparefoldersfiles;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CompareFoldersFilesv2 {

    public static void main(String[] args) throws IOException {
        String oldServerPath = "src/test/java/FileReaders/comparefoldersfiles/folder1/";
        String newServerPath = "src/test/java/FileReaders/comparefoldersfiles/folder2/";

        // Compare folder structures
        compareFolderStructures(oldServerPath, newServerPath);
    }

    // Method to compare folder structures and check for missing files
    public static void compareFolderStructures(String path1, String path2) {
        Set<String> folders1 = listFolders(path1);
        Set<String> folders2 = listFolders(path2);

        // Compare the number of folders
        if (folders1.size() != folders2.size()) {
            System.out.println("The number of folders is different between the two directories.");
        } else {
            System.out.println("The number of folders is the same between the two directories.");
        }

        // Check for missing folders in path2
        folders1.parallelStream().forEach(folder -> {
            if (!folders2.contains(folder)) {
                System.out.println("Folder missing in new server: " + folder);
            }
        });

        // Check for missing folders in path1
        folders2.parallelStream().forEach(folder -> {
            if (!folders1.contains(folder)) {
                System.out.println("Folder missing in old server: " + folder);
            }
        });

        // Check for missing files in each folder
        folders1.parallelStream().forEach(folder -> {
            if (folders2.contains(folder)) {
                compareFilesInFolder(path1 + File.separator + folder, path2 + File.separator + folder);
            }
        });
    }

    // Method to list all folders in a given directory
    public static Set<String> listFolders(String path) {
        File[] listOfFiles = new File(path).listFiles();
        if (listOfFiles == null) {
            System.out.println("The specified path does not exist or is not a directory.");
            return new HashSet<>();
        }
        try (Stream<File> files = Stream.of(listOfFiles)) {
            return files.filter(File::isDirectory)
                        .map(File::getName)
                        .collect(Collectors.toSet());
        }
    }

    // Method to compare files in two folders and check for missing files and byte size
    public static void compareFilesInFolder(String folderPath1, String folderPath2) {
        Set<String> files1 = listFiles(folderPath1);
        Set<String> files2 = listFiles(folderPath2);

        // Check for missing files in folderPath2
        files1.parallelStream().forEach(file -> {
            if (!files2.contains(file)) {
                System.out.println("File missing in new server: " + folderPath2 + File.separator + file);
            } else {
                // Check for byte size mismatch
                File file1 = new File(folderPath1 + File.separator + file);
                File file2 = new File(folderPath2 + File.separator + file);
                if (file1.length() != file2.length()) {
                    System.out.println("File size mismatch: " + folderPath1 + File.separator + file + " and " + folderPath2 + File.separator + file);
                }
            }
        });

        // Check for missing files in folderPath1
        files2.parallelStream().forEach(file -> {
            if (!files1.contains(file)) {
                System.out.println("File missing in old server: " + folderPath1 + File.separator + file);
            }
        });
    }

    // Method to list all files in a given folder
    public static Set<String> listFiles(String path) {
        File[] listOfFiles = new File(path).listFiles();
        if (listOfFiles == null) {
            System.out.println("The specified path does not exist or is not a directory.");
            return new HashSet<>();
        }
        try (Stream<File> files = Stream.of(listOfFiles)) {
            return files.filter(File::isFile)
                        .map(File::getName)
                        .collect(Collectors.toSet());
        }
    }
}