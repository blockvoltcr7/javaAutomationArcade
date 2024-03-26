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

        int currentTotalRecords = 15236;

        int lastRunCount = 0;
        double fileControlPercent = 0;
        String regionType = "USA";


        switch (regionType) {
            case "USA":
                lastRunCount = jsonObject.getInt("lastRunTotalCountUS");
                fileControlPercent = jsonObject.getDouble("fileControlPercentUSA");
                break;
            case "CAN":
                lastRunCount = jsonObject.getInt("lastRunTotalCountCAN");
                fileControlPercent = jsonObject.getDouble("fileControlPercentCAN");
                break;
            case "EU":
                lastRunCount = jsonObject.getInt("lastRunTotalCountEU");
                fileControlPercent = jsonObject.getDouble("fileControlPercentEU");
                break;
        }


        System.out.println("fileControlPercent: " + fileControlPercent);
        System.out.println("lastRunCount: " + lastRunCount);
        System.out.println("currentTotalRecords: " + currentTotalRecords);

        System.out.println("lastRunCount * (filecontrol percent / 100): " + lastRunCount * (fileControlPercent / 100));
        long offSetCount = BigDecimal.valueOf(lastRunCount).multiply(BigDecimal.valueOf(fileControlPercent / 100)).longValue();
        Long highRange = lastRunCount + offSetCount;
        Long lowRange = lastRunCount - offSetCount;

        System.out.println("offSetCount: " + offSetCount);
        System.out.println("highRange: " + highRange);
        System.out.println("lowRange: " + lowRange);
        System.out.println("currentTotalRecords: " + currentTotalRecords);

        if(currentTotalRecords > highRange || currentTotalRecords < lowRange){
            System.out.println("false");
            System.out.println("currentTotalRecords: " + currentTotalRecords);
            System.out.println("highRange: " + highRange);
            System.out.println("lowRange: " + lowRange);
            System.out.println("currentTotalRecords > highRange || currentTotalRecords < lowRange");

        }
        else{
            System.out.println("true");
        }


    }


    //function to read a file from a directory
    public static JSONObject readJsonFromFile() throws IOException {
        String path = "src/test/java/FileControlsConfigTest/file-config.json";
        String content = new String(Files.readAllBytes(Paths.get(path)));
        return new JSONObject(content);
    }
}
