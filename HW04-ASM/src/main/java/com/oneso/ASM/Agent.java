package com.oneso.ASM;

import org.objectweb.asm.*;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.StringConcatFactory;
import java.security.ProtectionDomain;
import java.util.*;

public class Agent {

  private static final int API = Opcodes.ASM5;
  private static final String CLASS_NAME = "com/oneso/ASM/TypesLogging";
  private static final String PROXY_METHOD = "MagicLog";

  public static void premain(String agentArgs, Instrumentation inst) {
    System.out.println("Magic is coming");
    inst.addTransformer(new ClassFileTransformer() {
      @Override
      public byte[] transform(ClassLoader loader, String className,
                              Class<?> classBeingRedefined,
                              ProtectionDomain protectionDomain,
                              byte[] classfileBuffer) {
        if (className.equals(CLASS_NAME)) {
          Map<String, Type[]> methodsWithArgs = findAnnotationLog(classfileBuffer);
          if(!methodsWithArgs.isEmpty()) {
            return changeMethod(classfileBuffer, methodsWithArgs);
          }
        }
        return classfileBuffer;
      }
    });
  }

  private static Map<String, Type[]> findAnnotationLog(byte[] classBuffer) {
    ClassReader cr = new ClassReader(classBuffer);
    ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
    ClassVisitor cv = new MethodScanner(API, cw);
    cr.accept(cv, API);

    return MethodScanner.AnnotationScanner.getMethodsWithArgs();
  }

  private static byte[] changeMethod(byte[] classBuffer, Map<String, Type[]> methodsWithArgs) {
    ClassReader cr = new ClassReader(classBuffer);
    ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
    ClassVisitor cv = new ClassVisitor(API, cw) {
      @Override
      public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (methodsWithArgs.containsKey(name)) {
          return super.visitMethod(access, name + PROXY_METHOD, descriptor, signature, exceptions);
        } else {
          return super.visitMethod(access, name, descriptor, signature, exceptions);
        }
      }
    };
    cr.accept(cv, API);
    createMethod(cw, methodsWithArgs);

    byte[] finalClass = cw.toByteArray();
    try (OutputStream fos = new FileOutputStream("ExampleProxyASM.class")) {
      fos.write(finalClass);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return finalClass;
  }

  private static void createMethod(ClassWriter cw, Map<String, Type[]> methodsWithArgs) {
    for (Map.Entry<String, Type[]> temp : methodsWithArgs.entrySet()) {
      Type[] types = temp.getValue();
      StringBuilder args = new StringBuilder(" ");
      StringBuilder descriptor = new StringBuilder();
      for (Type type : types) {
        descriptor.append(type);
      }

      MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, temp.getKey(), "(" + descriptor + ")V", null, null);
      mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
      for (int i = 0; i < types.length; i++) {
        mv.visitVarInsn(types[i].getOpcode(Opcodes.ILOAD), i + 1);
        args.append("'\u0001' ");
      }
      mv.visitInvokeDynamicInsn("makeConcatWithConstants", "(" + descriptor + ")Ljava/lang/String;", getHandle(),
          "Executed method: " + temp.getKey() + ", Param: " + "(" + args.toString() + ")");
      mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

      mv.visitVarInsn(Opcodes.ALOAD, 0);
      for (int i = 0; i < types.length; i++) {
        mv.visitVarInsn(types[i].getOpcode(Opcodes.ILOAD), i + 1);
      }
      mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, CLASS_NAME, temp.getKey() + PROXY_METHOD, "(" + descriptor + ")V", false);

      mv.visitInsn(Opcodes.RETURN);
      mv.visitMaxs(0, 0);
      mv.visitEnd();
    }
  }

  private static Handle getHandle() {
    return new Handle(Opcodes.H_INVOKESTATIC, Type.getInternalName(StringConcatFactory.class),
        "makeConcatWithConstants",
        MethodType.methodType(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class,
            String.class, Object[].class).toMethodDescriptorString(),
        false);
  }

  static class MethodScanner extends ClassVisitor {

    MethodScanner(int api, ClassVisitor classVisitor) {
      super(api, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exception) {
      MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exception);
      return new AnnotationScanner(API, methodVisitor, name, descriptor);
    }

    static class AnnotationScanner extends MethodVisitor {

      private String nameMethod;
      private Type[] descriptors;
      private static Map<String, Type[]> methodsWithArgs = new HashMap<>();

      AnnotationScanner(int api, MethodVisitor methodVisitor, String name, String descriptor) {
        super(api, methodVisitor);
        this.nameMethod = name;
        this.descriptors = Type.getArgumentTypes(descriptor);
      }

      @Override
      public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        String ANNOTATION = "Lcom/oneso/annotation/Log;";
        if (descriptor.equals(ANNOTATION)) {
          methodsWithArgs.put(nameMethod, descriptors);
        }
        return super.visitAnnotation(descriptor, visible);
      }

      static Map<String, Type[]> getMethodsWithArgs() {
        return methodsWithArgs;
      }
    }
  }
}
