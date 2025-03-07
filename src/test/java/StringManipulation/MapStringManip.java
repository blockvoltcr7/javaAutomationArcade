package StringManipulation;


import java.util.HashMap;
import java.util.Map;

public class MapStringManip {

    public static void main(String[] args) {
        //create a map of strings
        Map<String, String> map = new HashMap<>();
        map.put("a", "apple");
        map.put("b", "banana");
        map.put("c", "cherry");

        //get all the keys and values and concatenate them into a single string
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
            sb.append(";");

        }
        System.out.println(sb.toString());


    }
}
