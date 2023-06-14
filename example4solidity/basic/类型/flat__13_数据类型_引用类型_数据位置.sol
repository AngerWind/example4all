
/** 
 *  SourceUnit: c:\Users\Administrator\Desktop\example4all\example4solidity\basic\类型\_13_数据类型_引用类型_数据位置.sol
*/

////// SPDX-License-Identifier-FLATTEN-SUPPRESS-WARNING:GPL-3.0

pragma solidity ^0.8.17;

contract DataLocationTest {
    /**
        所有的引用类型: 数组,结构体都有一个额外的表示数据位置的注解, 用来说明数据存储的位置
        memory(内存), storage(存储), calldata(调用数据)

        storage: 表示该变量保存在区块链上, 只要合约存在就一直存储
        memory: 同我们普通程序的内存类似。即分配，即使用，越过作用域即不可被访问，等待被回收。
            不能用于外部调用
        calldata: 与memory类似, 但是修饰的变量时只读的

        storage关键字只能使用在internal和private的函数上
        一般情况下external函数中使用calldata, public函数中使用memory，以及internal和private函数中的 memory 或 storage 。 
     */
    /**
        数据位置与赋值行为: 
            - 从内存/calldata 到 存储 之间的两两赋值, 都会创建一份独立的拷贝
            - 从内存到内存的赋值, 只创建引用, 这意味着改变内存变量, 其他引用相同数据的所有内存变量的值也会跟着改变。
            - 从存储到本地存储变量的赋值只分配一个引用
            - 其他位置的数据向存储进行赋值, 总是进行拷贝.  这种情况下对状态变量或 存储 的结构体类型的局部变量成员的赋值，即使局部变量本身是一个引用，也会进行一份拷贝
     */
    uint[] stateVar = [1, 4, 5]; // 合约的成员变量只能为storage, 可以忽略

    function foo() public {
        // case 1 : from storage to memory
        uint[] memory y = stateVar; // copy the content of stateVar to y

        y[0] = 12;
        y[1] = 20;
        y[2] = 24;

        // case 2 : from memory to storage
        stateVar = y; // copy the content of y to stateVar

        // case 3 : from storage to storage
        uint[] storage z = stateVar; // z is a pointer to stateVar

        z[0] = 38;
        z[1] = 89;
        z[2] = 72;
    }

    function boo(string storage a) private pure {}
}

contract Tiny {
    uint[] x; // x 的数据存储位置是 storage，　位置可以忽略

    // memoryArray 的数据存储位置是 memory
    function f(uint[] memory memoryArray) public {
        x = memoryArray; // 将整个数组拷贝到 storage 中，可行
        uint[] storage y = x; // 分配一个指针（其中 y 的数据存储位置是 storage），可行
        y[7]; // 返回第 8 个元素，可行
        y.pop(); // 通过 y 修改 x，可行
        delete x; // 清除数组，同时修改 y，可行

        // The following does not work; it would need to create a new temporary /
        // unnamed array in storage, but storage is "statically" allocated:
        // 本地存储变量只能通过已经存在的存储变量来赋值
        // y = memoryArray;

        // It would "reset" the pointer, but there is no sensible location it could point to.
        // For more details see the documentation of the "delete" operator.
        // delete y;

        g(x); // 调用 g 函数，同时移交对 x 的引用
        h(x); // 调用 h 函数，同时在 memory 中创建一个独立的临时拷贝
    }

    function g(uint[] storage) internal pure {}

    function h(uint[] memory) public pure {}
}

