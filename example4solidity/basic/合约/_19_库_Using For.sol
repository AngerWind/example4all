// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

/**
    - 使用 using A for B 将函数附加到任何类型上作为成员函数
    - 这些函数将接收调用他们的对象作为他们的第一个参数
    - using for 只在声明的作用域中有效, 出了作用域就无效了
    - 当using for 在文件级别使用时, 并应用于一个自定义用户, 可以在末尾添加global关键字, 
        产生的效果是，这些函数被附加到使用该类型的任何地方（包括其他文件），而不仅仅是声明处所在的作用域。

    - 使用 using {f, g, h, L.t} for uint 可以指定一些函数附加到指定类型上
    - 使用 using L for uint 指定库中的所有函数(包括public和internal函数)添加到类型上
 */
struct Data {
    mapping(uint => bool) flags;
}
// Now we attach functions to the type.
// Data类型可以在当前文件中使用附加的函数
// 如果当前文件被import到其他文件中, 还想Data可以使用这些函数, 必须在其他文件中再次重复using for
//   import "flags.sol" as Flags;
//   using {Flags.insert, Flags.remove, Flags.contains}
//     for Flags.Data;
using {insert, remove, contains} for Data; // 通过using {} for 的形式为Data附加函数

// 使用global关键字, 即使被import, Data也可以直接使用这些附加函数
// using {insert, remove, contains} for Data global;

// 这是自由函数, 不能设置可见性, 强制internal
function insert(Data storage self, uint value) returns (bool) {
    if (self.flags[value]) return false; // already there
    self.flags[value] = true;
    return true;
}

function remove(Data storage self, uint value) returns (bool) {
    if (!self.flags[value]) return false; // not there
    self.flags[value] = false;
    return true;
}

function contains(Data storage self, uint value) view returns (bool) {
    return self.flags[value];
}

contract C {
    Data knownValues;

    function register(uint value) public {
        require(knownValues.insert(value)); // Data可以调用insert, remove, contains三个函数
    }
}

//  library SetLib {
//     function insert(Data storage self, uint value) public returns (bool){
//         if (self.flags[value])
//             return false; // already there
//         self.flags[value] = true;
//         return true;
//         }
//     function remove(Data storage self, uint value) public returns (bool) {
//         if (!self.flags[value])
//             return false; // not there
//         self.flags[value] = false;
//         return true;
//     }
//     function contains(Data storage self, uint value) public view returns (bool) {
//         return self.flags[value];
//     }
// }
// using SetLib for Data; // 使用using A for b 的形式附加函数, 达到与上面相同的效果
