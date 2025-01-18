package com.tiger;

import javassist.*;

import java.io.IOException;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.Arrays;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/1/3
 * @description
 */
public class AgentMainAgent {

    /** 动态加载。Java agent指定的premain方法，会在main方法之前被调用 */
    // 报错不会导致jvm退出
    public static void agentmain(String args, Instrumentation instrumentation) {
        System.out.println("agentmain start!");
        System.out.println(args);
        // 添加两个transformer
        // ClassLoader.load()
        addTransformer(instrumentation);
        // 重新加载student类
        // retransformClasses(instrumentation, "com.tiger.Student");

        // 直接替换class
        redefineClasses(instrumentation);
        System.out.println("agentmain end!");

    }

    private static void addTransformer(Instrumentation instrumentation) {
        /**
         * 有两种触发Transformer的方式: 1. ClassLoader.loadClass 2. Instrumentation.retransformClasses
         */
        instrumentation.addTransformer(new InsertClassFileTransformer("com.tiger.Student", "say", "aaa"));
        instrumentation.addTransformer(new InsertClassFileTransformer("com.tiger.Student", "say", "bbb"), true);
    }

    private static void retransformClasses(Instrumentation instrumentation, String className) {
        Class<?>[] classes = instrumentation.getAllLoadedClasses();
        if (classes != null) {
            for (Class<?> c : classes) {
                if (c.isInterface() || c.isAnnotation() || c.isArray() || c.isEnum()) {
                    continue;
                }
                if (c.getName().equals(className)) {
                    try {
                        /*
                         * retransformClasses()对JVM已经加载的类重新触发类加载。使用的就是上面注册的Transformer。
                         * retransformClasses()可以修改方法体，但是不能变更方法签名、增加和删除方法/类的成员属性
                         */
                        instrumentation.retransformClasses(c);
                    } catch (UnmodifiableClassException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void redefineClasses(Instrumentation instrumentation) {
        final ClassPool classPool = ClassPool.getDefault();
        for (Class loadedClass : instrumentation.getAllLoadedClasses()) {
            if (loadedClass.getName().equals("com.tiger.Student")) {
                try {
                    ClassLoader classLoader = loadedClass.getClassLoader();
                    classPool.insertClassPath(new LoaderClassPath(classLoader));

                    CtClass ctClass = classPool.get("com.tiger.Student");
                    CtMethod say = ctClass.getDeclaredMethod("say");
                    say.insertBefore("System.out.println(\"动态替换class!\");");
                    byte[] bytecode = ctClass.toBytecode();
                    /*
                     * 直接通过redefineClasses替换掉已经加载的classes
                     * redefineClasses可以修改方法体，但是不能变更方法签名、增加和删除方法/类的成员属性
                     */
                    instrumentation.redefineClasses(new ClassDefinition(loadedClass, bytecode));

                    break;
                } catch (UnmodifiableClassException | NotFoundException | CannotCompileException | IOException
                    | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
