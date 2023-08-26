package com.tiger.reference;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

import org.junit.Test;

public class ReferenceTest {

    @Test
    public void test() {
        String hello = new String("hello");
        SoftReference<String> softReference = new SoftReference<>(hello);
        hello = null;

        String world = new String("world");
        WeakReference<String> weakReference = new WeakReference<>(world);
        world = null;

        System.gc();
        System.out.println(softReference.get()); // hello
        System.out.println(weakReference.get()); // null
    }

}
