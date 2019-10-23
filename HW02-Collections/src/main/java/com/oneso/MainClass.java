package com.oneso;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainClass {
  public static void main(String[] args) {
    List<Integer> test1 = new DIYarrayList<>();
    List<Integer> test2 = new DIYarrayList<>();

    // ---1--- //
    Collections.addAll(test1, 1, 2, 3, 4);
    printArray(test1);
    System.out.println("---");
    test1.clear();

    // ---2--- //
    for (int i = 0; i < 30; i++) {
      test1.add(i);
      test2.add(i + 100);
    }

    Collections.copy(test2, test1);
    printArray(test1);
    printArray(test2);
    System.out.println("---");

    // ---3--- //
    Collections.sort(test1, (o1, o2) -> o2.compareTo(o1));
    printArray(test1);
  }

  private static void printArray(Collection<?> collection) {
    for (Object temp : collection) {
      System.out.printf("%s ", temp);
    }
    System.out.println();
  }
}
