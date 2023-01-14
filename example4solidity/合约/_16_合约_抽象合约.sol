// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

/**
    - 如果未实现合约中的至少一个函数，或者合约没有为其所有的基类合约构造函数提供参数时, 
        则必须将合约标记为 abstract。 
    - 即使实现了所有功能，合约也可能被标记为abstract。
    - 这样的抽象合约不能直接实例化。
 */



// 由于 utterance() 函数没有具体的实现, 必须标记为abstract
abstract contract Feline {
  function utterance() public pure virtual returns (bytes32);
}

contract Cat is Feline {
  function utterance() public pure override returns (bytes32) { return "miaow"; }
}

