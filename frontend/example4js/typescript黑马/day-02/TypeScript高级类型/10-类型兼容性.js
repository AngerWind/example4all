// 演示类型兼容性：
// let arr = ['a.js', 'b', 'c']
// arr.forEach(item => {})
// arr.forEach((item, index) => {})
// arr.forEach((item, index, array) => {})
// 两个类的兼容性演示：
var Point = /** @class */ (function () {
    function Point() {
    }
    return Point;
}());
var Point2D = /** @class */ (function () {
    function Point2D() {
    }
    return Point2D;
}());
var p = new Point2D();
