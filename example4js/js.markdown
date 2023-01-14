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

### JS垃圾回收
