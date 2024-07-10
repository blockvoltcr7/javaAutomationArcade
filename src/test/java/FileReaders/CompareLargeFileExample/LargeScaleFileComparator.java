package FileReaders.CompareLargeFileExample;

import java.io.*;
import java.nio.file.*;
import java.util.concurrent.*;
import java.util.zip.CRC32;
public class LargeScaleFileComparator {

    private static final int CHUNK_SIZE = 1024 * 1024; // 1MB chunks
    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        String file1 = "src/test/java/FileReaders/CompareLargeFileExample/File1.txt";
        String file2 = "src/test/java/FileReaders/CompareLargeFileExample/File2.txt";
        compareFiles(file1, file2);
    }

    public static void compareFiles(String file1Path, String file2Path) throws IOException, InterruptedException, ExecutionException {
        long startTime = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        try {
            long file1Size = Files.size(Paths.get(file1Path));
            long file2Size = Files.size(Paths.get(file2Path));

            if (file1Size != file2Size) {
                System.out.println("Files have different sizes. They are not identical.");
                return;
            }

            long chunks = (file1Size + CHUNK_SIZE - 1) / CHUNK_SIZE;
            CompletableFuture<?>[] futures = new CompletableFuture[(int)chunks];

            for (int i = 0; i < chunks; i++) {
                final long start = i * CHUNK_SIZE;
                futures[i] = CompletableFuture.supplyAsync(() -> compareChunk(file1Path, file2Path, start, CHUNK_SIZE), executor);
            }

            CompletableFuture.allOf(futures).join();

            boolean filesIdentical = true;
            for (CompletableFuture<?> future : futures) {
                if (!(boolean)future.get()) {
                    filesIdentical = false;
                    break;
                }
            }

            System.out.println(filesIdentical ? "Files are identical." : "Files are different.");
        } finally {
            executor.shutdown();
        }

        long endTime = System.currentTimeMillis();
        System.out.printf("Comparison completed in %.2f seconds.%n", (endTime - startTime) / 1000.0);
    }

    private static boolean compareChunk(String file1Path, String file2Path, long start, int size) {
        try (RandomAccessFile raf1 = new RandomAccessFile(file1Path, "r");
             RandomAccessFile raf2 = new RandomAccessFile(file2Path, "r")) {

            raf1.seek(start);
            raf2.seek(start);

            byte[] buffer1 = new byte[size];
            byte[] buffer2 = new byte[size];

            int bytesRead1 = raf1.read(buffer1);
            int bytesRead2 = raf2.read(buffer2);

            if (bytesRead1 != bytesRead2) {
                return false;
            }

            CRC32 crc1 = new CRC32();
            CRC32 crc2 = new CRC32();

            crc1.update(buffer1, 0, bytesRead1);
            crc2.update(buffer2, 0, bytesRead2);

            return crc1.getValue() == crc2.getValue();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
