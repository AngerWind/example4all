
/** 
 *  SourceUnit: c:\Users\Administrator\Desktop\example4all\example4solidity\basic\类型\_13_数据类型_值类型_函数.sol
*/

////// SPDX-License-Identifier-FLATTEN-SUPPRESS-WARNING:GPL-3.0

pragma solidity ^0.8.17;

/**
    - 函数类型是一种表示函数的类型。
    - 可以将一个函数赋值给另一个函数类型的变量，也可以将一个函数作为参数进行传递，还能在函数调用中返回函数类型变量。 
    - 函数类型有两类： 
        - 内部（internal） 函数类型 
            内部函数只能在当前合约内被调用, 调用一个内部函数是通过跳转到它的入口标签来实现的
        - 外部（external） 函数类型
            外部函数由一个地址和一个函数签名组成，可以通过外部函数调用传递或者返回。

    - 函数形式如下: 
        function (<parameter types>) {internal|external} [pure|constant|view|payable] [returns (<return types>)]

        如果函数类型不需要返回，则需要删除整个 returns (<return types>) 部分。
        函数类型默认是内部函数，因此不需要声明 internal 关键字。

    - 函数类型转换:
        函数类型 A 可以隐式转换为函数类型 B 当且仅当: 它们的参数类型相同，返回类型相同，它们的内部/外部属性是相同的，并且 A 的状态可变性比 B 的状态可变性更具限制性
        比如：
            pure函数可以转换为 view 和 non-payable 函数
            view函数可以转换为 non-payable 函数,  view不修改成员变量, 而non-payable函数不接受以太
            payable函数可以转换为 non-payable 函数, 如果一个函数是 payable ，这意味着它 也接受零以太的支付，因此它也是 non-payable 。

    - public或者external的函数都有以下的成员
        .address, 返回函数的合约的地址
        .selector, 返回bytes4类型的abi函数选择器
 */


 // ------------------------------ 使用函数成员的例子 ------------------------
 contract Example {
  function f() public payable returns (bytes4) {
    assert(this.f.address == address(this));
    return this.f.selector;
  }
  function g() public {
    this.f{gas: 10, value: 800}();
  }
}

// ------------------------------- 使用内部函数的例子 -----------------------
library ArrayUtils {
  // 内部函数可以在内部库函数中使用，
  // 因为它们会成为同一代码上下文的一部分
  function map(uint[] memory self, function (uint) pure returns (uint) f) internal pure returns (uint[] memory r) {
    r = new uint[](self.length);
    for (uint i = 0; i < self.length; i++) {
      r[i] = f(self[i]);
    }
  }
  function reduce( uint[] memory self, function (uint, uint) pure returns (uint) f ) internal pure returns (uint r) {
    r = self[0];
    for (uint i = 1; i < self.length; i++) {
      r = f(r, self[i]);
    }
  }
  function range(uint length) internal pure returns (uint[] memory r) {
    r = new uint[](length);
    for (uint i = 0; i < r.length; i++) {
      r[i] = i;
    }
  }
}

contract Pyramid {
  using ArrayUtils for *;
  function pyramid(uint l) public pure returns (uint) {
    return ArrayUtils.range(l).map(square).reduce(sum);
  }
  function square(uint x) internal pure returns (uint) {
    return x * x;
  }
  function sum(uint x, uint y) internal pure returns (uint) {
    return x + y;
  }
}

// -------------------------- 使用外部函数的例子 -----------------------------
// 这是一个预言机的案例
contract Oracle {
  struct Request {
    bytes data;
    function(uint) external callback;
  }
  Request[] private requests;
  event NewRequest(uint);
  // 外部的js监听这个事件
  function query(bytes memory data, function(uint) external callback) public {
    requests.push(Request(data, callback));
    emit NewRequest(requests.length - 1);
  }

  // 外部的JS监听到事件后, 查询相应的价格, 并调用这个方法来设置价格
  function reply(uint requestID, uint response) public {
    // 这里检查回复来自可信来源
    requests[requestID].callback(response);
  }
}

contract OracleUser {
  Oracle constant private ORACLE_CONST = Oracle(address(0x00000000219ab540356cBB839Cbe05303d7705Fa)); // known contract
  uint private exchangeRate;
  function buySomething() public {
    // 查询USD的价格, 并设置回调函数
    ORACLE_CONST.query("USD", this.oracleResponse);
  }
  function oracleResponse(uint response) public {
    require(
        msg.sender == address(ORACLE_CONST),
        "Only oracle can call this."
    );
    exchangeRate = response;
  }
}

