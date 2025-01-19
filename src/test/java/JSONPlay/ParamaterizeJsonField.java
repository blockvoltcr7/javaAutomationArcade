package JSONPlay;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class ParamaterizeJsonField {

    public static void main(String[] args) throws IOException {
        // Sample JSON string with a placeholder
        String jsonString = new String(Files.readAllBytes(Paths.get("src/test/java/JSONPlay/files/param_json.json")));
        // Array with multiple values (only integers or null)
        List<Object> values = Arrays.asList(42, 100, null, 200, 300);

        // Loop over the array and parameterize the JSON field value
        for (Object value : values) {
            try {
                String parameterizedJson = parameterizeJsonField(jsonString, value);
                System.out.println(parameterizedJson);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String parameterizeJsonField(String jsonString, Object parameterValue) throws IOException {
        // Convert the parameter value to a string representation
        String valueString = (parameterValue != null) ? parameterValue.toString() : "null";

        // Replace the placeholder with the parameter value
        String parameterizedJsonString = jsonString.replace("${parameterValue}", valueString);

        // Parse the JSON string to ensure it is valid
        ObjectMapper objectMapper = new ObjectMapper();
        Object jsonObject = objectMapper.readValue(parameterizedJsonString, Object.class);

        // Convert the JSON object back to a formatted JSON string
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
    }
}