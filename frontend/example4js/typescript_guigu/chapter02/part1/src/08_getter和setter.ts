(function () {


    /**
     * getter和setter在JS 的 es5中就添加了的, ts只是在js的基础上添加了类型标注
     */
    class Person {

        private _name: string;
        private _age: number;

        constructor(name: string, age: number) {
            this._name = name;
            this._age = age;
        }

        // 这个方法和name属性会冲突, 所以name属性按照约定设置为 _name
        get name() {
            console.log('get name()执行了！！');
            return this._name;
        }

        set name(value) {
            this._name = value;
        }

        get age() {
            return this._age;
        }

        set age(value) {
            if (value >= 0) {
                this._age = value
            }
        }
    }

    var person = new Person("zhangsan", 18);
    console.log(person.name); // 这会调用person的name的getter
    person.age = 18; // 这会调用person的age的setter

})();