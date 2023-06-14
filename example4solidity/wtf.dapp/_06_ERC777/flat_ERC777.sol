
/** 
 *  SourceUnit: c:\Users\Administrator\Desktop\example4all\example4solidity\wtf.dapp\_06_ERC777\ERC777.sol
*/
            
////// SPDX-License-Identifier-FLATTEN-SUPPRESS-WARNING:GPL-3.0
pragma solidity ^0.8.17;

interface IERC777Recipient {
    /**
     * ERC777中, recipient的回调函数
     */
    function tokensReceived(
        address operator,
        address from,
        address to,
        uint amount,
        bytes calldata userData,
        bytes calldata operatorData
    ) external;
}




/** 
 *  SourceUnit: c:\Users\Administrator\Desktop\example4all\example4solidity\wtf.dapp\_06_ERC777\ERC777.sol
*/
            
////// SPDX-License-Identifier-FLATTEN-SUPPRESS-WARNING:GPL-3.0
pragma solidity ^0.8.17;

interface IERC777Sender {
    /**
     * ERC777中, sender的回调函数
     */
    function tokensToSend(
        address operator,
        address from,
        address to,
        uint amount,
        bytes calldata userData,
        bytes calldata operatorData
    ) external;
}




/** 
 *  SourceUnit: c:\Users\Administrator\Desktop\example4all\example4solidity\wtf.dapp\_06_ERC777\ERC777.sol
*/
            
////// SPDX-License-Identifier-FLATTEN-SUPPRESS-WARNING:GPL-3.0
pragma solidity ^0.8.17;

/**
 * 在很多接口中都要求执行合约的相关地址要能够进行回调,  如erc777要求sender和recipient为合约的地址都要在erc1820上面进行注册
 * 当地址执行一定的操作的时候, 执行合约能够找到地址是否有实现了要求的接口的相关合约, 并且进行回调
 *
 * 执行合约是怎么找到地址是否有实现了要求的接口的相关合约, 并且进行回调的呢?
 * 实现的办法是: 在链上部署当前合约的一个实例, 该实例整个链唯一, 并且要求所有实现了要求的接口的合约都要在该实例上面进行注册
 * 该实例通过一个mapping(address => mapping(bytes32 => address))来保存什么地址实现了什么类型的接口, 并且实现的合约在那个地址
 *
 * 这样执行合约就可以通过查询该实例, 来找到地址是否有实现了要求的接口, 并且进行回调
 *
 * 该实例的地址是固定的, 0x1820a4B7618BdE71Dce8cdc73aAB6C95905faD24
 *
 * implementer可以由多个地址共享, 每个地址也可以注册多个接口的implementer
 * 用户地址必须将implementer设置为合约
 *
 * 该合约与erc165的区别就是: 用户地址也可以指定相关的合约进行回调, 而erc165只能是合约声明自己支持什么接口
 *
 * 只有address的manager可以设置address的implementer
 * 默认情况下address的manager是他自己
 * 如果address设置了manager, 那么他就不能再设置implementer了, 只有manager可以设置implementer
 */
interface IERC1820Registry {
    // 某个地址设置了某种接口的implementer
    event InterfaceImplementerSet(address indexed account, bytes32 indexed interfaceHash, address indexed implementer);

    event ManagerChanged(address indexed account, address indexed newManager);

    /**
     * 授权给某个manager, 该manager可以替自己设置implementer
     * manaeer可以是0x00, 代表取消授权
     * 每个地址只能有一个manager
     *
     * caller必须是account的manager
     *
     * 触发ManagerChanged事件
     *
     * 每个address都是自己的manager
     */
    function setManager(address account, address newManager) external;

    /**
     * 获取address的manager
     */
    function getManager(address account) external view returns (address);

    /**
     * 设置address的implementer合约实现了interfaceHash接口
     *
     * address如果为0x00, 那么address就是caller的地址
     * implementer如果为0x00, 那么就是删除address的implementer
     *
     * 触发InterfaceImplementerSet事件
     *
     * caller必须是account的manager
     * interfaceHash不能是IERC165的接口id
     * implementer必须实现IERC1820Implementer接口, 并且返回true, 除非implementer就是caller
     *
     */
    function setInterfaceImplementer(address account, bytes32 _interfaceHash, address implementer) external;

    /**
     * 获取address的实现了interfaceHash接口的implementer
     * 如果没有返回0x00
     *
     * @dev Returns the implementer of `interfaceHash` for `account`. If no such
     * implementer is registered, returns the zero address.
     *
     * If `interfaceHash` is an {IERC165} interface id (i.e. it ends with 28
     * zeroes), `account` will be queried for support of it.
     *
     * `account` being the zero address is an alias for the caller's address.
     */
    function getInterfaceImplementer(address account, bytes32 _interfaceHash) external view returns (address);

    /**
     * 根据interfaceName获取interfaceHash
     * https://eips.ethereum.org/EIPS/eip-1820#interface-name[section of the EIP].
     */
    function interfaceHash(string calldata interfaceName) external pure returns (bytes32);

    /**
     * @notice Updates the cache with whether the contract implements an ERC165 interface or not.
     * @param account Address of the contract for which to update the cache.
     * @param interfaceId ERC165 interface for which to update the cache.
     */
    function updateERC165Cache(address account, bytes4 interfaceId) external;

