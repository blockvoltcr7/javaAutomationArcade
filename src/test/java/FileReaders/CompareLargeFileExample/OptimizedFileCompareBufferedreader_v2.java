package FileReaders.CompareLargeFileExample;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OptimizedFileCompareBufferedreader_v2 {

    private static final int CHUNK_SIZE = 64 * 1024 * 1024; // 64MB chunks
    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        String file1 = "src/test/java/FileReaders/CompareLargeFileExample/File1.txt";
        String file2 = "src/test/java/FileReaders/CompareLargeFileExample/File2.txt";
        List<String> differences = findFileDifferences(file1, file2);

        System.out.println("Total differences found: " + differences.size());

        for (String mismatch : differences) {
            System.out.println(mismatch);
        }

        System.out.println("Total mismatches: " + differences.size());
    }

    public static List<String> findFileDifferences(String file1Path, String file2Path) throws IOException, InterruptedException, ExecutionException {
        long startTime = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        List<CompletableFuture<List<String>>> futures = new ArrayList<>();
        List<String> allDifferences = new ArrayList<>();

        try (FileChannel channel1 = FileChannel.open(Paths.get(file1Path), StandardOpenOption.READ);
             FileChannel channel2 = FileChannel.open(Paths.get(file2Path), StandardOpenOption.READ)) {

            long file1Size = channel1.size();
            long file2Size = channel2.size();
            long maxSize = Math.max(file1Size, file2Size);

            for (long start = 0; start < maxSize; start += CHUNK_SIZE) {
                final long chunkStart = start;
                final long chunkSize = Math.min(CHUNK_SIZE, maxSize - start);
                futures.add(CompletableFuture.supplyAsync(() -> compareChunk(channel1, channel2, chunkStart, chunkSize), executor));
            }

            for (CompletableFuture<List<String>> future : futures) {
                allDifferences.addAll(future.get());
            }

        } finally {
            executor.shutdown();
        }

        long endTime = System.currentTimeMillis();
        System.out.printf("Comparison completed in %.2f seconds.%n", (endTime - startTime) / 1000.0);

        return allDifferences;
    }

    private static List<String> compareChunk(FileChannel channel1, FileChannel channel2, long start, long size) {
        List<String> differences = new ArrayList<>();
        try {
            MappedByteBuffer buffer1 = channel1.map(FileChannel.MapMode.READ_ONLY, start, Math.min(size, channel1.size() - start));
            MappedByteBuffer buffer2 = channel2.map(FileChannel.MapMode.READ_ONLY, start, Math.min(size, channel2.size() - start));

            int lineNumber = countNewlines(channel1, 0, start) + 1;
            int lineStart = 0;

            int limit = Math.min(buffer1.limit(), buffer2.limit());
            for (int i = 0; i < limit; i++) {
                if (buffer1.get(i) != buffer2.get(i)) {
                    String line1 = extractLine(buffer1, lineStart, i);
                    String line2 = extractLine(buffer2, lineStart, i);

                    if (!compareLines(line1, line2)) {
                        differences.add(line2);
                    }

                    while (i < limit && buffer1.get(i) != '\n') {
                        i++;
                    }
                    lineStart = i + 1;
                }

                if (i < limit && buffer1.get(i) == '\n') {
                    lineNumber++;
                    lineStart = i + 1;
                }
            }

            if (buffer1.limit() != buffer2.limit()) {
                differences.add(String.format("Files have different lengths starting at byte %d (line %d)",
                        start + Math.min(buffer1.limit(), buffer2.limit()), lineNumber));
            }

            return differences;
        } catch (IOException e) {
            e.printStackTrace();
            return differences;
        }
    }

    private static boolean compareLines(String line1, String line2) {
        if (Math.abs(line1.length() - line2.length()) > 1) {
            return false;
        }

        int minLength = Math.min(line1.length(), line2.length());

        for (int i = 0; i < minLength - 1; i++) {
            if (line1.charAt(i) != line2.charAt(i)) {
                return false;
            }
        }

        if (line1.length() != line2.length()) {
            char extraChar = (line1.length() > line2.length()) ? line1.charAt(line1.length() - 1) : line2.charAt(line2.length() - 1);
            return extraChar == ' ';
        }

        char last1 = line1.charAt(line1.length() - 1);
        char last2 = line2.charAt(line2.length() - 1);
        return last1 == last2 || last1 == ' ' || last2 == ' ';
    }

    private static int countNewlines(FileChannel channel, long start, long end) throws IOException {
        int count = 0;
        long size = Math.min(end - start, channel.size() - start);
        MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, start, size);
        for (int i = 0; i < buffer.limit(); i++) {
            if (buffer.get(i) == '\n') {
                count++;
            }
        }
        return count;
    }

    private static String extractLine(MappedByteBuffer buffer, int start, int end) {
        StringBuilder line = new StringBuilder();
        for (int i = start; i <= end && i < buffer.limit(); i++) {
            char ch = (char) buffer.get(i);
            if (ch == '\n') break;
            line.append(ch);
        }
        return line.toString();
    }
}