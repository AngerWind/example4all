// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

/**
    delete a 的结果是将a类型初始值赋值给a。
    - 对于整型变量来说，相当于 a = 0
    - 对于枚举, 重置为序号为0的值
    - 对于动态数组来说，是将重置为数组长度为0的数组，
    - 对于静态数组来说，是将数组中的所有元素重置为初始值。
    - 对数组而言， delete a[x] 仅删除数组索引 x 处的元素，其他的元素和长度不变，这以为着数组中留出了一个空位。
    - 对于结构体，结果将重置所有的非映射属性（成员），这个过程是递归进行的。
    - delete 对整个映射是无效的, 然而，单个的键及其映射的值是可以被删除的

理解 delete a 的效果就像是给 a 赋值很重要，换句话说，这相当于在 a 中存储了一个新的对象。

当 a 是引用变量时，我们可以看到这个区别， delete a 它只会重置 a 本身，而不是更改它之前引用的值。

 */

enum Size {
    SMALL,
    BIG,
    HUGE
}
struct StrExample{
    string message;
    uint[] array;
    mapping (address => uint) balance;
}
contract DeleteLBC {
    uint public data = 9;
    uint[] public dataArray = [1, 2, 3, 4];
    uint[4] public fixedArray = [1, 2, 3, 4];
    mapping(address => uint) balance;

    function f() public {
        uint x = data;
        delete x; // 将 x 设为 0，并不影响data
        delete data; // 将 data 设为 0，并不影响 x，因为它仍然有个副本
        uint[] storage y = dataArray;

        // 将 dataArray.length 设为 0，但由于 uint[] 是一个复杂的对象，y 也将受到影响，
        // 因为它是一个存储位置是 storage 的对象的别名。
        // 另一方面："delete y" 是非法的，引用了 storage 对象的局部变量只能由已有的 storage 对象赋值。
        delete dataArray;
        
        
        assert(y.length == 0);
    }
}