package com.oneso;

import com.oneso.ASM.TypesLogging;

import java.util.Arrays;

/**
 * Для запуска с агентом: java --enable-preview -javaagent:agentAsm.jar -jar mainAsm.jar
 */
public class MainClassASM {

  public static void main(String[] args) {
    TypesLogging typesLogging = new TypesLogging();

    typesLogging.foo(123);
    typesLogging.show();
    typesLogging.stringArg("qwe", "www");
    typesLogging.booleanArg(true);
    typesLogging.intArg(1, 2);
    typesLogging.testTypes(new Object(), "text", 1, 1.12F, Arrays.asList("t", "2"), 'A', null);
  }
}
