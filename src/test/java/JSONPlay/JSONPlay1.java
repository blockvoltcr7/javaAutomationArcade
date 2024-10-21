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
