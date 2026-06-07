var Person = /** @class */ (function () {
    function Person(age, gender) {
        this.age = age;
        this.gender = gender;
    }
    return Person;
}());
var p = new Person(18, '男');
console.log(p.age, p.gender);
