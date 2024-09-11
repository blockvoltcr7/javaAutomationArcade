# Compare Folders and Files

## Overview

This document provides an overview of the Java program designed to compare the folder structures and files between two directories (`folder1` and `folder2`). The program checks for missing folders, missing files, and mismatched file sizes. It is optimized for performance to handle large numbers of folders (over 30,000) by using Java Streams and parallel processing.

## Requirements

1. Ensure that both `folder1` and `folder2` have the same number of folders.
2. Identify any missing folders between `folder1` and `folder2`.
3. Identify any missing files between corresponding folders in `folder1` and `folder2`.
4. Verify that the byte sizes of files with the same name in corresponding folders are the same.

## Implementation

### Main Class: `CompareFoldersFilesv2`

#### `main` Method

- Initializes the paths for `folder1` and `folder2`.
- Calls `compareFolderStructures` to start the comparison.

```java
public static void main(String[] args) throws IOException {
    String oldServerPath = "src/test/java/FileReaders/comparefoldersfiles/folder1/";
    String newServerPath = "src/test/java/FileReaders/comparefoldersfiles/folder2/";

    // Compare folder structures
    compareFolderStructures(oldServerPath, newServerPath);
}
```

#### `compareFolderStructures` Method

- Lists all folders in both directories.
- Compares the number of folders.
- Checks for missing folders in both directories.
- Compares files within each folder for missing files and byte size mismatches.

```java
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
```

### Helper Methods

#### `listFolders` Method

- Lists all folders in a given directory using Java Streams for efficient processing.

```java
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
```

#### `compareFilesInFolder` Method

- Lists all files in two folders.
- Checks for missing files in both folders.
- Compares the byte sizes of files with the same name.

```java
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
```

#### `listFiles` Method

- Lists all files in a given folder using Java Streams for efficient processing.

```java
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
```

## Optimizations

1. **Stream Processing**: Using `Stream.of` and `parallelStream` ensures that the code processes files and directories lazily and in parallel, which is efficient for large datasets.
2. **Memory Management**: Using try-with-resources ensures that streams are closed properly, preventing memory leaks.
3. **Parallel Processing**: Leveraging parallel streams allows the code to utilize multiple CPU cores, speeding up the comparison process.

## Conclusion

This program efficiently compares the folder structures and files between two large directories, ensuring that they have the same number of folders, identifying missing folders and files, and verifying that the byte sizes of files match. The use of Java Streams and parallel processing makes it scalable and performant for large datasets.