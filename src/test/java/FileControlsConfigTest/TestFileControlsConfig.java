package FileControlsConfigTest;

import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestFileControlsConfig {

    public static void main(String[] args) {



        JSONObject jsonObject = new JSONObject();
        try {
             jsonObject = readJsonFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //last rn count multiplied by (file control percent  / 100)
        int lastRnCount = jsonObject.getInt("lastRunTotalCountUS");
        double percent = jsonObject.getDouble("fileControlPercentUSA");

        Long offSetCount = BigDecimal.valueOf(lastRnCount).multiply(BigDecimal.valueOf(percent / 100)).longValue();
        System.out.println("Offset Count: " + offSetCount);

        //calculate the high range by adding the offset count to the last run count
        Long highRange = lastRnCount + offSetCount;
        System.out.println("High Range: " + highRange);
        //calculate the low range by subtracting the offset count from the last run count
        Long lowRange = lastRnCount - offSetCount;
        System.out.println("Low Range: " + lowRange);


    }


    //function to read a file from a directory
    public static JSONObject readJsonFromFile() throws IOException {
        String path = "src/test/java/FileControlsConfigTest/file-config.json";
        String content = new String(Files.readAllBytes(Paths.get(path)));
        return new JSONObject(content);
    }
}
