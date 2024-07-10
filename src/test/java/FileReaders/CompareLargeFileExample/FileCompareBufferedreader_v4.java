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
import java.util.concurrent.atomic.AtomicInteger;

public class FileCompareBufferedreader_v4 {

    // Constants for chunk size, thread pool size, and maximum differences to report
    private static final int CHUNK_SIZE = 1024 * 1024; // 1MB chunks
    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private static final int MAX_DIFFERENCES_TO_REPORT = 100; // Limit the number of differences to report

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        // Define file paths and call the compareFiles method
        String file1 = "src/test/java/FileReaders/CompareLargeFileExample/File1.txt";
        String file2 = "src/test/java/FileReaders/CompareLargeFileExample/File2.txt";
        compareFiles(file1, file2);
    }

    public static void compareFiles(String file1Path, String file2Path) throws IOException, InterruptedException, ExecutionException {
        long startTime = System.currentTimeMillis();

        // Create a thread pool for parallel processing
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        List<CompletableFuture<List<String>>> futures = new ArrayList<>();
        AtomicInteger differenceCount = new AtomicInteger(0);

        try {
            // Get file sizes
            long file1Size = Files.size(Paths.get(file1Path));
            long file2Size = Files.size(Paths.get(file2Path));

            // Check if file sizes are different
            if (file1Size != file2Size) {
                System.out.println("Files have different sizes. File1: " + file1Size + " bytes, File2: " + file2Size + " bytes");
            }

            // Calculate number of chunks based on the larger file size
            long chunks = (Math.max(file1Size, file2Size) + CHUNK_SIZE - 1) / CHUNK_SIZE;

            // Create and submit tasks for each chunk
            for (int i = 0; i < chunks; i++) {
                final long start = i * CHUNK_SIZE;
                futures.add(CompletableFuture.supplyAsync(() -> compareChunk(file1Path, file2Path, start, CHUNK_SIZE, differenceCount), executor));
            }

            // Collect results from all tasks
            List<String> allDifferences = new ArrayList<>();
            for (CompletableFuture<List<String>> future : futures) {
                allDifferences.addAll(future.get());
                if (allDifferences.size() >= MAX_DIFFERENCES_TO_REPORT) {
                    break;
                }
            }

            // Report results
            if (allDifferences.isEmpty()) {
                System.out.println("Files are identical.");
            } else {
                System.out.println("Files are different. Showing up to " + MAX_DIFFERENCES_TO_REPORT + " differences:");
                allDifferences.stream().limit(MAX_DIFFERENCES_TO_REPORT).forEach(System.out::println);
                if (differenceCount.get() > MAX_DIFFERENCES_TO_REPORT) {
                    System.out.println("... and " + (differenceCount.get() - MAX_DIFFERENCES_TO_REPORT) + " more differences.");
                }
            }
        } finally {
            executor.shutdown();
        }

        // Print execution time
        long endTime = System.currentTimeMillis();
        System.out.printf("Comparison completed in %.2f seconds.%n", (endTime - startTime) / 1000.0);
    }

    private static List<String> compareChunk(String file1Path, String file2Path, long start, int size, AtomicInteger differenceCount) {
        List<String> differences = new ArrayList<>();
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

                    // Format and add the difference to the list
                    String difference = String.format("Difference at byte %d (line %d):%nFile1: %s%nFile2: %s",
                            start + position, lineNumber, line1, line2);
                    differences.add(difference);
                    differenceCount.incrementAndGet();

                    // Check if we've reached the maximum number of differences to report
                    if (differenceCount.get() >= MAX_DIFFERENCES_TO_REPORT) {
                        return differences;
                    }

                    // Skip to the end of this line to avoid reporting multiple differences on the same line
                    while (position < bytesRead1 && buffer1[position] != '\n') {
                        position++;
                    }
                    lineStart = position + 1;
                }

                // Increment line number if we've reached the end of a line
                if (buffer1[position] == '\n') {
                    lineNumber++;
                    lineStart = position + 1;
                }

                position++;
            }

            // Check if chunks have different lengths
            if (bytesRead1 != bytesRead2) {
                differences.add(String.format("Files have different lengths starting at byte %d (line %d)",
                        start + Math.min(bytesRead1, bytesRead2), lineNumber));
                differenceCount.incrementAndGet();
            }

            return differences;
        } catch (IOException e) {
            e.printStackTrace();
            return differences;
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