package JSONPlay.files;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class JsonPLay2 {

    public static void main(String[] args) {
        String directoryPath = "src/test/java/JSONPlay/files";
        File directory = new File(directoryPath);

        validateDirectory(directory);

        File[] files = listFiles(directory);
        JSONArray categories = processFiles(files);

        JSONObject result = createJsonObject(categories, "categories");
        System.out.println("Generated JSON:");
        System.out.println(result.toString(4));

        compareJsonArrays(categories, "src/test/java/JSONPlay/files/expectedJsonFile.json","categories");
    }

    private static void validateDirectory(File directory) {
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("The provided path is not a directory");
        }
    }

    private static File[] listFiles(File directory) {
        return directory.listFiles();
    }

    private static JSONArray processFiles(File[] files) {
        JSONArray categories = new JSONArray();
        Arrays.stream(files)
                .map(File::getName)
                .map(fileName -> {
                    String[] parts = fileName.split("_");
                    if (parts.length >= 4) {
                        return parts[3];
                    }
                    return null;
                })
                .filter(part -> part != null)
                .forEach(categories::put);
        return categories;
    }

    private static JSONObject createJsonObject(JSONArray categories, String key) {
        JSONObject result = new JSONObject();
        result.put(key, categories);
        return result;
    }

    private static void compareJsonArrays(JSONArray generatedCategories, String expectedJsonFilePath, String key) {
        try (FileReader reader = new FileReader(expectedJsonFilePath)) {
            JSONObject expectedJson = new JSONObject(new JSONTokener(reader));
            JSONArray expectedCategories = expectedJson.getJSONArray(key);

            Set<String> generatedSet = new HashSet<>();
            for (int i = 0; i < generatedCategories.length(); i++) {
                generatedSet.add(generatedCategories.getString(i));
            }

            Set<String> expectedSet = new HashSet<>();
            for (int i = 0; i < expectedCategories.length(); i++) {
                expectedSet.add(expectedCategories.getString(i));
            }

            Set<String> differences = new HashSet<>(generatedSet);
            differences.removeAll(expectedSet);

            System.out.println("Differences:");
            differences.forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}