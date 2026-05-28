package jdk15;

public class _01_密封类和密封接口 {

    public sealed class Animal permits Cat, Dog { }

    // Cat必须直接继承Animal, 并且Cat必须是final的
    public final class Cat extends Animal { }

    // Dog必须直接继承Animal, 如果dog不想为final, 那么他也必须声明为sealed
    public sealed class Dog extends Animal permits Husky { }

    // Husky必须直接继承Dog
    public final class Husky extends Dog {  }


    // 接口也可以密封
    public sealed interface Color permits Red, Blue{};

    // Red必须直接继承Color, 并指定为final
    public static final class Red implements Color {}

    // 如果不想为final, 那么他也要标记为sealed
    public sealed interface Blue extends Color permits BlueGreen{}

    public static final class BlueGreen implements Blue{}


}
