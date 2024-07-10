package JsonParsing;
import io.restassured.path.json.JsonPath;
import org.json.JSONObject;
import java.util.List;
import java.util.stream.Collectors;

public class EnhancedJsonParsing {

    public static void main(String[] args) {
        String json = "{\n" +
                "  \"diagnosticDetails\": {\n" +
                "    \"diagnosticSummary\": {\n" +
                "      \"assetSummaries\": [\n" +
                "        {\n" +
                "          \"overconstratedLevel\": \"RED\",\n" +
                "          \"otherProperty\": \"value1\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"overconstratedLevel\": \"GREEN\",\n" +
                "          \"otherProperty\": \"value2\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"overconstratedLevel\": \"YELLOW\",\n" +
                "          \"otherProperty\": \"value3\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"overconstratedLevel\": \"GREEN\",\n" +
                "          \"otherProperty\": \"value4\"\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  }\n" +
                "}";

        JsonPath jsonPath = new JsonPath(json);
//        List<Object> greenObjects = jsonPath.getList("diagnosticDetails.diagnosticSummary.assetSummaries.findAll { it.overconstratedLevel == 'GREEN' }");
        List<Object> greenObjects = jsonPath.getList("diagnosticDetails.diagnosticSummary.assetSummaries.findAll { assetSummaries -> assetSummaries.overconstratedLevel == \"GREEN\" }");

        // Convert to List<JSONObject> for further processing if needed
        List<JSONObject> greenJsonObjects = greenObjects.stream()
                .map(obj -> new JSONObject((java.util.LinkedHashMap)obj))
                .collect(Collectors.toList());

        // Use greenJsonObjects as needed
        greenJsonObjects.forEach(System.out::println);
    }
}