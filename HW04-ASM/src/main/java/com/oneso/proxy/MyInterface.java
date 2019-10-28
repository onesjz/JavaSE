package com.oneso.proxy;

import java.util.List;

public interface MyInterface {

  public void foo(int a);

  public void show();

  public void intArg(int a, int b);

  public void booleanArg(boolean a);

  public void stringArg(String a, String b);

  public void testTypes(Object obj, String text, int val1, float val2, List<Object> objectList, char c, String needNull);
}