    /**
     * @notice Checks whether a contract implements an ERC165 interface or not.
     * If the result is not cached a direct lookup on the contract address is performed.
     * If the result is not cached or the cached value is out-of-date, the cache MUST be updated manually by calling
     * {updateERC165Cache} with the contract address.
     * @param account Address of the contract to check.
     * @param interfaceId ERC165 interface to check.
     * @return True if `account` implements `interfaceId`, false otherwise.
     */
    function implementsERC165Interface(address account, bytes4 interfaceId) external view returns (bool);

    /**
     * @notice Checks whether a contract implements an ERC165 interface or not without using or updating the cache.
     * @param account Address of the contract to check.
     * @param interfaceId ERC165 interface to check.
     * @return True if `account` implements `interfaceId`, false otherwise.
     */
    function implementsERC165InterfaceNoCache(address account, bytes4 interfaceId) external view returns (bool);
}




/** 
 *  SourceUnit: c:\Users\Administrator\Desktop\example4all\example4solidity\wtf.dapp\_06_ERC777\ERC777.sol
*/
            
////// SPDX-License-Identifier-FLATTEN-SUPPRESS-WARNING:GPL-3.0
pragma solidity ^0.8.17;

library Strings {
    bytes16 private constant _HEX_SYMBOLS = "0123456789abcdef";
    uint8 private constant _ADDRESS_LENGTH = 20;

    /**
     * @dev Converts a `uint256` to its ASCII `string` decimal representation.
     */
    function toString(uint256 value) internal pure returns (string memory) {
        // Inspired by OraclizeAPI's implementation - MIT licence
        // https://github.com/oraclize/ethereum-api/blob/b42146b063c7d6ee1358846c198246239e9360e8/oraclizeAPI_0.4.25.sol

        if (value == 0) {
            return "0";
        }
        uint256 temp = value;
        uint256 digits;
        while (temp != 0) {
            digits++;
            temp /= 10;
        }
        bytes memory buffer = new bytes(digits);
        while (value != 0) {
            digits -= 1;
            buffer[digits] = bytes1(uint8(48 + uint256(value % 10)));
            value /= 10;
        }
        return string(buffer);
    }

    /**
     * @dev Converts a `uint256` to its ASCII `string` hexadecimal representation.
     */
    function toHexString(uint256 value) internal pure returns (string memory) {
        if (value == 0) {
            return "0x00";
        }
        uint256 temp = value;
        uint256 length = 0;
        while (temp != 0) {
            length++;
            temp >>= 8;
        }
        return toHexString(value, length);
    }

    /**
     * @dev Converts a `uint256` to its ASCII `string` hexadecimal representation with fixed length.
     */
    function toHexString(
        uint256 value,
        uint256 length
    ) internal pure returns (string memory) {
        bytes memory buffer = new bytes(2 * length + 2);
        buffer[0] = "0";
        buffer[1] = "x";
        for (uint256 i = 2 * length + 1; i > 1; --i) {
            buffer[i] = _HEX_SYMBOLS[value & 0xf];
            value >>= 4;
        }
        require(value == 0, "Strings: hex length insufficient");
        return string(buffer);
    }

    /**
     * @dev Converts an `address` with fixed length of 20 bytes to its not checksummed ASCII `string` hexadecimal representation.
     */
    function toHexString(address addr) internal pure returns (string memory) {
        return toHexString(uint256(uint160(addr)), _ADDRESS_LENGTH);
    }
}




/** 
 *  SourceUnit: c:\Users\Administrator\Desktop\example4all\example4solidity\wtf.dapp\_06_ERC777\ERC777.sol
*/
            
////// SPDX-License-Identifier-FLATTEN-SUPPRESS-WARNING:GPL-3.0
pragma solidity ^0.8.17;

library Address {
    // 利用extcodesize判断一个地址是否为合约地址
    function isContract(address account) internal view returns (bool) {
        uint size;
        assembly {
            size := extcodesize(account)
        }
        return size > 0;
    }
}




/** 
 *  SourceUnit: c:\Users\Administrator\Desktop\example4all\example4solidity\wtf.dapp\_06_ERC777\ERC777.sol
*/
            
////// SPDX-License-Identifier-FLATTEN-SUPPRESS-WARNING:GPL-3.0
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




/** 
 *  SourceUnit: c:\Users\Administrator\Desktop\example4all\example4solidity\wtf.dapp\_06_ERC777\ERC777.sol
*/
            
////// SPDX-License-Identifier-FLATTEN-SUPPRESS-WARNING:GPL-3.0
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


/** 
 *  SourceUnit: c:\Users\Administrator\Desktop\example4all\example4solidity\wtf.dapp\_06_ERC777\ERC777.sol
*/

////// SPDX-License-Identifier-FLATTEN-SUPPRESS-WARNING:GPL-3.0
pragma solidity ^0.8.17;

////import "./IERC777.sol";
////import "../_01_erc20/IERC20.sol";
////import "../_02_erc721/Address.sol";
////import "../_02_erc721/Strings.sol";
////import "../_07_ERC1820/IERC1820Registry.sol";
////import "./IERC777Sender.sol";
////import "./IERC777Recipient.sol";

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

