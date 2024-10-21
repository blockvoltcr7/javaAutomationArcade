package JSONPlay.files;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;

public class JsonPLay2 {

    public static void main(String[] args) {
        // Define the directory path
        String directoryPath = "src/test/java/JSONPlay/files";
        File directory = new File(directoryPath);

        // Check if the provided path is a directory
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("The provided path is not a directory");
        }

        // List all files in the directory
        File[] files = directory.listFiles();
        JSONArray categories = new JSONArray();

        // Stream through the files, process each file name
        Arrays.stream(files)
                .map(File::getName) // Get the name of each file
                .map(fileName -> {
                    // Split the file name by underscore
                    String[] parts = fileName.split("_");
                    // Return the fourth part if it exists
                    if (parts.length >= 4) {
                        return parts[3];
                    }
                    return null;
                })
                .filter(part -> part != null) // Filter out null values
                .forEach(categories::put); // Add each part to the JSON array

        // Create the final JSON object with the categories array
        JSONObject result = new JSONObject();
        result.put("categories", categories);

        // Print the resulting JSON object
        System.out.println(result.toString(4));
    }
}