package com.example4asm;

import org.objectweb.asm.*;

import java.io.IOException;

import static org.objectweb.asm.Opcodes.ASM9;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/8/21
 * @description
 */
public class ASMCoreTest {

    public static void main(String[] args) throws IOException {
        // 使用ClassReader读取指定的class
        ClassReader cr = new ClassReader("com.example4asm.ASMCoreTest");
        cr.accept(new ClassVisitor(ASM9) {
            @Override
            public ClassVisitor getDelegate() {
                return super.getDelegate();
            }

            @Override
            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                System.out.println("version: " + version);
                System.out.println("access: " + access);
                System.out.println("name: " + name);
                System.out.println("signature: " + signature);
                System.out.println("superName: " + superName);
                if (interfaces!= null && interfaces.length > 0) {
                    System.out.print("interfaces:");
                    for (String s : interfaces) {
                        System.out.print(s + ",");
                    }

                }
                super.visit(version, access, name, signature, superName, interfaces);
            }

            @Override
            public void visitSource(String source, String debug) {
                System.out.println("call visitSource: source: " + source + ", debug: " + debug);
                super.visitSource(source, debug);
            }

            @Override
            public ModuleVisitor visitModule(String name, int access, String version) {
                return super.visitModule(name, access, version);
            }

            @Override
            public void visitNestHost(String nestHost) {
                System.out.println("call visitNestHost: nestHost: " + nestHost);
                super.visitNestHost(nestHost);
            }

            @Override
            public void visitOuterClass(String owner, String name, String descriptor) {
                System.out.println("call visitOuterClass: owner: " + owner + ", name: " + name + ", descriptor: " + descriptor);
                super.visitOuterClass(owner, name, descriptor);
            }

            @Override
            public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                return super.visitAnnotation(descriptor, visible);
            }

            @Override
            public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
                return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
            }

            @Override
            public void visitAttribute(Attribute attribute) {
                System.out.println("call visitAttribute: attribute: " + attribute);
                super.visitAttribute(attribute);
            }

            @Override
            public void visitNestMember(String nestMember) {
                System.out.println("call visitNestMember: nestMember: " + nestMember);
                super.visitNestMember(nestMember);
            }

            @Override
            public void visitPermittedSubclass(String permittedSubclass) {
                System.out.println("call visitPermittedSubclass: permittedSubclass: " + permittedSubclass);
                super.visitPermittedSubclass(permittedSubclass);
            }

            @Override
            public void visitInnerClass(String name, String outerName, String innerName, int access) {
                System.out.println("call visitInnerClass: name: " + name + ", outerName: " + outerName + ", innerName: " + innerName + ", access: " + access);
                super.visitInnerClass(name, outerName, innerName, access);
            }

            @Override
            public RecordComponentVisitor visitRecordComponent(String name, String descriptor, String signature) {
                return super.visitRecordComponent(name, descriptor, signature);
            }

            @Override
            public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
                return super.visitField(access, name, descriptor, signature, value);
            }

            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                return super.visitMethod(access, name, descriptor, signature, exceptions);
            }

            @Override
            public void visitEnd() {
                System.out.println("call visitEnd");
                super.visitEnd();
            }
        }, 0);
    }
}
