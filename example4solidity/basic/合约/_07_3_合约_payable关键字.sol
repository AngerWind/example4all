// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

contract C {
    constructor() payable {}

    /**
        payable关键字添加在函数上, 表示在调用该函数时, 可以附带eth
        也可以添加在构造函数上面, 表示在构建的时候就可以携带一定的eth
     */
    function fund() public payable {}
}
