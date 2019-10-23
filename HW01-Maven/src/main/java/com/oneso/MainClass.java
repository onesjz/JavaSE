package com.oneso;

import com.google.common.collect.*;

import java.util.Map;

public class MainClass {

  public static void main(String[] args) {

    Multiset<String> multiset = HashMultiset.create();
    multiset.add("test");
    multiset.add("test");
    multiset.add("test2");

    Multimap<String, Integer> multimap = HashMultimap.create();
    multimap.put("test", 1);
    multimap.put("test", 2);
    multimap.put("test2", 3);

    multiset.forEach(System.out::println);

    for (Map.Entry<String, Integer> temp : multimap.entries()) {
      System.out.println("Key: " + temp.getKey() + " Value: " + temp.getValue());
    }
  }
}
