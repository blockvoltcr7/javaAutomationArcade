# Java Collections: List vs Set

## Introduction

Understanding the difference between `List` and `Set` in Java is fundamental for any Java developer, as both interfaces play a critical role in collection framework handling. This document aims to highlight the key differences and provide simple examples to illustrate their usage.

## List
- **Definition**: A `List` is an ordered collection also known as a sequence. Users have precise control over where in the list each element is inserted.
- **Access**: Elements can be accessed by their integer index (position in the list), and users can search for elements within the list.
- **Duplicates**: Lists typically allow duplicate elements.

## Set
- **Definition**: A `Set` is a collection that cannot contain duplicate elements. It models the mathematical set abstraction.
- **Uniqueness**: Sets contain no pair of elements `e1` and `e2` such that `e1.equals(e2)`, and at most one null element.
- **Order**: Most Set implementations do not maintain the order of their elements.

## Example

```java
import java.util.*;

public class ListSetExample {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("Apple");
        list.add("Banana");
        list.add("Apple");

        Set<String> set = new HashSet<>();
        set.add("Apple");
        set.add("Banana");
        set.add("Apple");

        System.out.println("List: " + list);
        System.out.println("Set: " + set);
    }
}
