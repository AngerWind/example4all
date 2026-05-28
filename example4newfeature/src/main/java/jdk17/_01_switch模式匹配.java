package jdk17;

public class _01_switch模式匹配 {

    public static class Animal {}

    public static class Dog extends Animal {}

    public static class Cat extends Animal {}

    public static void main(String[] args) {
        Animal animal = new Dog();
        switch (animal) {
            case null -> {
                System.out.println("null");
            }
            case Dog dog -> {
                System.out.println("dog");
            }
            case Cat cat -> {
                System.out.println("cat");
            }
            default -> {
                System.out.println("other");
            }
        }
    }
}
