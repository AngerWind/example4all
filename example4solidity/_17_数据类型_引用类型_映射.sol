// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

/**
    - 映射类型在声明时的形式为 mapping(KeyType => ValueType)。 
    - KeyType 可以是任何基本类型，即可以是任何的内建类型， bytes 和 string 或合约类型、枚举类型。 
        而其他用户定义的类型或复杂的类型如：映射、结构体、即除 bytes 和 string 之外的数组类型是不可以作为 KeyType 的类型的。
    - ValueType 可以是包括映射类型在内的任何类型。

    - 在映射中，实际上并不存储 key，而是存储它的 keccak256 哈希值，从而便于查询实际的值。
        正因为如此，映射是没有长度的，也没有 key 的集合或 value 的集合的概念。因此如果没有其他信息键的信息是无法被删除

    - 映射只能是 存储storage 的数据位置，因此只允许作为状态变量 或 作为函数内的 存储storage 引用 或 作为库函数的参数。 
        它们不能用合约公有函数的参数或返回值。 这些限制同样适用于包含映射的数组和结构体。

    - 可以将映射声明为 public ，然后来让 Solidity 创建一个 getter 函数。 
        KeyType 将成为 getter 的必须参数，并且 getter 会返回 ValueType 。
        如果 ValueType 是一个映射。这时在使用 getter 时将需要递归地传入每个 KeyType 参数，
*/

contract MappingExample {
    mapping(address => uint256) public balances;

    // 对于mapping中没有的address, 总是返回0
    function get(address addr) public view returns (uint256) {
        return balances[addr];
    }

    function set(address addr, uint256 _balance) public {
        balances[addr] = _balance;
    }

    function remove(address addr) public  {
        delete balances[addr];
    }


    // 嵌套mapping
    struct Movie {
        string title;
        string director;
    }
    mapping(address => mapping(uint => Movie)) myMovie;
    function addMovie(uint id, string memory title, string memory director) public {
        myMovie[msg.sender][id] = Movie(title, director);
    }
}
