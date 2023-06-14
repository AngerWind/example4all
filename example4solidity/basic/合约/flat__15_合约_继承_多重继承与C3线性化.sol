
/** 
 *  SourceUnit: c:\Users\Administrator\Desktop\example4all\example4solidity\basic\合约\_15_合约_继承_多重继承与C3线性化.sol
*/

////// SPDX-License-Identifier-FLATTEN-SUPPRESS-WARNING:GPL-3.0

pragma solidity ^0.8.17;

/**
    参考: https://blog.tedxiong.com/C3_Linearization.html
        https://blog.csdn.net/JohnnyMartin/article/details/101170982

        
    在多重继承中, 一个子类可以有多个父类, 父类有可以有多个父类, 最终构成一个DAG图
    如下图:  O
            A -> O
            B -> O
            C -> O
            K1 -> B, A
            K2 -> C, A
            Z -> K2, K1
        1. 在构造的时候, 不能同时构造两个同层级的父类, 那么应该按照什么顺序来构造父类对象
        2. 在调用super.foo()的时候, 这里的super到底是指的哪一个
        3. 那么在调用方法的时候, 子类往上寻找父类方法的顺序是什么？
    
    在python中使用C3 线性化来确定方法的调用顺序，也叫方法解析顺序（Method Resolution Order，MRO）
    约定以下符号
        - 如果A继承自B,C, 记为A->B, C
        - merge()表示merge算法
        - L(A)表示A的线性化结果的简写
        - [B, C, D] : 某个类的线性化结果的具体值
    利用以上符号，该算法归纳如下：
        - 对于继承单个基类的情况 A->B, 则有L(A) = [A, B]
        - 对于继承多个基类的情况 A->B,C,D,则有 L(A) = [A] + merge(L(B), L(C), L(D) , [B,C,D])
        - 对于merge([X], [Y], [Z]),遍历XYZ序列的第一个元素，若该元素是其他序列中的第一个元素，或不在其他序列出现，
            则从所有序列中删除这个元素，合并到MRO中，继续遍历X的下一个元素；否则保留该元素，并查看下一个序列的第一个元素是否符合规则。
        - 直到merge操作的序列为空
        - 如果merge操作的序列无法为空，则说明不合法。

    在python中, 确定Z的父类方法调用顺序的算法计算如下:
    L(O) := [O]
    L(A) := [A] + merge(L(O), [O])
          = [A] + merge([O], [O])
          = [A, O]

    L(B) := [B, O]//计算过程类似L(A)
    L(C) := [C, O]//计算过程类似L(A)

    L(K1) := [K1] + merge(L(B), L(A), [B, A])
           = [K1] + merge([B, O], [A, O], [B, A])
           // 取第一个序列的第一个元素B, 发现B是第三个序列的第一个元素, 就把它合并到MRO中, 并从所有序列中删除
           = [K1, B] + merge([O], [A, O], [A]) 
           // 取第一个序列的第一个元素O, 发现O既不是其他序列的第一个元素, 也不是不在其他序列中出现
           // 所以取第二个序列的第一个元素A, 发现符合规则, 合并到MRO中并从所有序列中删除
           = [K1, B, A] + merge([O], [O]) 
           = [K1, B, A, O]

     L(K2) := [K2, C, A, O]//计算过程类似L(K1)

     L(Z) := [Z] + merge(L(K2), L(K1), [K2, K1])
           = [Z] + merge([K2, C, A, O], [K1, B, A, O], [K2, K1])
           // 取第一个序列的第一个元素K2, 发现K2是第三个序列的第一个元素, 就把它合并到MRO中, 并从所有序列中删除
           = [Z, K2] + merge([C, A, O], [K1, B, A, O], [K1])
           // 取第一个序列的第一个元素C, 发现他没有出现在其他序列中, 就把它合并到MRO中, 并从所有序列中删除
           = [Z, K2, C] + merge([A, O], [K1, B, A, O], [K1])
           // 取第一个序列的第一个元素A, 发现A既不是其他序列的第一个元素, 也不是不在其他序列中出现
           // 所以取第二个序列的第一个元素K1, 发现他是第三个序列的第一个元素, 合并到MRO中并从所有序列中删除
           = [Z, K2, C, K1] + merge([A, O], [B, A, O])
           // 取第一个序列的第一个元素A, 发现A既不是其他序列的第一个元素, 也不是不在其他序列中出现
           // 所以取第二个序列的第一个元素B, 发现他没有出现在其他序列中, 合并到MRO中并从所有序列中删除
           = [Z, K2, C, K1, B] + merge([A, O], [A, O])
           // 取第一个序列的第一个元素A, 发现A是第二个序列的第一个元素, 就把它合并到MRO中, 并从所有序列中删除
           = [Z, K2, C, K1, B, A] + merge([O], [O])
           = [Z, K2, C, K1, B, A, O]
    所以在python中, 上面那个例子的函数查找顺序是[Z, K2, C, K1, B, A, O], 构造函数执行顺序是[O,A,B,K1,C,K2,Z]
    可以在python中使用一下代码验证: 
    #!/usr/bin/python
    class O(object):
        def echo(self):
            print ("I am class_O")
    class A(O):
        pass

    class B(O):
        pass

    class C(O):
        pass

    class K1(B,A):
        def echo(self):
            print ("I am class_K1")

    class K2(C,A):
        pass

    class Z(K2,K1):
        pass
    print (Z.__mro__)


    在solidity中, 与python不同的是 A is B,C 等效于 A -> C, B
    所以在solidity中, 上面的例子计算如下: 
    L(O) := [O]
    L(A) := [A] + merge(L(O), [O])
          = [A] + merge([O], [O])
          = [A, O]

    L(B) := [B, O]//计算过程类似L(A)
    L(C) := [C, O]//计算过程类似L(A)

    L(K1) := [K1] + merge(L(A), L(B), [A, B])
           = [K1] + merge([A, O], [B, O], [A, B])
           //取一个序列的第一个元素，是其他序列中的第一个元素，或不在其他序列出现，符合条件是B
           = [K1, A] + merge([O], [B, O], [B])
           = [K1, A, B] + merge([O], [O])
           = [K1, A, B, O]

     L(K2) := [K2, A, C, O]//计算过程类似L(K1)

     L(Z) := [Z] + merge(L(K1), L(K2), [K1, K2])
           = [Z] + merge([K1, A, B, O], [K2, A, C, O], [K1, K2])
           // 取K1, 符合规则
           = [Z, K1] + merge([A, B, O], [K2, A, C, O], [K2])
           // 取A, 不符合规则, 取K2符合规则
           = [Z, K1, K2] + merge([A, B, O], [A, C, O])
           // 取A符合规则
           = [Z, K1, K2, A] + merge([B, O], [C, O])
           // 去B符合规则
           = [Z, K1, K2, A, B] + merge([O], [C, O])
           // 取O不符合规则, 取C符合规则
           = [Z, K1, K2, A, B, C] + merge([O], [O])
           = [Z, K1, K2, A, B, C, O]
    所以在solidity中, 方法的查找顺序是[Z, K1, K2, A, B, C, O], 构造函数的执行顺序是: [O,C,B,A,K2,K1,Z]
    使用以下代码进行验证

*/

contract O {
    constructor() {
        log("O");
    }

    event Log(string message);

    function log(string memory message) internal {
        emit Log(message);
    }

    address payable owner;

    function foo() public virtual {
        log("O");
    }
}

contract A is O {
    constructor() {
        log("A");
    }

    function foo() public virtual override {
        log("A");
        super.foo();
    }
}

contract B is O {
    constructor() {
        log("B");
    }

    function foo() public virtual override {
        log("B");
        super.foo();
    }
}

contract C is O {
    constructor() {
        log("C");
    }

    function foo() public virtual override {
        log("C");
        super.foo();
    }
}

contract K1 is B, A {
    constructor() {
        log("K1");
    }

    function foo() public virtual override(B, A) {
        log("K1");
        super.foo();
    }
}

contract K2 is C, A {
    constructor() {
        log("K2");
    }

    function foo() public virtual override(C, A) {
        log("K2");
        super.foo();
    }
}

contract Z is K2, K1 {
    constructor() {
        log("Z"); // O C B A K2 K1 Z
    }
    // Z K1 K2 A B C O
    function foo() public virtual override(K2, K1) {
        log("Z");
        super.foo();
    }
}
