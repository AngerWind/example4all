var Point = /** @class */ (function () {
    function Point() {
        this.x = 1;
        this.y = 2;
    }
    Point.prototype.scale = function (n) {
        this.x *= n;
        this.y *= n;
    };
    return Point;
}());
var p = new Point();
p.scale(10);
console.log(p.x, p.y);
