package com.oneso.ASM;

import com.oneso.annotation.Log;

import java.util.List;

public class TypesLogging {

  public void foo(int a) {
  }

  @Log
  public void show() {
  }

  @Log
  public void intArg(int a, int b) {
  }

  @Log
  public void booleanArg(boolean a) {
  }

  @Log
  public void stringArg(String a, String b) {
  }

  @Log
  public void testTypes(Object obj, String text, int val1, float val2, List<Object> objectList, char c, String needNull) {
  }

}
