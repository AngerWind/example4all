(function () {

    /**
     * 在JS中, 只有class 和 extends 关键字, abstract抽象类和抽象方法是ts中特意添加的, 他会将编译后的类转换为JS中的代码
     *
     * TS中的抽象类和Java中的差不多, 不能new, 可以有抽象方法
     */
    abstract class Animal {
        name: string;

        constructor(name: string) {
            this.name = name;
        }

        // 定义抽象方法, 子类必须重新, 否则也只能是抽象类
        abstract sayHello():void;
    }

    class Dog extends Animal{

        // 重写
        sayHello() {
            console.log('汪汪汪汪！');
        }

    }

    class Cat extends Animal{
        sayHello() {
            console.log('喵喵喵喵！');
        }

    }

    const dog = new Dog('旺财');
    dog.sayHello();

})();