// object表示一个js对象
let a: object;
a = {};
a = function () { // 函数也是对象
};

// {} 用来指定对象中可以包含哪些属性
// 语法：{属性名:属性值,属性名:属性值}
// 在属性名后边加上?，表示属性是可选的
let b: {name: string, age?: number};
b = {name: '孙悟空', age: 18};

// 这个类型必须有一个name属性, string类型
// 并且他可以有其他的属性, 属性名是string, value是string或者number类型, 这个叫做索引签名
// 这里的propName不是固定的, 只是随便取的一个名字, 可以改为key, k等其他名字
let c: {name: string, [propName: string]: string | number};
c = {name: '猪八戒', age: 18, gender: '男'};

// !!!!!!!!!!!! 注意, 索引签名 [propName: string]: any 会约束所有属性，所以指定的属性类型必须是索引签名类型的子集：
// ❌ 报错，索引签名约束所有的属性都是string类型, 但是age是number类型
// let b1: { name: string, age: number, [propName: string]: string };
// ✅ 改为 any 或联合类型
let c1: { name: string, age: number, [propName: string]: string | number };

/*
*   设置函数结构的类型声明：
*       语法：(形参:类型, 形参:类型 ...) => 返回值
* */
let d: (a: number ,b: number)=>number;
// d = function (n1: string, n2: string): number{
//     return 10;
// }


/*
*   数组的类型声明：
*       类型[]
*       Array<类型>
* */
// string[] 表示字符串数组
let e: string[];
e = ['a', 'b', 'c'];

// number[] 表示数值数值
let f: number[];

let g: Array<number>;
g = [1, 2, 3];

/*
*   元组，元组就是固定长度的数组
*       语法：[类型, 类型, 类型]
*
*   js中没有元组, 这里的元组是ts中新增的一个类型, 实际上在js中还是一个数组
*   这里ts约束这个数组只能有两个元素, 第一个是string类型, 第二个是number类型
* */
let h: [string, number];
h = ['hello', 123];

/*
* enum 枚举
*
* 在原生js中, 根本没有enum这个类型, 实际上enum也是ts中添加的类型, 他被编译为js之后就是一个var的类型
* */
enum Gender {
    Male,    // 值为 0
    Female   // 值为 1
}

let user: Gender = Gender.Male;   // 0
let user2: Gender = Gender.Female; // 1

// 和go中的itoa一样, 会自动递增
enum Gender1 {
    Male = 4,     // 4
    Female        // 5（自动递增）
}
// 也可以指定其他类型的值
enum Direction {
    Up = 'UP',
    Down = 'DOWN',
    Left = 'LEFT',
    Right = 'RIGHT'
}


let i: {name: string, gender: Gender};
i = {
    name: '孙悟空',
    gender: Gender.Male // 'male'
}

// console.log(i.gender === Gender.Male);
// & 是交叉类型, 表示需要同时满足两边的约束, 即j必须有一个name属性, 也要有一个age属性
let j: { name: string } & { age: number };
j = {name: '孙悟空', age: 18};


// 类型的别名
type myType = 1 | 2 | 3 | 4 | 5;
let k: myType;
let l: myType;
let m: myType;

k = 2;


// 这个类型是非常重要的, 表示aa可以是一个对象, 或者是null
let aa: {name: string, age: number} | null  = {name: "hello", age: 18};
aa = null;
// 在ts中, 如果没有开启strictNullChecks的话, 那么任何类型都可以赋值为null
let a11: number = null;      // ✅
let b11: string = null;      // ✅
let c11: boolean = null;     // ✅
let d11: {} = null;          // ✅


// 开启 strictNullChecks（推荐，TS 默认开启）的话, 只有 类型1 | null  这样的类型可以赋值为null
// 只有显式包含 null 的类型才能赋值为 null
let a22: number = null;           // ❌ 报错
let b22: number | null = null;    // ✅
let c22: string = null;           // ❌ 报错
let d22: string | null = null;    // ✅
let e22: object = null;           // ❌ 报错
let f22: object | null = null;    // ✅