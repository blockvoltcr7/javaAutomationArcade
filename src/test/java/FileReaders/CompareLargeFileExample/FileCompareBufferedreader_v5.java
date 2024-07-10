package FileReaders.CompareLargeFileExample;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileCompareBufferedreader_v5 {

    // Constants for chunk size and thread pool size
    private static final int CHUNK_SIZE = 1024 * 1024; // 1MB chunks
    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        // Define file paths
        String file1 = "src/test/java/FileReaders/CompareLargeFileExample/File1.txt";
        String file2 = "src/test/java/FileReaders/CompareLargeFileExample/File2.txt";

        // Compare files and get list of mismatches
        List<String> allMismatches = compareFiles(file1, file2);

        // Iterate over all mismatches and print them
        for (String mismatch : allMismatches) {
            System.out.println(mismatch);
        }
    }

    public static List<String> compareFiles(String file1Path, String file2Path) throws IOException, InterruptedException, ExecutionException {
        long startTime = System.currentTimeMillis();

        // Create a thread pool for parallel processing
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        List<CompletableFuture<List<String>>> futures = new ArrayList<>();
        List<String> allMismatches = new ArrayList<>();

        try {
            // Get file sizes
            long file1Size = Files.size(Paths.get(file1Path));
            long file2Size = Files.size(Paths.get(file2Path));

            // Verify if file sizes do not match
            if (file1Size != file2Size) {
                System.out.println("Files have different sizes. File1: " + file1Size + " bytes, File2: " + file2Size + " bytes");
            }

            // Calculate number of chunks based on the larger file size
            long chunks = (Math.max(file1Size, file2Size) + CHUNK_SIZE - 1) / CHUNK_SIZE;

            // Create and submit tasks for each chunk
            for (int i = 0; i < chunks; i++) {
                final long start = i * CHUNK_SIZE;
                futures.add(CompletableFuture.supplyAsync(() -> compareChunk(file1Path, file2Path, start, CHUNK_SIZE), executor));
            }

            // Collect results from all futures
            for (CompletableFuture<List<String>> future : futures) {
                allMismatches.addAll(future.get());
            }

            // Print results
            if (allMismatches.isEmpty()) {
                System.out.println("Files are identical.");
            } else {
                System.out.println("Files are different. Mismatches found:");
                for (String mismatch : allMismatches) {
                    System.out.println(mismatch);
                }
                System.out.println("Total mismatches: " + allMismatches.size());
            }
        } finally {
            // Shutdown the executor service
            executor.shutdown();
        }

        // Print execution time
        long endTime = System.currentTimeMillis();
        System.out.printf("Comparison completed in %.2f seconds.%n", (endTime - startTime) / 1000.0);

        return allMismatches;
    }

    private static List<String> compareChunk(String file1Path, String file2Path, long start, int size) {
        List<String> mismatches = new ArrayList<>();
        try (RandomAccessFile raf1 = new RandomAccessFile(file1Path, "r");
             RandomAccessFile raf2 = new RandomAccessFile(file2Path, "r")) {

            // Seek to the start position of the chunk in both files
            raf1.seek(start);
            raf2.seek(start);

            // Read chunk into buffers
            byte[] buffer1 = new byte[size];
            byte[] buffer2 = new byte[size];
            int bytesRead1 = raf1.read(buffer1);
            int bytesRead2 = raf2.read(buffer2);

            int lineNumber = 1;
            int lineStart = 0;
            int position = 0;

            // Count newlines from the start of the file to the start of this chunk
            for (long i = 0; i < start; i++) {
                raf1.seek(i);
                if (raf1.read() == '\n') {
                    lineNumber++;
                }
            }

            // Compare bytes in the chunk
            while (position < bytesRead1 && position < bytesRead2) {
                if (buffer1[position] != buffer2[position]) {
                    // Found a difference, extract full lines
                    String line1 = extractFullLine(raf1, start + lineStart);
                    String line2 = extractFullLine(raf2, start + lineStart);

                    // Format and print the mismatch
                    String mismatch = String.format("Mismatch at byte %d (line %d):%nFile1: %s%nFile2: %s",
                            start + position, lineNumber, line1, line2);
                    System.out.println(mismatch);

                    // Add the mismatched line from File2 to the list
                    mismatches.add(line2);

                    // Skip to the end of this line
                    while (position < bytesRead1 && buffer1[position] != '\n') {
                        position++;
                    }
                    lineStart = position + 1;
                }

                // Increment line number if we've reached the end of a line
                if (position < bytesRead1 && buffer1[position] == '\n') {
                    lineNumber++;
                    lineStart = position + 1;
                }

                position++;
            }

            // Check if chunks have different lengths
            if (bytesRead1 != bytesRead2) {
                mismatches.add(String.format("Files have different lengths starting at byte %d (line %d)",
                        start + Math.min(bytesRead1, bytesRead2), lineNumber));
            }

            return mismatches;
        } catch (IOException e) {
            e.printStackTrace();
            return mismatches;
        }
    }

    // Helper method to extract a full line from a file
    private static String extractFullLine(RandomAccessFile raf, long start) throws IOException {
        raf.seek(start);
        StringBuilder line = new StringBuilder();
        int ch;
        while ((ch = raf.read()) != -1 && ch != '\n') {
            line.append((char) ch);
        }
        return line.toString();
    }
}