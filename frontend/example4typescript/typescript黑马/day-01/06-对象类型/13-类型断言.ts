
// 类型断言

function getData(): any {
    return 'hello world';
}

// 不用断言
const result = getData();
result.length;  // ❌ TS 不知道 result 是 string

// 用断言
const result1 = getData() as string;
result1.length;  // ✅ TS 知道是 string



// getElementById 返回 HTMLElement | null
const input = document.getElementById('myInput');

// input.value;  // ❌ HTMLElement 上没有 value 属性
const input1 = document.getElementById('myInput') as HTMLInputElement;
input1.value;  // ✅ HTMLInputElement 上有 value




// 类型断言也可以用来收窄联合类型
interface Cat {
    name: string;
    meow(): void;
}

interface Dog {
    name: string;
    bark(): void;
}
function process(pet: Cat | Dog) {
    // pet.meow();  // ❌ 联合类型只能访问共有属性
    (pet as Cat).meow();  // ✅ 告诉 TS 这是 Cat
}

/**
 * 类型断言不是类型转换, 类型断言只是在编译期, 让ts知道这个属性的类型是啥, 这样在编译的时候不会报错
 * 但是在运行期的时候, 这个类型该是啥类型还是啥类型, 如果没有对应的属性的话, 会报错
 */
let a: any = 'hello';
let b = a as number;  // TS 认为是 number，但运行时还是 string

console.log(typeof b);  // "string"
b.toFixed();            // 运行时报错，不是真正的 number





// 非空断言 !
function getUser(): string | null {
    return null;
}

const name1 = getUser();
name1.length;    // ❌ 可能是 null

const name2 = getUser()!; // ! 表示"我确定这个值不是 null 或 undefined"。
name2.length;    // ✅ 告诉 TS 不会是 null/undefined

