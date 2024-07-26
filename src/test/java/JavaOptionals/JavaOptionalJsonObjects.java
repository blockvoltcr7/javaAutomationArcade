package JavaOptionals;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.json.JSONObject.NULL;

public class JavaOptionalJsonObjects {

    /**
     * imagine a function that takes in a JSONObject and and Enum and returns a JSON Array
     * the return statement is an Optional.Of Nullable that takes in the JSONObject then uses .filter then .map, then another .map
     * then there is another return statement that returns a JSON Array object
     */

    public enum JsonField {
        ITEMS,
        DATA,
        ELEMENTS
    }

    public static void main(String[] args) {
        JSONObject inputJson = new JSONObject("{\"items\": [1, 2, 3]}");
        JSONArray result = JavaOptionalJsonObjects.processJson(inputJson, JavaOptionalJsonObjects.JsonField.ITEMS);
        System.out.println(result); // Outputs: [1, 2, 3]

        JSONObject invalidJson = new JSONObject("{\"data\": \"not an array\"}");
        JSONArray nullResult = JavaOptionalJsonObjects.processJson(invalidJson, JavaOptionalJsonObjects.JsonField.ITEMS);
        System.out.println(nullResult); // Outputs: null

// It's a good practice to check for null before using the result
        if (nullResult != null) {
            // Process the array
        } else {
            // Handle the null case
            System.out.println("The specified field was not found or was not a JSONArray");
        }
    }

    /**
     * Processes a JSONObject based on a specified field and returns a JSONArray.
     *
     * @param jsonObject The input JSONObject to process.
     * @param field The enum specifying which field to extract from the JSONObject.
     * @return A JSONArray containing the processed data, or null if the field is not found or is not an array.
     */
    public static JSONArray processJson(JSONObject jsonObject, JsonField field) {
        return Optional.ofNullable(jsonObject)
                .filter(json -> json.has(field.name().toLowerCase()))
                .map(json -> json.opt(field.name().toLowerCase()))
                .filter(obj -> obj instanceof JSONArray)
                .map(obj -> (JSONArray) obj)
                .orElse(null);
    }


    /**
     * Extends diagnostic rules for a specific sector by adding a sector-specific label to each rule.
     *
     * This method processes a JSONObject containing diagnostic rules and enhances each rule
     * with a label specific to the given sector type. It creates a new JSONArray containing
     * these extended rules.
     *
     * @param rulesAPI A JSONObject containing the original diagnostic rules. Expected to have
     *                 a "diagRules" JSONArray.
     * @param sectorType An enum value representing the sector type for which rules are being processed.
     *
     * @return A JSONArray containing the extended diagnostic rules, each with an added "ruleLabel" field.
     *         Returns null if the input is null, invalid, or if processing fails at any stage.
     *
     * @throws JSONException If there's an error parsing the JSON or accessing JSON elements.
     *                       This is an unchecked exception thrown by the JSON library.
     *
     * @see SectorTypeEnum
     * @see JSONObject
     * @see JSONArray
     */
//    public static JSONArray getExtendedRulesBySector(JSONObject rulesAPI, SectorTypeEnum sectorType) {
//        // Method implementation remains the same as in the previous response
//        var ruleLabel = getRulesLabel(sectorType);
//
//        return Optional.ofNullable(rulesAPI)
//                .filter(div -> !NULL.equals(div))
//                .map(div -> (JSONObject) div)
//                .map(rulesJsonObj -> {
//                    JSONArray extendedRulesJSONArray = new JSONArray();
//                    JSONArray diagRules = new JSONArray(rulesJsonObj.getJSONArray("diagRules"));
//
//                    IntStream.range(0, diagRules.length()).forEach(i -> {
//                        JSONObject tempHolderObject = new JSONObject(diagRules.getJSONObject(i).toString());
//                        tempHolderObject.put("ruleLabel", ruleLabel);
//                        extendedRulesJSONArray.put(tempHolderObject);
//                    });
//
//                    return extendedRulesJSONArray;
//                })
//                .orElse(null);
//    }

}
