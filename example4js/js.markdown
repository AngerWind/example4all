### 箭头函数与function的区别

1. 箭头函数与声明式函数的写法

   ~~~javascript
   //function
    function fn(a, b){
    return a + b;
    }
    //arrow function
    var foo = (a, b)=>{ return a + b };
   ~~~

2. this的指向: 使用function定义的函数，this的指向随着调用环境的变化而变化，而箭头函数中的this指向是固定不变的，一直指向定义函数的环境

    ~~~javascirpt
    //使用function定义的函数
    function foo(){
            console.log(this);
            }
    var obj = { aa: foo };
    foo(); //Window
    obj.aa() //obj { aa: foo }
    ~~~

3. 变量的提升:
   function的级别最高, 而用箭头函数定义函数的时候，需要var（let const定义的时候更不必说）关键词，而var所定义的变量不能得到变量提升，故箭头函数一定要定义于调用之前

#### for in 和 for of, forEach的原理
1. for in是基于可枚举属性来继续循环的, 遍历的是属性, 可以循环对象和数组(把数组理解为下标是1, 2, 3的对象即可)
2. for of是基于迭代器的, 遍历的是属性值, 如果对象没有迭代器, 就不能对其进行遍历
   常常用在遍历数组, set, map
3. forEach和for of差不多
~~~ts
const obj1: {[key: string]: any} = {
	a: 1,
	b: 2,
	c: 3,
}
const array = [1, 2, 3]
for (let key in obj1) {
	console.log(key) // abc
	console.log(typeof key) // string
	console.log(obj1[key]) // 123
}
console.log("-----------------------");
for (let key in array) {
	console.log(key) // 123
	console.log(typeof key) // string, for in迭代总是返回string类型的key, 在进行计算的时候要注意
	console.log(array[key]) // 123
}
console.log("-----------------------");
// for of只能迭代有迭代器的对象, 比如数组, map, set, string, 如果对象有迭代器也可以迭代
for (let key of array) {
	console.log(key) // 123
	console.log(typeof key) // number
	console.log(array[key]) // 123
}
console.log("-----------------------");

array.forEach((item, index) => {
	console.log(index) // 123
	console.log(typeof index) // number
	console.log(item) // 123
})
~~~