package com.oneso;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;

public class MainClassTest {

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException, ClassNotFoundException {

        if(args[0].isEmpty()) {
            System.out.println("Надо было передать класс");
            System.exit(-1);
        }

        if(args[0].equals(RunnerTests.class.getName())) {
            Class<?> aClass = Class.forName(args[0]);
            Object instance = aClass.getDeclaredConstructors()[0].newInstance();
            Method methodRun = aClass.getMethod("run", Collection.class);
            methodRun.invoke(instance, Collections.singleton(ClassForTest.class));
        }
    }
}
