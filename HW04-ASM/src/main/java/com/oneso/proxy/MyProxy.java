package com.oneso.proxy;

import com.oneso.annotation.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class MyProxy {

  public static MyInterface getTypesProxyLog() {
    InvocationHandler handler = new HandlerMethod(new MyClass());
    return (MyInterface) Proxy.newProxyInstance(MyProxy.class.getClassLoader(),
        MyClass.class.getInterfaces(), handler);
  }

  static class HandlerMethod implements InvocationHandler {

    private final MyInterface myInterface;
    private final List<String> methodsLog;

    HandlerMethod(MyInterface myInterface) {
      this.myInterface = myInterface;
      methodsLog = findMethodsWithLog(myInterface.getClass());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

      if(methodsLog.contains(method.getName())) {
        StringBuilder parameters = new StringBuilder(" ");
        if (args != null) {
          for (Object temp : args) {
            parameters.append(String.format("'%s' ", temp));
          }
        }
        System.out.println("Executed method: " + method.getName() + " Param: (" + parameters.toString() + ")");
      }
      return method.invoke(myInterface, args);
    }

    private List<String> findMethodsWithLog(Class<?> aClass) {
      List<String> out = new ArrayList<>();

      Method[] methods = aClass.getDeclaredMethods();
      for(Method temp : methods) {
        if(temp.isAnnotationPresent(Log.class)) {
          out.add(temp.getName());
        }
      }
      return out;
    }
  }
}
