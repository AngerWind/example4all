// SPDX-License-Identifier:GPL-3.0
pragma solidity ^0.8.17;

import "./IERC777.sol";
import "../_01_erc20/IERC20.sol";
import "../_02_erc721/Address.sol";
import "../_02_erc721/Strings.sol";
import "../_07_ERC1820/IERC1820Registry.sol";
import "./IERC777Sender.sol";
import "./IERC777Recipient.sol";

/**
 * 该接口实现了erc20和erc777, IERC20-Transfer和IERC777-Send都会在token转移时触发
 * 子类必须使用_mint来创建代币的供应机制
 *
 * granularity被硬编码为1
 */
contract ERC777 is IERC777, IERC20 {
    using Address for address;

    IERC1820Registry internal constant _ERC1820_REGISTRY = IERC1820Registry(0x1820a4B7618BdE71Dce8cdc73aAB6C95905faD24);

    mapping(address => uint256) private _balances;

    uint256 private _totalSupply;

    string private _name;
    string private _symbol;

    bytes32 private constant _TOKENS_SENDER_INTERFACE_HASH = keccak256("ERC777TokensSender"); // Sender的回调函数签名
    bytes32 private constant _TOKENS_RECIPIENT_INTERFACE_HASH = keccak256("ERC777TokensRecipient"); // Recipient的回调函数签名

    address[] private _defaultOperatorsArray; // 用来返回所有的default operator(在其他语言中map是可枚举的所以一个map就可以代替这两个的功能)
    mapping(address => bool) private _defaultOperators; // 用以查询某个operator是否是default operator

    mapping(address => mapping(address => bool)) private _operators; // 记录holder授权的operator
    mapping(address => mapping(address => bool)) private _revokedDefaultOperators; // 记录holder撤销的default operator

    mapping(address => mapping(address => uint256)) private _allowances; // erc20的授权

    /**
     * `defaultOperators`可以是空数组
     */
    constructor(string memory name_, string memory symbol_, address[] memory defaultOperators_) {
        _name = name_;
        _symbol = symbol_;

        _defaultOperatorsArray = defaultOperators_;
        for (uint256 i = 0; i < defaultOperators_.length; i++) {
            _defaultOperators[defaultOperators_[i]] = true;
        }

        // 向ERC1820注册接口
        _ERC1820_REGISTRY.setInterfaceImplementer(address(this), keccak256("ERC777Token"), address(this));
        _ERC1820_REGISTRY.setInterfaceImplementer(address(this), keccak256("ERC20Token"), address(this));
    }

    function name() public view virtual override returns (string memory) {
        return _name;
    }

    function symbol() public view virtual override returns (string memory) {
        return _symbol;
    }

    function decimals() public pure virtual returns (uint8) {
        return 18; // 固定为18
    }

    // 在当前合约中是1 (大多数情况)
    function granularity() public view virtual override returns (uint256) {
        return 1;
    }

    function totalSupply() public view virtual override(IERC20, IERC777) returns (uint256) {
        return _totalSupply;
    }

    function balanceOf(address tokenHolder) public view virtual override(IERC20, IERC777) returns (uint256) {
        return _balances[tokenHolder];
    }

    function send(address recipient, uint256 amount, bytes memory data) public virtual override {
        _send(msg.sender, recipient, amount, data, "", true);
    }

    // ERC20
    function transfer(address recipient, uint256 amount) public virtual override returns (bool) {
        _send(msg.sender, recipient, amount, "", "", false);
        return true;
    }

    /**
     * 销毁
     *
     * Also emits a {IERC20-Transfer} event for ERC20 compatibility.
     */
    function burn(uint256 amount, bytes memory data) public virtual override {
        _burn(msg.sender, amount, data, "");
    }

    /**
     * 判断operator是否是holder真实的operator
     */
    function isOperatorFor(address operator, address tokenHolder) public view virtual override returns (bool) {
        // operator是holder自己
        // 或者operator是default operator并且holder没有删除他对该default operator的授权
        // 或者operator是holder授权的operator
        return
            operator == tokenHolder ||
            (_defaultOperators[operator] && !_revokedDefaultOperators[tokenHolder][operator]) ||
            _operators[tokenHolder][operator];
    }

    /**
     * caller授权operator
     */
    function authorizeOperator(address operator) public virtual override {
        require(msg.sender != operator, "ERC777: authorizing self as operator");

        if (_defaultOperators[operator]) {
            // 如果之前该holder删除了该default operator的授权, 那么就恢复授权
            delete _revokedDefaultOperators[msg.sender][operator];
        } else {
            // 否则直接记录授权
            _operators[msg.sender][operator] = true;
        }

        emit AuthorizedOperator(operator, msg.sender);
    }

    /**
     * 取消operator的授权
     */
    function revokeOperator(address operator) public virtual override {
        require(operator != msg.sender, "ERC777: revoking self as operator");

        if (_defaultOperators[operator]) {
            // 看看是不是default operator, 如果是就记录一下该holder撤销了该default operator
            _revokedDefaultOperators[msg.sender][operator] = true;
        } else {
            delete _operators[msg.sender][operator]; // 否则就直接删除授权
        }

        emit RevokedOperator(operator, msg.sender);
    }

    function defaultOperators() public view virtual override returns (address[] memory) {
        return _defaultOperatorsArray;
    }

    /**
     * operator负责的转账
     * Emits {Sent} and {IERC20-Transfer} events.
     */
    function operatorSend(
        address sender,
        address recipient,
        uint256 amount,
        bytes memory data,
        bytes memory operatorData
    ) public virtual override {
        require(isOperatorFor(msg.sender, sender), "ERC777: caller is not an operator for holder");
        _send(sender, recipient, amount, data, operatorData, true);
    }

    /**
     * Emits {Burned} and {IERC20-Transfer} events.
     */
    function operatorBurn(
        address account,
        uint256 amount,
        bytes memory data,
        bytes memory operatorData
    ) public virtual override {
        require(isOperatorFor(msg.sender, account), "ERC777: caller is not an operator for holder");
        _burn(account, amount, data, operatorData);
    }

    // 获取授权额度
    function allowance(address holder, address spender) public view virtual override returns (uint256) {
        return _allowances[holder][spender];
    }

    // 授权
    function approve(address spender, uint256 value) public virtual override returns (bool) {
        address holder = msg.sender;
        _approve(holder, spender, value);
        return true;
    }

    /**
     * 如果owner给recipient的额度是无线大的话, 就不更新额度了
     * erc20的transferFrom和erc777的operatorSend采用独立的规则
     * 即transferFrom是检测caller有没有授权额度, 而operatorSend检测的是caller是不是operator
     */
    function transferFrom(address holder, address recipient, uint256 amount) public virtual override returns (bool) {
        address spender = msg.sender;
        _spendAllowance(holder, spender, amount); // 花费掉holder对caller的授权额度
        _send(holder, recipient, amount, "", "", false); // 转账
        return true;
    }

    /**
     * 铸造并分配代币
     */
    function _mint(address account, uint256 amount, bytes memory userData, bytes memory operatorData) internal virtual {
        _mint(account, amount, userData, operatorData, true);
    }

    /**
     * 铸造明确分配代币
     */
    function _mint(
        address account,
        uint256 amount,
        bytes memory userData,
        bytes memory operatorData,
        bool requireReceptionAck
    ) internal virtual {
        require(account != address(0), "ERC777: mint to the zero address");

        address operator = msg.sender;

        _beforeTokenTransfer(operator, address(0), account, amount); // 子类的回调

        _totalSupply += amount; // 总量增加
        _balances[account] += amount; // 该账户的余额增加

        _callTokensReceived(operator, address(0), account, amount, userData, operatorData, requireReceptionAck); // receiver的回调

        emit Minted(operator, account, amount, userData, operatorData); // 触发事件
        emit Transfer(address(0), account, amount);
    }

    // 转移token
    function _send(
        address from,
        address to,
        uint256 amount,
        bytes memory userData,
        bytes memory operatorData,
        bool requireReceptionAck
    ) internal virtual {
        require(from != address(0), "ERC777: transfer from the zero address");
        require(to != address(0), "ERC777: transfer to the zero address");

        address operator = msg.sender;

        _callTokensToSend(operator, from, to, amount, userData, operatorData); // sender的回调

        _move(operator, from, to, amount, userData, operatorData); // 转移token

        _callTokensReceived(operator, from, to, amount, userData, operatorData, requireReceptionAck); // receiver的回调
    }

    /**
     * 销毁token
     */
    function _burn(address from, uint256 amount, bytes memory data, bytes memory operatorData) internal virtual {
        require(from != address(0), "ERC777: burn from the zero address");

        address operator = msg.sender;

        _callTokensToSend(operator, from, address(0), amount, data, operatorData); // 调用sender的回调

        _beforeTokenTransfer(operator, from, address(0), amount); // 子类的回调

        // Update state variables
        uint256 fromBalance = _balances[from];
        require(fromBalance >= amount, "ERC777: burn amount exceeds balance");
        unchecked {
            _balances[from] = fromBalance - amount; // 减掉token
        }
        _totalSupply -= amount; // 从总的供应量中减去

        emit Burned(operator, from, amount, data, operatorData); // 触发事件
        emit Transfer(from, address(0), amount);
    }

    // token转移
    function _move(
        address operator,
        address from,
        address to,
        uint256 amount,
        bytes memory userData,
        bytes memory operatorData
    ) private {
        _beforeTokenTransfer(operator, from, to, amount); // 该方法由子类实现

        uint256 fromBalance = _balances[from];
        require(fromBalance >= amount, "ERC777: transfer amount exceeds balance");
        unchecked {
            _balances[from] = fromBalance - amount;
        }
        _balances[to] += amount;

        emit Sent(operator, from, to, amount, userData, operatorData); // 触发事件
        emit Transfer(from, to, amount);
    }

    // erc20授权
    function _approve(address holder, address spender, uint256 value) internal virtual {
        require(holder != address(0), "ERC777: approve from the zero address");
        require(spender != address(0), "ERC777: approve to the zero address");

        _allowances[holder][spender] = value;
        emit Approval(holder, spender, value);
    }

    /**
     * 调用sender的回调
     */
    function _callTokensToSend(
        address operator,
        address from,
        address to,
        uint256 amount,
        bytes memory userData,
        bytes memory operatorData
    ) private {
        address implementer = _ERC1820_REGISTRY.getInterfaceImplementer(from, _TOKENS_SENDER_INTERFACE_HASH); // 获取sender的回调地址
        if (implementer != address(0)) {
            IERC777Sender(implementer).tokensToSend(operator, from, to, amount, userData, operatorData); // call
        }
    }

    /**
     * 调用receiver的回调
     * @param requireReceptionAck if true, contract recipients are required to implement ERC777TokensRecipient
     */
    function _callTokensReceived(
        address operator,
        address from,
        address to,
        uint256 amount,
        bytes memory userData,
        bytes memory operatorData,
        bool requireReceptionAck
    ) private {
        address implementer = _ERC1820_REGISTRY.getInterfaceImplementer(to, _TOKENS_RECIPIENT_INTERFACE_HASH);
        if (implementer != address(0)) {
            IERC777Recipient(implementer).tokensReceived(operator, from, to, amount, userData, operatorData);
        } else if (requireReceptionAck) {
            require(!to.isContract(), "ERC777: token recipient contract has no implementer for ERC777TokensRecipient");
        }
    }

    /**
     * 消耗掉owner给spender的授权额度
     * 如果owner给owner的授权额度是无限大的话, 就不更新allowance
     *
     */
    function _spendAllowance(address owner, address spender, uint256 amount) internal virtual {
        uint256 currentAllowance = allowance(owner, spender);
        // 如果owner给spender的授权额度是无限大的话, 就不更新allowance
        if (currentAllowance != type(uint256).max) {
            require(currentAllowance >= amount, "ERC777: insufficient allowance");
            unchecked {
                _approve(owner, spender, currentAllowance - amount); // 这里通过_approve来更新allowance, 会触发Approval事件????????
            }
        }
    }

    /**
     * @dev Hook that is called before any token transfer. This includes
     * calls to {send}, {transfer}, {operatorSend}, {transferFrom}, minting and burning.
     *
     * Calling conditions:
     *
     * - when `from` and `to` are both non-zero, `amount` of ``from``'s tokens
     * will be to transferred to `to`.
     * - when `from` is zero, `amount` tokens will be minted for `to`.
     * - when `to` is zero, `amount` of ``from``'s tokens will be burned.
     * - `from` and `to` are never both zero.
     *
     * To learn more about hooks, head to xref:ROOT:extending-contracts.adoc#using-hooks[Using Hooks].
     */
    function _beforeTokenTransfer(address operator, address from, address to, uint256 amount) internal virtual {}
}
