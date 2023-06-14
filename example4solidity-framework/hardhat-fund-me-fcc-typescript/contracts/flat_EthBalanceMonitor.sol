
/** 
 *  SourceUnit: c:\Users\Administrator\Desktop\example4all\example4solidity\hardhat-fcc\hardhat-fund-me-fcc-typescript\contracts\EthBalanceMonitor.sol
*/

////// SPDX-License-Identifier-FLATTEN-SUPPRESS-WARNING: MIT

pragma solidity ^0.8.7;

// 该方法必须实现performUpkeep和checkUpkeep方法, performUpkeep执行回调, checkUpkeep检测是否执行performUpkeep
////import "@chainlink/contracts/src/v0.8/interfaces/AutomationCompatibleInterface.sol";
// 该合约提供了whenPaused和whenNotPaused修饰符，用于确保方法只能在暂停或者非暂停时执行, 并且该合约必须实现两个外部方法来进行暂停和恢复
////import "@openzeppelin/contracts/security/Pausable.sol";

contract EthBalanceMonitor is Pausable, AutomationCompatibleInterface {
    // observed limit of 45K + 10k buffer
    uint256 public constant MIN_GAS_FOR_TRANSFER = 55_000;

    event TopUpSucceeded(address indexed recipient);
    event TopUpFailed(address indexed recipient);

    error InvalidWatchList();
    error OnlyKeeperRegistry();
    error DuplicateAddress(address duplicate);

    struct Target {
        bool isActive; // 是否生效
        uint96 minBalanceWei; // 最小的余额, 低于余额就给他发送topUpAmountWei金额
        uint96 topUpAmountWei; // 发送的金额
        uint56 lastTopUpTimestamp; // 上一次检查的时间戳
    }

    address public s_keeperRegistryAddress; // keeperRegistry的地址
    uint256 public s_minWaitPeriodSeconds; // 最小的间隔
    address[] public s_watchList; // 记录所有监控的地址
    mapping(address => Target) public s_targets; // 记录所有监控地址的信息

    address owner;
    modifier onlyOwner() {
        if (msg.sender == owner) {
            _;
        }
    }

    constructor(address keeperRegistryAddress, uint256 minWaitPeriodSeconds) {
        owner = msg.sender;
        require(keeperRegistryAddress != address(0));
        s_keeperRegistryAddress = keeperRegistryAddress; // 设置keeperRegistryAddress
        s_minWaitPeriodSeconds = minWaitPeriodSeconds; // 设置最小等待时间
    }

    /**
     * 清除上一次的watchlist, 并添加新的watchlist
     */
    function setWatchList(
        address[] calldata addresses,
        uint96[] calldata minBalancesWei,
        uint96[] calldata topUpAmountsWei
    ) external onlyOwner {
        if (addresses.length != minBalancesWei.length || addresses.length != topUpAmountsWei.length) {
            revert InvalidWatchList();
        }
        address[] memory oldWatchList = s_watchList;
        for (uint256 idx = 0; idx < oldWatchList.length; idx++) {
            s_targets[oldWatchList[idx]].isActive = false; // 清除以前的watchlist
        }
        for (uint256 idx = 0; idx < addresses.length; idx++) {
            if (s_targets[addresses[idx]].isActive) {
                revert DuplicateAddress(addresses[idx]);
            }
            if (addresses[idx] == address(0)) {
                revert InvalidWatchList();
            }
            if (topUpAmountsWei[idx] == 0) {
                revert InvalidWatchList();
            }
            // 添加新的watchlist
            s_targets[addresses[idx]] = Target({
                isActive: true,
                minBalanceWei: minBalancesWei[idx],
                topUpAmountWei: topUpAmountsWei[idx],
                lastTopUpTimestamp: 0
            });
        }
        s_watchList = addresses;
    }
    // 获取需要充值的地址
    function getUnderfundedAddresses() public view returns (address[] memory) {
        address[] memory watchList = s_watchList;
        address[] memory needsFunding = new address[](watchList.length);
        uint256 count = 0;
        uint256 minWaitPeriod = s_minWaitPeriodSeconds;
        uint256 balance = address(this).balance;
        Target memory target;
        for (uint256 idx = 0; idx < watchList.length; idx++) {
            target = s_targets[watchList[idx]];
            if (
                // 上次检查的时间戳 + 最小等待时间 <= 当前时间戳
                // 当前余额 >= topUpAmountWei
                // 监控账户余额 < minBalanceWei
                target.lastTopUpTimestamp + minWaitPeriod <= block.timestamp &&
                balance >= target.topUpAmountWei &&
                watchList[idx].balance < target.minBalanceWei 
            ) {
                needsFunding[count] = watchList[idx]; // 记录需要充值的地址
                count++;
                balance -= target.topUpAmountWei;
            }
        }
        if (count != watchList.length) {
            assembly {
                mstore(needsFunding, count) // 直接修改needsFunding的长度
            }
        }
        return needsFunding;
    }
    // 充值
    function topUp(address[] memory needsFunding) public whenNotPaused {
        uint256 minWaitPeriodSeconds = s_minWaitPeriodSeconds;
        Target memory target;
        for (uint256 idx = 0; idx < needsFunding.length; idx++) {
            target = s_targets[needsFunding[idx]];
            if (
                // 需要重新验证参数
                target.isActive &&
                target.lastTopUpTimestamp + minWaitPeriodSeconds <= block.timestamp &&
                needsFunding[idx].balance < target.minBalanceWei
            ) {
                // 直接发送topUpAmountWei金额
                bool success = payable(needsFunding[idx]).send(target.topUpAmountWei);
                if (success) {
                    s_targets[needsFunding[idx]].lastTopUpTimestamp = uint56(block.timestamp);
                    emit TopUpSucceeded(needsFunding[idx]);
                } else {
                    emit TopUpFailed(needsFunding[idx]);
                }
            }
            if (gasleft() < MIN_GAS_FOR_TRANSFER) {
                return;
            }
        }
    }
    function checkUpkeep(
        bytes calldata
    ) external view override whenNotPaused returns (bool upkeepNeeded, bytes memory performData) {
        // 获取需要充值的地址
        address[] memory needsFunding = getUnderfundedAddresses();
        upkeepNeeded = needsFunding.length > 0;
        performData = abi.encode(needsFunding);
        return (upkeepNeeded, performData);
    }
    function performUpkeep(bytes calldata performData) external override onlyKeeperRegistry whenNotPaused {
        address[] memory needsFunding = abi.decode(performData, (address[]));
        // 进行充值
        topUp(needsFunding);
    }

    function withdraw(uint256 amount, address payable payee) external onlyOwner {
        require(payee != address(0));
        payee.transfer(amount);
    }
    receive() external payable {}
    
    function pause() external onlyOwner {
        _pause();
    }

    /**
     * @notice Unpauses the contract
     */
    function unpause() external onlyOwner {
        _unpause();
    }

    modifier onlyKeeperRegistry() {
        if (msg.sender != s_keeperRegistryAddress) {
            revert OnlyKeeperRegistry();
        }
        _;
    }
}

