package com.oneso;

import com.oneso.proxy.MyInterface;
import com.oneso.proxy.MyProxy;

import java.util.Arrays;

public class MainClassProxy {

  public static void main(String[] args) {
    MyInterface myInterface = MyProxy.getTypesProxyLog();

    myInterface.foo(123);
    myInterface.booleanArg(true);
    myInterface.intArg(1, 2);
    myInterface.show();
    myInterface.stringArg("qwe", "www");
    myInterface.testTypes(new Object(), "qwe", 1, 2.5F, Arrays.asList("q", "w"), 'A', null);
  }
}
