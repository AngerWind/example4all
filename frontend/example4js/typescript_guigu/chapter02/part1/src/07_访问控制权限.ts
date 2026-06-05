(function () {


    /**
     * 在JS中, 已经有了访问控制权限, 比如定义一个属性 #age, 那么他就是private的, 默认是public的
     * 但是在JS中没有protected这个属性
     *
     * 所以在TS中, 专门添加的public, private, protected这三个关键字
     * 这三个关键字的作用和Java中的关键字完全一样
     *     public属性可以被任意修改
     *     private属性只能在类内部使用, 但是可以添加public方法来修改private属性
     *     protected属性可以被当前类和子类访问
     */

    // JS：只有 # 私有（ES2022）
    class User {
        name = '张三';        // public（默认）
        // private（私有）
        // 这个属性在外部不能使用, user.#secret, user["#secret"]
        // 甚至这个属性在子类中, 也无法使用
        #secret = '秘密';

        #privateMethod() {    // 私有方法
            return '私有方法';
        }

        getSecret() {
            return this.#secret;  // 内部可访问
        }
    }


    /**
     * TS中的访问控制权限
     */
    class User1 {
        public name: string;           // 公有，任何地方都能访问
        private secret: string;        // 私有，只有类内部能访问
        protected id: number;          // 受保护，类内部 + 子类能访问

        constructor(name: string, secret: string, id: number) {
            this.name = name;
            this.secret = secret;
            this.id = id;
        }

        // 类内部可以访问所有
        showAll() {
            console.log(this.name);    // ✅
            console.log(this.secret);  // ✅
            console.log(this.id);      // ✅
        }
    }

    class Admin extends User1 {
        show() {
            console.log(this.name);    // ✅ public，子类可访问
            // console.log(this.secret);  // ❌ private，子类不可访问
            console.log(this.id);      // ✅ protected，子类可访问
        }
    }

    const user = new User1('张三', '秘密', 1);
    user.name;      // ✅ public
    // user.secret;    // ❌ private，外部不可访问
    // user.id;        // ❌ protected，外部不可访问


    /**
     * TS 在针对 class的时候, 新增了一种简写的写法
     */
    class User2 {
        constructor(
            public name: string,
            private secret: string,
            protected id: number,
            readonly key: string
        ) {}
    }

})();