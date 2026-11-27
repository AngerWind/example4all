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
var Point3D = /** @class */ (function () {
    function Point3D() {
    }
    return Point3D;
}());
var p1 = new Point3D();
// 错误演示
// const p2: Point3D = new Point()
