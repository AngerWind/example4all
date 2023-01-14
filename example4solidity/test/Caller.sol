// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

import {A} from "./A.sol";

contract Existing {
    A dc;

    constructor(address t) {
        dc = A(t);
    }

    function getA() public view returns (uint256 result) {
        return dc.a();
    }

    function setA(uint256 _val) public returns (uint256 result) {
        dc.setA(_val);
        return _val;
    }
}

contract ExistingWithoutABI {
    address dc;

    constructor(address _t) {
        dc = _t;
    }

    function setASignature(uint256 val) public returns (bool) {
        (bool success, bytes memory returnData) = dc.call(abi.encodeWithSignature("setA(uint256)", val));
        return success;
    }
}
