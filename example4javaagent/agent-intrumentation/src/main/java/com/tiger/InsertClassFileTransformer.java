package com.tiger;

import javassist.*;

import java.io.InputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.security.ProtectionDomain;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/1/3
 * @description
 */
public class InsertClassFileTransformer implements ClassFileTransformer {

    private String className;
    private String methodName;
    private String message;

    public InsertClassFileTransformer(String className, String methodName, String message) {
        this.className = className;
        this.message = message;
        this.methodName = methodName;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
        ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
            try {
                className = className.replace("/", ".");
                if (className.equals(this.className)) {
                    final ClassPool classPool = ClassPool.getDefault();
                    /**
                     * classPool会从Thread.currentThread().getContextClassLoader()获取classloader
                     * 然后根据classloader去获取classpath, 从而获取class字节码文件
                     * Thread.currentThread().getContextClassLoader()中的ClassLoader默认是当前线程的创建者
                     * 如果返回null, 说明当前线程的创建者的ClassLoader是BootstrapClassLoader
                     *
                     * 在静态加载agent的时候是没问题的, 因为当前方法是在main线程中执行的,
                     * 而main线程Thread.currentThread().getContextClassLoader()返回的是appclassLoader
                     *
                     * 但是在动态加载的时候, 当前线程的创建者的ClassLoader是BootstrapClassLoader
                     * 所以classPool无法通过Thread.currentThread().getContextClassLoader()获取classloader,
                     * 也就无法加载class字节码文件了, 需要手动添加
                     */
                    classPool.insertClassPath(new LoaderClassPath(loader));
                    final CtClass clazz = classPool.get(className);
                    if (clazz.isFrozen()) {
                        clazz.defrost();
                    }
                    for (CtMethod method : clazz.getMethods()) {
                        // Modifier.isNative(methods[i].getModifiers())过滤本地方法,否则会报
                        if (Modifier.isNative(method.getModifiers())) {
                            continue;
                        }
                        if (!method.getName().contains(this.methodName)) {
                            continue;
                        }
                        method.insertBefore("System.out.println(\"before: (" + message +  ") \");");
                        method.insertAfter("System.out.println(\"after: (" + message +  ") \");", false);
                    }
                    return clazz.toBytecode();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 返回null表示对class字节码不做任何更改
            return null;
    }
}
