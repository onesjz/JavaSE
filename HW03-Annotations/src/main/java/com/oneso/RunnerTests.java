package com.oneso;

import com.oneso.Annotations.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class RunnerTests {

    private List<String> failed = new ArrayList<>();
    private List<String> correct = new ArrayList<>();

    public void run(Collection<? extends Class<?>> classes) {

        for(Class<?> aClass : classes) {
            Map<String, Method> methodMap = getMapMethods(aClass.getDeclaredMethods());

            if(methodMap.containsKey("beforeAll"))
                callMethod(methodMap.get("beforeAll"), null, false);

            callMethods(aClass.getDeclaredMethods(), methodMap, aClass);

            if(methodMap.containsKey("afterAll"))
                callMethod(methodMap.get("afterAll"), null, false);
        }

        System.out.printf("Общее количество тестов: %d%n", failed.size() + correct.size());
        System.out.printf("Выполнено: %d - %s%n", correct.size(), correct);

        if(failed.size() > 0) {
            System.out.printf("Провалено: %d - %s%n", failed.size(), failed);
        }

    }

    private Map<String, Method> getMapMethods(Method[] methods) {
        Map<String, Method> out = new HashMap<>();

        for (Method method : methods) {
            if (method.isAnnotationPresent(BeforeAll.class))
                out.put("beforeAll", method);
            if (method.isAnnotationPresent(AfterAll.class))
                out.put("afterAll", method);
            if (method.isAnnotationPresent(Before.class))
                out.put("before", method);
            if (method.isAnnotationPresent(After.class))
                out.put("after", method);
        }
        return out;
    }

    private void callMethods(Method[] methods, Map<String, Method> beforeAfter, Class<?> aClass) {
        for(Method method : methods) {
            Optional<Object> instance = getInstance(aClass);

            if(instance.isPresent()) {

                if(method.isAnnotationPresent(Test.class)) {
                    if(beforeAfter.containsKey("before"))
                        callMethod(beforeAfter.get("before"), instance.get(), false);

                    System.out.println(method.getDeclaredAnnotation(Description.class).value());
                    callMethod(method, instance.get(), true);

                    if(beforeAfter.containsKey("after"))
                        callMethod(beforeAfter.get("after"), instance.get(), false);
                }
            }
        }
    }

    private void callMethod(Method method, Object instance, boolean test) {
        try {
            method.invoke(instance);
            if(test)
                correct.add(method.getName());

        } catch (IllegalAccessException | InvocationTargetException e) {
            System.out.println("Не удалось выполнить метод: " + method.getName());
            if(test)
                failed.add(method.getName());
        }
    }

    private Optional<Object> getInstance(Class<?> aClass) {
        try {
            return Optional.of(aClass.getDeclaredConstructors()[0].newInstance());
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            System.out.println("Не удалось получить instance класса");
            return Optional.empty();
        }
    }
}
