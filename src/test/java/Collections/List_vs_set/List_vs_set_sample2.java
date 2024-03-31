package Collections.List_vs_set;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class List_vs_set_sample2 {

    static class Person {
        String name;
        int age;

        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Person person = (Person) o;
            return age == person.age && Objects.equals(name, person.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, age);
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    public static void main(String[] args) {
        List<Person> list = new ArrayList<>();
        list.add(new Person("John", 30));
        list.add(new Person("Jane", 25));
        list.add(new Person("John", 30));

        Set<Person> set = new HashSet<>();
        set.add(new Person("John", 30));
        set.add(new Person("Jane", 25));
        set.add(new Person("John", 30));

        System.out.println("List: " + list);
        System.out.println("Set: " + set);


        // Convert the list to a JSON array
        JSONArray jsonList = new JSONArray();
        for (Person person : list) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", person.name);
            jsonObject.put("age", person.age);
            jsonList.put(jsonObject);
        }
        System.out.println("JSON List: " + jsonList);

        // Convert the set to a JSON array
        JSONArray jsonSet = new JSONArray();
        for (Person person : set) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", person.name);
            jsonObject.put("age", person.age);
            jsonSet.put(jsonObject);
        }
        System.out.println("JSON Set: " + jsonSet);

    }
}
