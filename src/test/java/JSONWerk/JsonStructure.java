package JSONWerk;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class JsonStructure {

    public static void main(String[] args) {
        // Create JSON object with synthetic data
        JSONObject dataset = createSyntheticData();

        // Print out each array item in each sheet
        printDataset(dataset);

        // Print the full JSON object to the console
        System.out.println(dataset.toString(4));

        // Write the JSON object to a file with a unique name
        writeJsonToFile(dataset, "src/test/java/JSONWerk/output");
    }

    /**
     * Creates a JSON object with synthetic data.
     *
     * @return JSONObject containing the synthetic data
     */
    public static JSONObject createSyntheticData() {
        JSONObject dataset = new JSONObject();

        for (int i = 1; i <= 7; i++) {
            JSONArray sheet = new JSONArray();
            for (int j = 1; j <= 5; j++) {
                JSONObject row = new JSONObject();
                row.put("column1", "value" + ((i - 1) * 5 + j));
                row.put("column2", "value" + ((i - 1) * 5 + j + 1));
                row.put("column3", "value" + ((i - 1) * 5 + j + 2));
                row.put("rowNumber", j); // Add rowNumber column
                sheet.put(row);
            }
            dataset.put("sheet" + i, sheet);
        }

        JSONObject root = new JSONObject();
        root.put("dataset", dataset);
        return root;
    }

    /**
     * Prints out each array item in each sheet of the dataset.
     *
     * @param dataset JSONObject containing the dataset
     */
    public static void printDataset(JSONObject dataset) {
        JSONObject sheets = dataset.getJSONObject("dataset");

        for (String sheetName : sheets.keySet()) {
            System.out.println("Sheet: " + sheetName);
            JSONArray sheet = sheets.getJSONArray(sheetName);
            for (int i = 0; i < sheet.length(); i++) {
                JSONObject row = sheet.getJSONObject(i);
                System.out.println("Row " + (i + 1) + ": " + row);
            }
        }
    }

    /**
     * Writes the JSON object to a file with a unique name.
     *
     * @param jsonObject The JSON object to write
     * @param directory  The directory where the file will be written
     */
    public static void writeJsonToFile(JSONObject jsonObject, String directory) {
        String uniqueFileName = directory + "/dataset_" + UUID.randomUUID().toString() + ".json";
        try (FileWriter file = new FileWriter(uniqueFileName)) {
            file.write(jsonObject.toString(4)); // Pretty print with an indent factor of 4
            System.out.println("JSON file created: " + uniqueFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}