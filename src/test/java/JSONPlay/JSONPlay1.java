package JSONPlay;

import java.io.File;

public class JSONPlay1 {

    public static void main(String[] args) {
        String directoryPath = "src/test/java/JSONPlay/files";
        String region = "CA";
        String count = "1";
        String diversification = "div";

        int matchingFileCount = getMatchingFileCount(directoryPath, region, count, diversification);
        System.out.println("Number of matching files: " + matchingFileCount);
    }

    /**
     * Returns the number of files in the provided directory that match the specified pattern.
     *
     * @param directoryPath the path to the directory
     * @param region        the region to match
     * @param count         the count to match
     * @param diversification the diversification to match
     * @return the number of matching files
     */
    public static int getMatchingFileCount(String directoryPath, String region, String count, String diversification) {
        File directory = new File(directoryPath);
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("The provided path is not a directory");
        }

        String pattern = region + "_" + count + "_" + diversification;
        File[] matchingFiles = directory.listFiles((dir, name) -> name.contains(pattern));

        return matchingFiles != null ? matchingFiles.length : 0;
    }
}
