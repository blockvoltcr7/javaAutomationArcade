package FileReaders.CompareLargeFileExample;


import java.io.*;
import java.nio.file.*;
import java.util.concurrent.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
public class FileCompareBufferedreader_v3 {

    private static final int CHUNK_SIZE = 1024 * 1024; // 1MB chunks
    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private static final int MAX_DIFFERENCES_TO_REPORT = 100; // Limit the number of differences to report
    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        String file1 = "src/test/java/FileReaders/CompareLargeFileExample/File1.txt";
        String file2 = "src/test/java/FileReaders/CompareLargeFileExample/File2.txt";
        compareFiles(file1, file2);
    }

    public static void compareFiles(String file1Path, String file2Path) throws IOException, InterruptedException, ExecutionException {
        long startTime = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        List<CompletableFuture<List<String>>> futures = new ArrayList<>();
        AtomicInteger differenceCount = new AtomicInteger(0);

        try {
            long file1Size = Files.size(Paths.get(file1Path));
            long file2Size = Files.size(Paths.get(file2Path));

            if (file1Size != file2Size) {
                System.out.println("Files have different sizes. File1: " + file1Size + " bytes, File2: " + file2Size + " bytes");
            }

            long chunks = (Math.max(file1Size, file2Size) + CHUNK_SIZE - 1) / CHUNK_SIZE;

            for (int i = 0; i < chunks; i++) {
                final long start = i * CHUNK_SIZE;
                futures.add(CompletableFuture.supplyAsync(() -> compareChunk(file1Path, file2Path, start, CHUNK_SIZE, differenceCount), executor));
            }

            List<String> allDifferences = new ArrayList<>();
            for (CompletableFuture<List<String>> future : futures) {
                allDifferences.addAll(future.get());
                if (allDifferences.size() >= MAX_DIFFERENCES_TO_REPORT) {
                    break;
                }
            }

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

        long endTime = System.currentTimeMillis();
        System.out.printf("Comparison completed in %.2f seconds.%n", (endTime - startTime) / 1000.0);
    }

    private static List<String> compareChunk(String file1Path, String file2Path, long start, int size, AtomicInteger differenceCount) {
        List<String> differences = new ArrayList<>();
        try (RandomAccessFile raf1 = new RandomAccessFile(file1Path, "r");
             RandomAccessFile raf2 = new RandomAccessFile(file2Path, "r")) {

            raf1.seek(start);
            raf2.seek(start);

            byte[] buffer1 = new byte[size];
            byte[] buffer2 = new byte[size];

            int bytesRead1 = raf1.read(buffer1);
            int bytesRead2 = raf2.read(buffer2);

            int lineNumber = (int)(start / 80) + 1; // Assuming average line length of 80 characters
            int position = 0;

            while (position < bytesRead1 && position < bytesRead2) {
                if (buffer1[position] != buffer2[position]) {
                    String difference = String.format("Difference at byte %d (approx. line %d): File1: %02X, File2: %02X",
                            start + position, lineNumber, buffer1[position], buffer2[position]);
                    differences.add(difference);
                    differenceCount.incrementAndGet();

                    if (differenceCount.get() >= MAX_DIFFERENCES_TO_REPORT) {
                        return differences;
                    }
                }

                if (buffer1[position] == '\n') {
                    lineNumber++;
                }

                position++;
            }

            if (bytesRead1 != bytesRead2) {
                differences.add(String.format("Files have different lengths starting at byte %d (approx. line %d)",
                        start + Math.min(bytesRead1, bytesRead2), lineNumber));
                differenceCount.incrementAndGet();
            }

            return differences;
        } catch (IOException e) {
            e.printStackTrace();
            return differences;
        }
    }
}
