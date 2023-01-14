// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

contract HelloWorld {
    /**
        bytes和string是特殊的数组, bytes 类似于 bytes1[]，但它在 调用数据calldata 和 内存memory 中会被“紧打包”（译者注：将元素连续地存在一起，不会按每 32 字节一单元的方式来存放）。 
        string不同通过索引访问, 也没有.length, pop, push方法

        bytes有.length, storage的bytes有push和pop
    */

    string a = "message";
    bytes storageBytes = new bytes(200);

    function test2() public  {
        // 创建一个动态字节数组：
        bytes memory b = new bytes(200);
        for (uint256 i = 0; i < b.length; i++) {
            b[i] = bytes1(uint8(i));
        }

        storageBytes[0] = 0xff;
        storageBytes.push(0xaa);
        storageBytes.pop();
    }

    /* 
        比较字符串: keccak256(abi.encodePacked(s1)) == keccak256(abi.encodePacked(s2))
        连接两个字符串: string.concat(s1, s2)
        以字节形式访问字符串: 
            bytes(s).length;  
            bytes(s)[7] = 'x';

        bytes.concat 函数可以连接任意数量的 bytes 或 bytes1 ... bytes32 值
        如果你想使用字符串参数或其他不能隐式转换为 bytes 的类型，你需要先将它们转换为 bytes或 bytes1/…/ bytes32。
    */
    function test1() public pure {
        string memory s1 = "hello";
        string memory s2 = "world";
        // 比较字符串
        bool same = keccak256(abi.encodePacked(s1)) ==
            keccak256(abi.encodePacked(s2));
        // 连接字符串
        string memory concat = string.concat(s1, s2);
        // 获取字符串的字节数
        uint256 len = bytes(s1).length;
        // 修改字符串的字节内容
        bytes(s1)[0] = "x";

        // 连接字节数组
        bytes memory array = new bytes(2);
        array[1] = 0xff;
        array[0] = 0xaa;
        bytes memory concatBytes = bytes.concat(array, bytes1(0xbb));
    }
}
