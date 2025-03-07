package StringManipulation;

                import org.json.JSONArray;
                import org.json.JSONObject;

                public class JsonManipStringParse {

                    public static void main(String[] args) {
                        String json = "{\n" +
                                "  \"array\": [\n" +
                                "    {\n" +
                                "      \"innerArray\": [\n" +
                                "        {\n" +
                                "          \"layer1\": {\n" +
                                "            \"layer2\": {\n" +
                                "              \"key\": \"value1\"\n" +
                                "            }\n" +
                                "          }\n" +
                                "        },\n" +
                                "        {\n" +
                                "          \"layer1\": {\n" +
                                "            \"layer2\": {\n" +
                                "              \"key\": \"value2\"\n" +
                                "            }\n" +
                                "          }\n" +
                                "        },\n" +
                                "        {\n" +
                                "          \"layer1\": {\n" +
                                "            \"layer2\": {\n" +
                                "              \"key\": \"value3\"\n" +
                                "            }\n" +
                                "          }\n" +
                                "        }\n" +
                                "      ]\n" +
                                "    }\n" +
                                "  ]\n" +
                                "}";

                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray array = jsonObject.getJSONArray("array");
                        JSONObject firstElement = array.getJSONObject(0);
                        JSONArray innerArray = firstElement.getJSONArray("innerArray");

                        System.out.println("InnerArray: " + innerArray.toString());

                        //loop over the jsonarray
                        for (int i = 0; i < innerArray.length(); i++) {
                            JSONObject innerArrayElement = innerArray.getJSONObject(i);
                            JSONObject layer1 = innerArrayElement.getJSONObject("layer1");
                            JSONObject layer2 = layer1.getJSONObject("layer2");
                            String key = layer2.getString("key");
                            System.out.println("Key: " + key);
                        }
                    }
                }