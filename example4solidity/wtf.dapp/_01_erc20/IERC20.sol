// SPDX-License-Identifier:GPL-3.0
pragma solidity ^0.8.17;

/**
 * 下面是erc20的标准合约接口, 他定义了代币转账的基本逻辑
 *      账户余额
 *      转账
 *      授权转账
 *      代币总供给
 *      代币信息（可选）：名称，代号，小数位数
 * 其中定义了两个事件 Transfer和Approval, 分别在转账和授权时被释放
 * 定义了六个函数, 提供了转移代币的基本功能
 */
interface IERC20 {
    // 当token从一个账户转移到另外一个账户时, 触发该事件
    event Transfer(address indexed _from, address indexed _to, uint _value);

    // 当token从一个账户授权给另一个账户时, 触发该事件
    event Approval(address indexed _owner, address indexed _spender, uint _value);

    // 返回token的发行总量
    function totalSupply() external view returns (uint totalSupply); // 总发行量

    // 返回某个地址的token持有量
    function balanceOf(address _owner) external view returns (uint balance);

    /**
     * 从调用者转移代币到_to,如果成功返回true
     * 触发Transfer事件
     */
    function transfer(address _to, uint _value) external returns (bool success);

    /**
     * 调用者授权给spender账户amount数量代币, 会改变allowance
     * 触发Approval事件
     */
    function approve(address _spender, uint _value) external returns (bool success);

    /**
     * 返回owner账户授权给spender账户的额度, 默认为0
     * 当调用 approve 或者 transferFrom时, allowance会改变
     */
    function allowance(address _owner, address _spender) external view returns (uint remaining);

    /**
     * 通过授权机制, 从from账户向to账户转账token
     * 转账部分会从allowance中扣除
     *
     * 触发Transfer事件
     */
    function transferFrom(address _from, address _to, uint _value) external returns (bool success);
}
