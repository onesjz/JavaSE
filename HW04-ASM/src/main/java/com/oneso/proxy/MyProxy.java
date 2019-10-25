package com.oneso.proxy;

import com.oneso.annotation.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MyProxy {

  public static MyInterface getTypesProxyLog() {
    InvocationHandler handler = new HandlerMethod(new MyClass());
    return (MyInterface) Proxy.newProxyInstance(MyProxy.class.getClassLoader(),
        MyClass.class.getInterfaces(), handler);
  }

  static class HandlerMethod implements InvocationHandler {

    private final MyInterface myInterface;

    HandlerMethod(MyInterface myInterface) {
      this.myInterface = myInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

      Method originalMethod = myInterface.getClass().getMethod(method.getName(), method.getParameterTypes());

      if (originalMethod.isAnnotationPresent(Log.class)) {
        StringBuilder parameters = new StringBuilder(" ");
        if (args != null) {
          for (Object temp : args) {
            parameters.append(String.format("'%s' ", temp));
          }
        }
        System.out.println("Executed method: " + method.getName() + " Param: (" + parameters.toString() + ")");

        return method.invoke(myInterface, args);
      }
      return method.invoke(myInterface, args);
    }
  }
}
