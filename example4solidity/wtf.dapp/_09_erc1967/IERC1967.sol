// SPDX-License-Identifier: MIT

pragma solidity ^0.8.0;

/**
 * 定义了三个事件
 */
interface IERC1967 {
    // logic改变时触发
    event Upgraded(address indexed implementation);

    // admin改变时触发
    event AdminChanged(address previousAdmin, address newAdmin);

    // beacon改变时触发
    event BeaconUpgraded(address indexed beacon);
}
