// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

/**
    可以使用 string.concat 连接任意数量的 string 字符串。 
    该函数返回一个 string memory ，包含所有参数的内容，无填充方式拼接在一起。 
    如果你想使用不能隐式转换为 string 的其他类型作为参数，你需要先把它们转换为 string。

    bytes.concat 函数可以连接任意数量的 bytes 或 bytes1 ... bytes32 值。 
    该函数返回一个 bytes memory ，包含所有参数的内容，无填充方式拼接在一起。 
    如果你想使用字符串参数或其他不能隐式转换为 bytes 的类型，你需要先将它们转换为 bytes或bytes1/…/ bytes32。
 */

 contract C {
    string s = "Storage";
    function f(bytes calldata bc, string memory sm, bytes16 b) public view {
        string memory concatString = string.concat(s, string(bc), "Literal", sm);
        assert((bytes(s).length + bc.length + 7 + bytes(sm).length) == bytes(concatString).length);

        bytes memory concatBytes = bytes.concat(bytes(s), bc, bc[:2], "Literal", bytes(sm), b);
        assert((bytes(s).length + bc.length + 2 + 7 + bytes(sm).length + b.length) == concatBytes.length);
    }
}
