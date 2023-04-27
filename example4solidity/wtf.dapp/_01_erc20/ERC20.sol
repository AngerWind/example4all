// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;
import "./IERC20.sol";

contract ERC20 is IERC20 {
    // Token信息, 可选
    string public name; // 全称
    string public symbol; // 代号
    uint8 public immutable decimals; // token的精度, 大部分都是18

    uint public override totalSupply; // 代币总供给, 初始为0, 任何人都可以mint代币

    mapping(address => uint) public override balanceOf; // 地址对应余额
    mapping(address => mapping(address => uint)) public override allowance; // 授权

    constructor(string memory _name, string memory _symbol, uint8 _decinals) {
        name = _name;
        symbol = _symbol;
        decimals = _decinals;
    }

    function transfer(address _to, uint _value) public virtual override returns (bool success) {
        require(balanceOf[msg.sender] >= _value, "not enough tokens");
        balanceOf[msg.sender] -= _value;
        balanceOf[_to] += _value;
        emit Transfer(msg.sender, _to, _value);
        success = true;
    }

    function approve(address _spender, uint _value) public virtual override returns (bool success) {
        allowance[msg.sender][_spender] = _value;
        emit Approval(msg.sender, _spender, _value);
        success = true;
    }

    function transferFrom(address _from, address _to, uint _value) public virtual override returns (bool success) {
        require(msg.sender == _from || msg.sender == _to, "invalid transfer"); // 只有授权双方可以调用这个函数
        require(allowance[_from][_to] > _value, "not enough token allowance"); // 没有足够授权
        allowance[_from][_to] -= _value;
        balanceOf[_from] -= _value;
        balanceOf[_to] += _value;
        emit Transfer(_from, _to, _value);
        success = true;
    }

    /**
     * 不在erc20中, 定义了任何人都可以mint代币
     */
    function mint(uint amount) external {
        balanceOf[msg.sender] += amount;
        totalSupply += amount;
        emit Transfer(address(0), msg.sender, amount);
    }

    /**
     * 不在erc20中, 定义了任何人都可以销毁自己的代币
     */
    function burn(uint amount) external {
        balanceOf[msg.sender] -= amount;
        totalSupply -= amount;
        emit Transfer(msg.sender, address(0), amount);
    }
}
