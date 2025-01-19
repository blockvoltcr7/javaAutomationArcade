package StringManipulation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileNamesStringManipulation {

    public static void main(String[] args) {
        String filename = "20241205-09.40.52-7sb9234-8cdf-87b8-as98723-100-0-0.gson.gz";
        String uniqueId = extractUniqueId(filename);
        System.out.println("Unique ID: " + uniqueId);
    }

    public static String extractUniqueId(String filename) {
        Pattern pattern = Pattern.compile("^[^-]+-[^-]+-(.*?)-[^-]+-[^-]+-[^-]+\\.gson\\.gz$");
        Matcher matcher = pattern.matcher(filename);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

}

