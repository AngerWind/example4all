(function () {

    /**
     * 接口可以在定义类的时候去限制类的结构，
     *   接口中的所有的属性都不能有实际的值
     *   接口只定义对象的结构，而不考虑实际值
     *       在接口中所有的方法都是抽象方法
     * */
    interface myInter {
        name: string;

        sayHello():void;
    }

    /*
    * 定义类时，可以使类去实现一个接口,
    *   实现接口就是使类满足接口的要求
    * */
    class MyClass implements myInter {
        name: string;

        constructor(name: string) {
            this.name = name;
        }

        sayHello(){
            console.log('大家好~~');
        }

    }


    /**
     * interface 和 type 一样, 除了用来限制 class 的属性和方法, 也可以用来限制 object的属性和方法
     */
    // 类型别名
    type myType = {
        name: string,
        age: number
    };
    const obj1: myType = {
        name: 'John',
        age: 5,
    }

    /**
     * 使用 interface 也可以完成这个功能
     */
    interface myInterface {
        name: string;
        age: number;
    }

    const obj: myInterface = {
        name: 'sss',
        age: 111,
    };


})();