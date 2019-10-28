package com.oneso.proxy;

import com.oneso.annotation.Log;

import java.util.List;

public class MyClass implements MyInterface {

  @Override
  public void foo(int a) {

  }

  @Log
  @Override
  public void show() {

  }

  @Log
  @Override
  public void intArg(int a, int b) {

  }

  @Log
  @Override
  public void booleanArg(boolean a) {

  }

  @Log
  @Override
  public void stringArg(String a, String b) {

  }

  @Log
  @Override
  public void testTypes(Object obj, String text, int val1, float val2, List<Object> objectList, char c, String needNull) {

  }
}
