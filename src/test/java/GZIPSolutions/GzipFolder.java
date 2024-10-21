package GZIPSolutions;

import java.io.*;
import java.nio.file.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Utility class for compressing and decompressing folders using GZIP.
 */
public class GzipFolder {

    /**
     * Main method to compress a folder into a .gz file.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        String sourceFolder = "src/test/java/GZIPSolutions/data";
        String outputGzipFile = "src/test/java/GZIPSolutions/output/data.gz";
        String outputFolder = "src/test/java/GZIPSolutions/extracted";

        try {
            // Compress the folder
            compressFolderToGzip(sourceFolder, outputGzipFile);
            // Decompress the .gz file
            decompressGzipToFolder(outputGzipFile, outputFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Compresses a folder into a .gz file.
     *
     * @param sourceFolder   The path to the source folder to be compressed
     * @param outputGzipFile The path to the output .gz file
     * @throws IOException If an I/O error occurs
     */
    public static void compressFolderToGzip(String sourceFolder, String outputGzipFile) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(outputGzipFile);
             GZIPOutputStream gos = new GZIPOutputStream(fos);
             BufferedOutputStream bos = new BufferedOutputStream(gos)) {

            Path sourcePath = Paths.get(sourceFolder);
            // Walk through the file tree starting from the source folder
            Files.walk(sourcePath)
                    .filter(path -> !Files.isDirectory(path)) // Filter out directories
                    .forEach(path -> {
                        try (InputStream is = Files.newInputStream(path)) {
                            // Buffer to hold file data
                            byte[] buffer = new byte[1024];
                            int length;
                            // Read from the file and write to the .gz file
                            while ((length = is.read(buffer)) > 0) {
                                bos.write(buffer, 0, length);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }
    }

    /**
     * Decompresses a .gz file into a folder.
     *
     * @param inputGzipFile The path to the input .gz file
     * @param outputFolder  The path to the output folder where files will be extracted
     * @throws IOException If an I/O error occurs
     */
    public static void decompressGzipToFolder(String inputGzipFile, String outputFolder) throws IOException {
        try (FileInputStream fis = new FileInputStream(inputGzipFile);
             GZIPInputStream gis = new GZIPInputStream(fis);
             BufferedInputStream bis = new BufferedInputStream(gis)) {

            Path outputPath = Paths.get(outputFolder, "csvdata.csv");
            // Create directories if they do not exist
            Files.createDirectories(outputPath.getParent());
            try (OutputStream os = Files.newOutputStream(outputPath)) {
                byte[] buffer = new byte[1024];
                int length;
                // Read from the .gz file and write to the output file
                while ((length = bis.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
            }
        }
    }
}