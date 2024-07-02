package Streams;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Optional;

public class StreamJsonArray {

    public static void main(String[] args) {
        String jsonString = "{\n" +
                "    \"543535\": {\n" +
                "      \"overconlevel\":\"yellow\"\n" +
                "  },\n" +
                "      \"534534\": {\n" +
                "      \"overconlevel\":\"red\"\n" +
                "  },\n" +
                "      \"53453\": {\n" +
                "      \"overconlevel\":\"red\"\n" +
                "  }\n" +
                "}";

        // Parse the JSON string
        JSONObject jsonObject = new JSONObject(jsonString);

        // Get the keys and store them in an array
        String[] keys = JSONObject.getNames(jsonObject);

        // Use Arrays.stream to loop over the keys and filter the keys that have a "yellow" overconlevel
        Optional<String> yellowOverconlevelKey = Arrays.stream(keys)
                .filter(key -> jsonObject.getJSONObject(key).getString("overconlevel").equals("yellow"))
                .findFirst();

        if (yellowOverconlevelKey.isPresent()) {
            // Get the JSON object with a "yellow" overconlevel
            JSONObject yellowOverconlevelObject = jsonObject.getJSONObject(yellowOverconlevelKey.get());
            System.out.println("Yes, there is a JSON object with a yellow overconlevel: " + yellowOverconlevelObject);
        } else {
            System.out.println("No, there is no JSON object with a yellow overconlevel");
        }

        String[] array = new String[]{"element1", "element2", "element3"};

        if("element1".equals(array[0])) {
            System.out.println("Yes, element1 is in the array");
        }
    }
}