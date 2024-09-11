package FileReaders.comparefoldersfiles;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class CompareFoldersFiles {

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
        for (String folder : folders1) {
            if (!folders2.contains(folder)) {
                System.out.println("Folder missing in new server: " + folder);
            }
        }

        // Check for missing folders in path1
        for (String folder : folders2) {
            if (!folders1.contains(folder)) {
                System.out.println("Folder missing in old server: " + folder);
            }
        }

        // Check for missing files in each folder
        for (String folder : folders1) {
            if (folders2.contains(folder)) {
                compareFilesInFolder(path1 + folder, path2 + folder);
            }
        }
    }

    // Method to list all folders in a given directory
    public static Set<String> listFolders(String path) {
        Set<String> folderNames = new HashSet<>();
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isDirectory()) {
                    folderNames.add(file.getName());
                }
            }
        } else {
            System.out.println("The specified path does not exist or is not a directory.");
        }
        return folderNames;
    }

    // Method to compare files in two folders and check for missing files and byte size
    public static void compareFilesInFolder(String folderPath1, String folderPath2) {
        Set<String> files1 = listFiles(folderPath1);
        Set<String> files2 = listFiles(folderPath2);

        // Check for missing files in folderPath2
        for (String file : files1) {
            if (!files2.contains(file)) {
                System.out.println("File missing in new server: " + folderPath2 + "/" + file);
            } else {
                // Check for byte size mismatch
                File file1 = new File(folderPath1 + "/" + file);
                File file2 = new File(folderPath2 + "/" + file);
                if (file1.length() != file2.length()) {
                    System.out.println("File size mismatch: " + folderPath1 + "/" + file + " and " + folderPath2 + "/" + file);
                }
            }
        }

        // Check for missing files in folderPath1
        for (String file : files2) {
            if (!files1.contains(file)) {
                System.out.println("File missing in old server: " + folderPath1 + "/" + file);
            }
        }
    }

    // Method to list all files in a given folder
    public static Set<String> listFiles(String path) {
        Set<String> fileNames = new HashSet<>();
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    fileNames.add(file.getName());
                }
            }
        } else {
            System.out.println("The specified path does not exist or is not a directory.");
        }
        return fileNames;
    }
}