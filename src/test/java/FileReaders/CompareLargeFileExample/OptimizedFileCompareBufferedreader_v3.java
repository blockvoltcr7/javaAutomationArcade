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

public class OptimizedFileCompareBufferedreader_v3 {
    private static final int CHUNK_SIZE = 64 * 1024 * 1024; // 64MB chunks
    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        String file1 = "src/test/java/FileReaders/CompareLargeFileExample/File1.txt";
        String file2 = "src/test/java/FileReaders/CompareLargeFileExample/File2.txt";

        List<String> allMismatches = compareFiles(file1, file2);

        for (String mismatch : allMismatches) {
            System.out.println(mismatch);
        }

        System.out.println("Total mismatches: " + allMismatches.size());
    }

    public static List<String> compareFiles(String file1Path, String file2Path) throws IOException, InterruptedException, ExecutionException {
        long startTime = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        List<CompletableFuture<List<String>>> futures = new ArrayList<>();
        List<String> allMismatches = new ArrayList<>();

        try (FileChannel channel1 = FileChannel.open(Paths.get(file1Path), StandardOpenOption.READ);
             FileChannel channel2 = FileChannel.open(Paths.get(file2Path), StandardOpenOption.READ)) {

            long file1Size = channel1.size();
            long file2Size = channel2.size();

            if (file1Size != file2Size) {
                System.out.println("Files have different sizes. File1: " + file1Size + " bytes, File2: " + file2Size + " bytes");
            }

            long minSize = Math.min(file1Size, file2Size);
            long chunks = (minSize + CHUNK_SIZE - 1) / CHUNK_SIZE;

            for (int i = 0; i < chunks; i++) {
                final long start = i * CHUNK_SIZE;
                final long end = Math.min(start + CHUNK_SIZE, minSize);
                futures.add(CompletableFuture.supplyAsync(() -> compareChunk(channel1, channel2, start, end), executor));
            }

            for (CompletableFuture<List<String>> future : futures) {
                allMismatches.addAll(future.get());
            }

            // Handle the case where one file is longer than the other
//            if (file1Size != file2Size) {
//                allMismatches.add(String.format("File size mismatch. File1: %d bytes, File2: %d bytes", file1Size, file2Size));
//            }

        } finally {
            executor.shutdown();
        }

        long endTime = System.currentTimeMillis();
        System.out.printf("Comparison completed in %.2f seconds.%n", (endTime - startTime) / 1000.0);

        return allMismatches;
    }

    private static List<String> compareChunk(FileChannel channel1, FileChannel channel2, long start, long end) {
        List<String> mismatches = new ArrayList<>();
        try {
            long size1 = Math.min(end - start, channel1.size() - start);
            long size2 = Math.min(end - start, channel2.size() - start);
            MappedByteBuffer buffer1 = channel1.map(FileChannel.MapMode.READ_ONLY, start, size1);
            MappedByteBuffer buffer2 = channel2.map(FileChannel.MapMode.READ_ONLY, start, size2);

            int lineNumber = countNewlines(channel1, 0, start) + 1;
            int lineStart = 0;

            int limit = (int) Math.min(size1, size2);
            for (int i = 0; i < limit; i++) {
                if (buffer1.get(i) != buffer2.get(i)) {
                    String line1 = extractLine(buffer1, lineStart, (int)size1);
                    String line2 = extractLine(buffer2, lineStart, (int)size2);

                    if (!line1.trim().equals(line2.trim())) {
                        mismatches.add(line2);
                    }

                    // Skip to the end of this line
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

            return mismatches;
        } catch (IOException e) {
            e.printStackTrace();
            return mismatches;
        }
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
        for (int i = start; i < end; i++) {
            char ch = (char) buffer.get(i);
            if (ch == '\n') break;
            line.append(ch);
        }
        return line.toString();
    }
}