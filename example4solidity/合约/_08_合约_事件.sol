// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

/**
    - Solidity 事件是EVM的日志功能之上的抽象。 
        应用程序可以 **通过以太坊客户端的RPC接口订阅和监听** 这些事件。
    - 事件在合约中可被继承。当他们被调用时，会使参数被存储到交易的日志中 —— 一种区块链中的特殊数据结构。 
        这些日志与地址相关联，被并入区块链中，只要区块可以访问就一直存在（现在开始会被永久保存，在 Serenity 版本中可能会改动)。 
        日志和事件在合约内不可直接被访问（甚至是创建日志的合约也不能访问）。
    - 参考资料: 
        https://www.cnblogs.com/tinyxiong/p/9045274.html
        https://learnblockchain.cn/2018/04/15/web3-html/

 */

contract ClientReceipt {
    // 定义一个事件
    event Deposit(address indexed from, bytes32 indexed id, uint256 value);

    function deposit(bytes32 id) public payable {
        // 事件使用 emit 触发事件。
        // 可以使用web3.js中的函数来监听这个事件
        emit Deposit(msg.sender, id, msg.value);
    }
}
/**
    js代码监听上面的Deposit事件:

    // 获取合约的abi, 编译之后点击Compilation Details查看
    var abi = "从编译界面copy过来的合约的abi";
    var ClientReceipt = web3.eth.contract(abi);
    // 传入合约的地址
    var clientReceipt = ClientReceipt.at("0x1234...xlb67");
    
    var depositEvent = clientReceipt.Deposit();

    // 监听变化
    depositEvent.watch(function(error, result) {
        // 结果包含 非索引参数 以及 主题 topic
        if (!error)
            console.log(result);
    });

    // 或者通过传入回调函数，立即开始听监
    var depositEvent = clientReceipt.Deposit(function(error, result) {
        if (!error)
            console.log(result);
    });
 */
