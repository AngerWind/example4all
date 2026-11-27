var p1;
var p2;
var p3;
// 正确：
// p1 = p2
// p2 = p1
// p1 = p3
// 错误演示：
// p3 = p1
// 类和接口之间也是兼容的
var Point4D = /** @class */ (function () {
    function Point4D() {
    }
    return Point4D;
}());
p2 = new Point4D();
