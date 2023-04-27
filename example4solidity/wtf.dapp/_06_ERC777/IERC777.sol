// SPDX-License-Identifier:GPL-3.0
pragma solidity ^0.8.17;

/**
 *
 * https://learnblockchain.cn/2019/09/27/erc777/
 *
 *
 * erc777与erc20类似, 都是同质化代币的标准
 * 但是出现erc777的原因是erc20的一些缺陷, 例如:
 *     1. 当从from到to转账时, to无法在链上进行监听, 除非to在链下主动查询
 *     2. 当从from到to转账时, to无法知晓转账原因, 也不能第一时间知晓回调
 *     3. 当授权的账户动用了账户的金额时, 无法对持有者进行回调, 通知, 也无法知晓原因
 *
 * 所以诞生了erc777, 他能够在转账时附带一些额外的信息, 并且如果to是一个合约, 那么会调用他在erc1820上面注册的接口, 达到回调的效果
 *
 * erc777为了兼容erc20, 查询函数和erc20一样, 但是操作接口为了避免混淆都使用了其他命名
 * 并且erc777固定了精度为777, 并且添加了一个新的属性granularity, 即他是任何操作的最小单元, 任何操作都必须是他的整数倍, 通常为1
 *
 * 在erc777中, 定义了操作员(operator)和持有者(holder)两个角色
 *  - holder是持有代币的地址, 他可以进行转账和销毁操作
 *  - operator经过holder授权后可以代表持有者进行转账和销毁操作
 *
 * 此外在创建erc777token时, 可以指定多个默认的operator,
 * 这些default operator类似于超级管理员, 可以对任何人持有的token进行转账和销毁操作
 * 当然holder也可以取消这些default operator和operator的权限
 */
interface IERC777 {
    // operator铸造amount个token并且分配给to的事件
    event Minted(address indexed operator, address indexed to, uint256 amount, bytes data, bytes operatorData);

    // operator销毁from amount个token的事件
    event Burned(address indexed operator, address indexed from, uint256 amount, bytes data, bytes operatorData);

    // operator授权摸个账户称为他的operator的事件
    event AuthorizedOperator(address indexed operator, address indexed tokenHolder);

    // holder移除了operator的权限的事件
    event RevokedOperator(address indexed operator, address indexed tokenHolder);

    function name() external view returns (string memory); // 名称

    function symbol() external view returns (string memory); // 代币符号

    // 定义不可划分的最小力度, 所有的mint, 转账, destory都必须是这个力度的整数倍,通常是1
    // 这个与erc20的decimals不同, erc20的decimals是小数位数,是可选函数
    // 而erc777的decimals为了兼容erc20固定了就是18
    function granularity() external view returns (uint256);

    function totalSupply() external view returns (uint256); // 总供应量

    function balanceOf(address owner) external view returns (uint256); // 查询某个地址的余额

    /**
     * 从caller转账amout个token到recipient
     * 如果caller和recipient都在erc1820上面注册了接口, 那么会调用他们的接口传入data和空的operatorData, See {IERC777Sender} and {IERC777Recipient}.
     *
     * 触发事件: {Sent}
     *
     * caller必须有足够的token
     * recipient不能是0地址
     * 如果recipient是一个合约, 那么他必须实现IERC777Recipient接口
     */
    function send(address recipient, uint256 amount, bytes calldata data) external;

    /**
     * 调用者销毁指定数量的token, 并且减少总供应量
     * 如果调用者在erc1820上面注册了接口, 那么会调用他的接口传入data和空的operatorData, See {IERC777Sender}
     *
     * 触发Burned事件
     */
    function burn(uint256 amount, bytes calldata data) external;

    /**
     * 判断operator是否是holder的真正的operator
     * 如果operator是default operator并且holder没有取消他的权限, 那么也会返回true
     * 任何holder都是他自己的operator
     */
    function isOperatorFor(address operator, address tokenHolder) external view returns (bool);

    /**
     * 调用者授权给operator
     * operator不能是调用者本身
     * 触发AuthorizedOperator事件
     */
    function authorizeOperator(address operator) external;

    /**
     * 调用者取消operator的权限
     * 如果operator是default operator, 那么他仍然是default operator, 只是holder单独取消了default operator对他的权限
     * 触发RevokedOperator事件
     *
     * operator不能是调用者本身
     */
    function revokeOperator(address operator) external;

    /**
     * 返回所有的default operator
     */
    function defaultOperators() external view returns (address[] memory);

    /**
     * operator从sender转账amount个token到recipient
     *
     * 如果sender和recipient在ERC1820上注册了接口, 那么会调用 See {IERC777Sender} and {IERC777Recipient}.
     *
     * 触发Sent事件
     *
     * sender不能是零地址
     * sender必须有amount个token
     * caller必须是sender的operator
     * 如果recipient是合约, 那么他必须实现IERC777Recipient接口
     */
    function operatorSend(
        address sender,
        address recipient,
        uint256 amount,
        bytes calldata data,
        bytes calldata operatorData
    ) external;

    /**
     * operator销毁account指定数量的token, 并且减少总供应量
     * 调用者必须是account的operator
     * 如果account在erc1820上面注册了接口, 那么会调用他的接口传入data和空的operatorData, See {IERC777Sender}
     *
     * 触发Burned事件
     *
     * account不能是0地址
     * account必须有至少amount的token
     * 调用者必须是account的operator
     */
    function operatorBurn(address account, uint256 amount, bytes calldata data, bytes calldata operatorData) external;

    event Sent(
        address indexed operator,
        address indexed from,
        address indexed to,
        uint256 amount,
        bytes data,
        bytes operatorData
    );
}
