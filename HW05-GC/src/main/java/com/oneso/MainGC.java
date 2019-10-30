package com.oneso;

public class MainGC {

  public static void main(String[] args) {

    GC gc = new GC();

    try {
      gc.start(5 * 1000 * 1000, true);
    } catch (Exception e) {
      System.out.println("Что-то пошло не так: " + e.getMessage());
    }

  }
}
