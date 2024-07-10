package JsonParsing;

import io.restassured.path.json.JsonPath;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class ParseJsonArray {


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


        JSONObject jsonObject = new JSONObject(json);
        JSONArray assetSummaries = jsonObject.getJSONObject("diagnosticDetails")
                .getJSONObject("diagnosticSummary")
                .getJSONArray("assetSummaries");

        for (int i = 0; i < assetSummaries.length(); i++) {
            JSONObject assetSummary = assetSummaries.getJSONObject(i);
            if ("GREEN".equals(assetSummary.getString("overconstratedLevel"))) {
                System.out.println(assetSummary.getString("overconstratedLevel"));
            }
        }




    }
}
