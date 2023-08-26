pragma solidity =0.5.16;

import './interfaces/IUniswapV2Factory.sol';
import './UniswapV2Pair.sol';

contract UniswapV2Factory is IUniswapV2Factory {
    // 官方接收手续费的地址, 如果该地址不为0地址, 则每笔交易官方都会收取手续费
    address public feeTo;
    // 谁可以设置feeTo地址
    address public feeToSetter;

    // 记录tokenA和tokenB的交易对地址, 同时也记录tokenB对tokenA的交易对地址
    mapping(address => mapping(address => address)) public getPair;
    // 所有交易对地址
    address[] public allPairs;

    // 创建交易对事件
    event PairCreated(address indexed token0, address indexed token1, address pair, uint);

    constructor(address _feeToSetter) public {
        feeToSetter = _feeToSetter;
    }

    function allPairsLength() external view returns (uint) {
        return allPairs.length;
    }

    // 创建交易对
    function createPair(address tokenA, address tokenB) external returns (address pair) {
        require(tokenA != tokenB, 'UniswapV2: IDENTICAL_ADDRESSES');
        // 交易对地址按照字典顺序排列
        (address token0, address token1) = tokenA < tokenB ? (tokenA, tokenB) : (tokenB, tokenA);
        // 检测tokenA不是0地址, 如果tokenA不是0地址, 因为addressA<addressB, 所以只要A不是0地址, B也不是0地址
        require(token0 != address(0), 'UniswapV2: ZERO_ADDRESS');
        // 检测交易对不存在
        require(getPair[token0][token1] == address(0), 'UniswapV2: PAIR_EXISTS'); // single check is sufficient
        // 获取Pair合约的creationCode
        bytes memory bytecode = type(UniswapV2Pair).creationCode;
        // 通过create2创建合约, 传入的参数为0, 代码为bytecode, 长度为bytecode的长度, salt为keccak256(token0, token1)
        bytes32 salt = keccak256(abi.encodePacked(token0, token1));
        assembly {
            pair := create2(0, add(bytecode, 32), mload(bytecode), salt)
        }
        // 初始化交易对, 并传入token0和token1的地址
        IUniswapV2Pair(pair).initialize(token0, token1);
        getPair[token0][token1] = pair;
        getPair[token1][token0] = pair; // populate mapping in the reverse direction
        allPairs.push(pair);
        emit PairCreated(token0, token1, pair, allPairs.length);
    }

    function setFeeTo(address _feeTo) external {
        require(msg.sender == feeToSetter, 'UniswapV2: FORBIDDEN');
        feeTo = _feeTo;
    }

    function setFeeToSetter(address _feeToSetter) external {
        require(msg.sender == feeToSetter, 'UniswapV2: FORBIDDEN');
        feeToSetter = _feeToSetter;
    }
}
