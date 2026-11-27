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
    Animal.prototype.__run__ = function () {
        console.log('Animal 内部辅助函数');
    };
    // 受保护的
    Animal.prototype.move = function () {
        this.__run__();
        console.log('走两步');
    };
    // 公开的
    Animal.prototype.run = function () {
        this.__run__();
        this.move();
        console.log('跑起来');
    };
    return Animal;
}());
var a = new Animal();
// a.js.
// 子类
var Dog = /** @class */ (function (_super) {
    __extends(Dog, _super);
    function Dog() {
        return _super !== null && _super.apply(this, arguments) || this;
    }
    Dog.prototype.bark = function () {
        // this.
        console.log('旺旺！');
    };
    return Dog;
}(Animal));
var d = new Dog();
// d.
