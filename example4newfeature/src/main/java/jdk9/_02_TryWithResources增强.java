package jdk9;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class _02_TryWithResources增强 {

    public static void main(String[] args) throws FileNotFoundException {
        // 1. 在jdk7中增加了try-with-resources的写法
        try (
                // 代码写在这里会比较臃肿
                FileInputStream fileInputStream = new FileInputStream("a.txt");
                FileOutputStream fileOutputStream = new FileOutputStream("b.txt");
        ) {

        } catch (Exception e) {

        }
        // 2. 在jkd9中, 对try-with-resources的写法进行了增强
        FileInputStream fileInputStream = new FileInputStream("a.txt");
        FileOutputStream fileOutputStream = new FileOutputStream("b.txt");
        try (fileOutputStream; fileInputStream) {

        } catch (Exception e) {

        }
    }
}
