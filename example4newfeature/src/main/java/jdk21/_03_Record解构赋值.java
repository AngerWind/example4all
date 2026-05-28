package jdk21;

public class _03_Record解构赋值 {

    public static record Student (String name, int age){};

    public static void main(String[] args) {
        Object object = new Student("zhangsan", 12);

        if (object instanceof Student(String name, int age)) {
            System.out.println(name);
            System.out.println(age);
        }
    }
}
