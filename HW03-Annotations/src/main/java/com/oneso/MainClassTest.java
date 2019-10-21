package com.oneso;

import com.oneso.Annotations.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainClassTest {

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        Map<String, Method> beforeAfter = new HashMap<>();
        Class<?> aClass = null;

        if(args[0].isEmpty()) {
            System.out.println("Надо было передать класс");
            System.exit(-1);
        }

        try {
            aClass = Class.forName(args[0]);
        } catch (ClassNotFoundException e) {
            System.out.printf("Не существует \"%s\"%n", args[0]);
            System.exit(-1);
        }

        Method[] methods = aClass.getDeclaredMethods();

        for (Method method : methods) {
            if(method.isAnnotationPresent(BeforeAll.class))
                beforeAfter.put("beforeAll", method);
            if(method.isAnnotationPresent(AfterAll.class))
                beforeAfter.put("afterAll", method);
            if(method.isAnnotationPresent(Before.class))
                beforeAfter.put("before", method);
            if(method.isAnnotationPresent(After.class))
                beforeAfter.put("after", method);
        }

        beforeAfter.get("beforeAll").invoke(null);
        Object instance;

        List<String> failed = new ArrayList<>();
        List<String> correct = new ArrayList<>();
        for (Method method : methods) {
            instance = aClass.getDeclaredConstructors()[0].newInstance();

            if(method.isAnnotationPresent(Test.class)) {
                try {
                    beforeAfter.get("before").invoke(instance);
                    System.out.println(method.getDeclaredAnnotation(Description.class).value());
                    method.invoke(instance);
                    beforeAfter.get("after").invoke(instance);
                    correct.add(method.getName());
                } catch (Exception e) {
                    failed.add(method.getName());
                }
            }
        }

        beforeAfter.get("afterAll").invoke(null);

        System.out.printf("Общее количество тестов: %d%n", failed.size() + correct.size());
        System.out.printf("Выполнено: %d - %s%n", correct.size(), correct);

        if(failed.size() > 0) {
            System.out.printf("Провалено: %d - %s%n", failed.size(), failed);
        }
    }
}
