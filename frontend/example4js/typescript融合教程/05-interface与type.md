# interface 与 type

## 1. interface

`interface` 主要用于描述对象结构：

```ts
interface Person {
  name: string;
  age: number;
  sayHi(): void;
}

const person: Person = {
  name: "jack",
  age: 18,
  sayHi() {}
};
```

也可以描述函数属性：

```ts
interface Person {
  name: string;
  sayHi: () => void;
}
```

## 2. interface 继承

接口可以通过 `extends` 继承：

```ts
interface Point2D {
  x: number;
  y: number;
}

interface Point3D extends Point2D {
  z: number;
}

const point: Point3D = {
  x: 1,
  y: 2,
  z: 3
};
```

接口可以多继承：

```ts
interface Named {
  name: string;
}

interface Aged {
  age: number;
}

interface User extends Named, Aged {}
```

## 3. type 类型别名

`type` 可以给任意类型起别名：

```ts
type ID = string | number;
type Direction = "up" | "down" | "left" | "right";
type Point = [number, number];
type User = {
  name: string;
  age: number;
};
```

数组类型别名常见写法：

```ts
type MyTupleArray = [string, number][];
type MyUnionArray = (string | number)[];

const a: MyTupleArray = [["a", 1]];
const b: MyUnionArray = ["a", 1];
```

## 4. interface 与 type 的共同点

二者都可以描述对象：

```ts
interface User1 {
  name: string;
  age: number;
}

type User2 = {
  name: string;
  age: number;
};
```

二者都可以被类实现：

```ts
interface Singable {
  name: string;
  sing(): void;
}

class Person implements Singable {
  name = "jack";

  sing() {
    console.log("sing");
  }
}
```

类型别名也可以被 `implements`，前提是它最终描述的是对象结构：

```ts
type Movable = {
  move(): void;
};

class Dog implements Movable {
  move() {}
}
```

## 5. interface 与 type 的区别

`interface` 支持声明合并：

```ts
interface User {
  name: string;
}

interface User {
  age: number;
}

const user: User = {
  name: "jack",
  age: 18
};
```

`type` 不支持同名合并：

```ts
type UserType = {
  name: string;
};

// type UserType = { age: number }; // 报错
```

`type` 能描述联合类型、交叉类型、元组、条件类型等复杂类型运算：

```ts
type Status = "active" | "disabled";
type Nullable<T> = T | null;
type Pair = [string, number];
```

## 6. 交叉类型

交叉类型使用 `&`，表示同时具备多个类型的成员：

```ts
interface Person {
  name: string;
  say(): number;
}

interface Contact {
  phone: string;
}

type PersonDetail = Person & Contact;

const obj: PersonDetail = {
  name: "jack",
  phone: "133...",
  say() {
    return 1;
  }
};
```

## 7. 交叉类型和接口继承的区别

接口继承要求子接口成员与父接口成员保持兼容：

```ts
interface A {
  fn: (value: number) => string;
}

// interface B extends A {
//   fn: (value: string) => string; // 报错
// }
```

交叉类型可能把两个函数签名合成重载式结构：

```ts
interface A {
  fn: (value: number) => string;
}

interface B {
  fn: (value: string) => string;
}

type C = A & B;

declare const c: C;
c.fn(1);
c.fn("a");
```

如果是普通属性冲突，交叉后可能得到不可用的 `never`：

```ts
type A = { id: string };
type B = { id: number };
type C = A & B;

// C["id"] 是 never
```

## 8. 怎么选择

建议：

- 定义公共对象结构，尤其是可能被扩展的结构，用 `interface`。
- 定义联合类型、元组、基础类型别名、复杂类型运算，用 `type`。
- 团队已有规范时优先跟随团队规范。

一个简单原则：

```text
对象结构优先 interface，类型运算优先 type。
```
