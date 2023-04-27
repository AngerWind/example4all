// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

/**
 *
 * https://learnblockchain.cn/2019/06/10/address-compute/
 * https://www.wtf.academy/solidity-advanced/Create2/
 *
 * 在solidity中有两种创建合约的方式
 *      1. 这种是最开始的创建方式, 通过CREATE操作码来创建合约, 通过创建者地址和nonce来计算
 *          对于账户, nonce是发送过的交易数, 从0开始
 *          对于合约, nonce是该合约创建过的合约数, 根据eip161规范, 合约账户的nonce从1开
 *          先通过RLP对sender和nonce进行编码, 然后使用keccak-256进行hash计算
 *          因为在solidity中无法获取nonce, 所以也就无法预知要创建的合约的地址
 *
 *          def mk_contract_address(sender, nonce):
 *              return sha3(rlp.encode([normalize_address(sender), nonce]))[12:]
 *
 *      2. 在eip1014中新添加了操作码CREATE2, 他是创建合约的另一种方式
 *          对于由CREATE2创建的合约, 地址是
 *              keccak256(0xff,sender,salt,keccak256(create_code))[12:]
 *          也就是说给定合约和创建者, 是能够提前预知需要创建的合约的地址的
 *          需要使用CREATE2操作码来创建合约, 使用如下方式
 *              Contract x = new Contract{salt: _salt, value: _value}(params)
 *
 *          他的实际场景引用主要在:
 *              1. 交易所为新用户预留创建钱包合约地址
 *                  https://benpaodewoniu.github.io/2022/01/15/solidity29/
 *              2. 对于给定的参数, 能够计算出与用户相关的合约的地址, 不需要通过mapping来查询
 *
 */
contract Pair {
    address public factory; // 工厂合约地址
    address public token0; // 代币1
    address public token1; // 代币2

    constructor() payable {
        factory = msg.sender;
    }

    // called once by the factory at time of deployment
    function initialize(address _token0, address _token1) external {
        require(msg.sender == factory, "UniswapV2: FORBIDDEN"); // sufficient check
        token0 = _token0;
        token1 = _token1;
    }
}

contract PairFactory2 {
    mapping(address => mapping(address => address)) public getPair; // 通过两个代币地址查Pair地址
    address[] public allPairs; // 保存所有Pair地址

    function createPair2(address tokenA, address tokenB) external returns (address pairAddr) {
        require(tokenA != tokenB, "IDENTICAL_ADDRESSES"); //避免tokenA和tokenB相同产生的冲突
        // 计算用tokenA和tokenB地址计算salt
        (address token0, address token1) = tokenA < tokenB ? (tokenA, tokenB) : (tokenB, tokenA); //将tokenA和tokenB按大小排序
        bytes32 salt = keccak256(abi.encodePacked(token0, token1));
        // 用create2部署新合约
        Pair pair = new Pair{salt: salt}();
        // 调用新合约的initialize方法
        pair.initialize(tokenA, tokenB);
        // 更新地址map
        pairAddr = address(pair);
        allPairs.push(pairAddr);
        getPair[tokenA][tokenB] = pairAddr;
        getPair[tokenB][tokenA] = pairAddr;
    }

    // 提前计算pair合约地址
    function calculateAddr(address tokenA, address tokenB) public view returns (address predictedAddress) {
        require(tokenA != tokenB, "IDENTICAL_ADDRESSES"); //避免tokenA和tokenB相同产生的冲突
        // 计算用tokenA和tokenB地址计算salt
        (address token0, address token1) = tokenA < tokenB ? (tokenA, tokenB) : (tokenB, tokenA); //将tokenA和tokenB按大小排序
        bytes32 salt = keccak256(abi.encodePacked(token0, token1));
        // 计算合约地址方法 hash()
        predictedAddress = address(
            uint160(
                uint(keccak256(abi.encodePacked(bytes1(0xff), address(this), salt, keccak256(type(Pair).creationCode))))
            )
        );
    }
}
