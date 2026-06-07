/* class Person {
  // 只读属性
  readonly age: number = 18

  constructor(age: number) {
    this.age = age
  }

  // 错误演示：
  // readonly setAge() {
  //   // this.age = 20
  // }
} */
var Person = /** @class */ (function () {
    function Person(age) {
        // 只读属性
        // 注意：只要是 readonly 来修饰的属性，必须手动提供明确的类型
        this.age = 18;
        this.age = age;
    }
    return Person;
}());
// --
// interface IPerson {
//   readonly name: string
// }
// let obj: IPerson = {
//   name: 'jack'
// }
var obj = {
    name: 'jack'
};
obj.name = 'rose';
