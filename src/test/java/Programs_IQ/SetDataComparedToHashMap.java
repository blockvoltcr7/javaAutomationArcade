package Programs_IQ;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SetDataComparedToHashMap {


    public static void main(String[] args) {
        Set<Integer> set = new HashSet<>();

        set.add(1);
        set.add(2);
        set.add(3);
        set.add(4);


        Map<Integer,String> map = new HashMap<>();

        map.put(1,"One");
        map.put(2,"Two");
        map.put(3,"Three");
        map.put(4,"Four");
        map.put(5,"Five");

        findMissingAccounts(set,map);
    }

    //function to find the missing accounts integers in the set and map
    public static void findMissingAccounts(Set<Integer> set, Map<Integer,String> map){
        for(int i=1; i<=5; i++){
            if(!set.contains(i)){
                System.out.println("Missing account in set: "+i);
            }
            if(!map.containsKey(i)){
                System.out.println("Missing account in map: "+i);
            }
        }
    }
}
