package com.oneso;

import com.oneso.Annotations.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class RunnerTests {

    private static final String BEFORE_ALL = "beforeAll";
    private static final String AFTER_ALL = "afterAll";
    private static final String BEFORE = "before";
    private static final String AFTER = "after";

    private List<String> failed = new ArrayList<>();
    private List<String> correct = new ArrayList<>();

    public void run(Collection<? extends Class<?>> classes) {

        for(Class<?> aClass : classes) {
            Map<String, Method> methodMap = getMapMethods(aClass.getDeclaredMethods());

            if(methodMap.containsKey(BEFORE_ALL))
                if(!callMethod(methodMap.get(BEFORE_ALL), null, false)) {
                    callMethod(methodMap.get(AFTER_ALL), null, false);
                    throw new RuntimeException("Не удалось подготовить данные для тестов");
                }

            callMethods(aClass.getDeclaredMethods(), methodMap, aClass);

            if(methodMap.containsKey(AFTER_ALL))
                callMethod(methodMap.get(AFTER_ALL), null, false);
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
            if (method.isAnnotationPresent(BeforeAll.class)) {
                if(out.containsKey(BEFORE_ALL))
                    throw new IllegalArgumentException("Не допустимо создавать более одного метода BeforeAll");
                out.put(BEFORE_ALL, method);
            }
            if (method.isAnnotationPresent(AfterAll.class)) {
                if(out.containsKey(AFTER_ALL))
                    throw new IllegalArgumentException("Не допустимо создавать более одного метода AfterAll");
                out.put(AFTER_ALL, method);
            }
            if (method.isAnnotationPresent(Before.class)) {
                if(out.containsKey(BEFORE))
                    throw new IllegalArgumentException("Не допустимо создавать более одного метода Before");
                out.put(BEFORE, method);
            }
            if (method.isAnnotationPresent(After.class)) {
                if(out.containsKey(AFTER))
                    throw new IllegalArgumentException("Не допустимо создавать более одного метода After");
                out.put(AFTER, method);
            }
        }

        if(out.containsKey(BEFORE_ALL)) {
            if(!out.containsKey(AFTER_ALL))
                throw new RuntimeException("Необходимо создать метод с аннотацией @AfterAll");
        }

        return out;
    }

    private void callMethods(Method[] methods, Map<String, Method> beforeAfter, Class<?> aClass) {
        for(Method method : methods) {
            Optional<Object> instance = getInstance(aClass);

            if(instance.isPresent()) {

                if(method.isAnnotationPresent(Test.class)) {
                    if(beforeAfter.containsKey(BEFORE))
                        if(!callMethod(beforeAfter.get(BEFORE), instance.get(), false)) {
                            callMethod(beforeAfter.get(AFTER), instance.get(), false);
                            System.out.println("Не получилось подготовить данные, пропускаем " + method.getName());
                            continue;
                        }

                    if(method.isAnnotationPresent(Description.class))
                        System.out.println(method.getDeclaredAnnotation(Description.class).value());
                    callMethod(method, instance.get(), true);

                    if(beforeAfter.containsKey(AFTER))
                        callMethod(beforeAfter.get(AFTER), instance.get(), false);
                }
            }
        }
    }

    private boolean callMethod(Method method, Object instance, boolean test) {
        try {
            method.invoke(instance);
            if(test)
                correct.add(method.getName());
            return true;
        } catch (IllegalAccessException | InvocationTargetException e) {
            System.out.println("Не удалось выполнить метод: " + method.getName());
            if(test)
                failed.add(method.getName());
            return false;
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
