package com.oneso;

import java.util.Collections;

public class MainClassTest {

  public static void main(String[] args) {

    if (args[0].isEmpty()) {
      System.out.println("Надо было передать класс");
      System.exit(-1);
    }

    try {
      Class<?> aClass = Class.forName(args[0]);

      RunnerTests runnerTests = new RunnerTests();
      runnerTests.run(Collections.singleton(aClass));
    } catch (ClassNotFoundException e) {
      System.out.println("Не удалось прочитать класс с тестами " + args[0]);
    }
  }
}
