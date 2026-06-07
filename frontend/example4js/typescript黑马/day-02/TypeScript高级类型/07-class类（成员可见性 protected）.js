var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (Object.prototype.hasOwnProperty.call(b, p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        if (typeof b !== "function" && b !== null)
            throw new TypeError("Class extends value " + String(b) + " is not a constructor or null");
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
// 父类
var Animal = /** @class */ (function () {
    function Animal() {
    }
    // 这个方法是受保护的
    Animal.prototype.move = function () {
        console.log('走两步');
    };
    Animal.prototype.run = function () {
        this.move();
        console.log('跑起来');
    };
    return Animal;
}());
var a = new Animal();
// a.js.move()
// 子类
var Dog = /** @class */ (function (_super) {
    __extends(Dog, _super);
    function Dog() {
        return _super !== null && _super.apply(this, arguments) || this;
    }
    Dog.prototype.bark = function () {
        this.move();
        console.log('旺旺！');
    };
    return Dog;
}(Animal));
var d = new Dog();
// d
