pragma solidity >=0.5.0;

import '../../uniswap-v2-core/interfaces/IUniswapV2Pair.sol';
import "./SafeMath.sol";

library UniswapV2Library {
    using SafeMath for uint;

    // returns sorted token addresses, used to handle return values from pairs sorted in this order
    function sortTokens(address tokenA, address tokenB) internal pure returns (address token0, address token1) {
        require(tokenA != tokenB, 'UniswapV2Library: IDENTICAL_ADDRESSES');
        (token0, token1) = tokenA < tokenB ? (tokenA, tokenB) : (tokenB, tokenA);
        require(token0 != address(0), 'UniswapV2Library: ZERO_ADDRESS');
    }

    // calculates the CREATE2 address for a pair without making any external calls
    // 通过tokenA和tokenB的地址, 计算出pair合约的地址
    // todo 如果这一步计算出来的pair实际上还没有创建, 会怎么样?
    function pairFor(address factory, address tokenA, address tokenB) internal pure returns (address pair) {
        (address token0, address token1) = sortTokens(tokenA, tokenB);
        pair = address(uint(keccak256(abi.encodePacked(
                hex'ff',
                factory,
                keccak256(abi.encodePacked(token0, token1)),
                hex'2bbf561059bfad76dd3e7f5e7e45d68e76db16d20d00dc76889202628a6417d9' // init code hash
            ))));
    }

    // fetches and sorts the reserves for a pair
    // 返回tokenA和tokenB的余额
    function getReserves(address factory, address tokenA, address tokenB) internal view returns (uint reserveA, uint reserveB) {
        // 地址小的合约作为pair的left
        (address token0,) = sortTokens(tokenA, tokenB);
        // 获取pair合约地址, 然后调用getReserves方法获取token0和token1的余额
        (uint reserve0, uint reserve1,) = IUniswapV2Pair(pairFor(factory, tokenA, tokenB)).getReserves();
        // 如果token0是tokenA, 则reserve0是tokenA的余额, 否则reserve1是tokenA的余额
        (reserveA, reserveB) = tokenA == token0 ? (reserve0, reserve1) : (reserve1, reserve0);
    }

    // given some amount of an asset and pair reserves, returns an equivalent amount of the other asset
    // 指定tokenA的数量, 按照池子中tokenA和tokenB的比例, 返回能够兑换的tokenB的数量
    function quote(uint amountA, uint reserveA, uint reserveB) internal pure returns (uint amountB) {
        require(amountA > 0, 'UniswapV2Library: INSUFFICIENT_AMOUNT');
        require(reserveA > 0 && reserveB > 0, 'UniswapV2Library: INSUFFICIENT_LIQUIDITY');
        amountB = amountA.mul(reserveB) / reserveA;
    }

    // given an input amount of an asset and pair reserves, returns the maximum output amount of the other asset
    // 指定tokenA的数量, 按照池子中tokenA和tokenB的比例, 返回tokenB的最大数量, 其中会收取0.3%的手续费
    function getAmountOut(uint amountIn, uint reserveIn, uint reserveOut) internal pure returns (uint amountOut) {
        require(amountIn > 0, 'UniswapV2Library: INSUFFICIENT_INPUT_AMOUNT');
        require(reserveIn > 0 && reserveOut > 0, 'UniswapV2Library: INSUFFICIENT_LIQUIDITY');
        // 收取0.3%的手续费
        uint amountInWithFee = amountIn.mul(997);
        uint numerator = amountInWithFee.mul(reserveOut);
        uint denominator = reserveIn.mul(1000).add(amountInWithFee);
        // 计算tokenB的数量, 这个计算公式需要看文档
        amountOut = numerator / denominator;
    }

    // given an output amount of an asset and pair reserves, returns a required input amount of the other asset
    function getAmountIn(uint amountOut, uint reserveIn, uint reserveOut) internal pure returns (uint amountIn) {
        require(amountOut > 0, 'UniswapV2Library: INSUFFICIENT_OUTPUT_AMOUNT');
        require(reserveIn > 0 && reserveOut > 0, 'UniswapV2Library: INSUFFICIENT_LIQUIDITY');
        // 已知tokenA, B的数量分别是x,y, 输出dy的tokenB, 计算输入dx的tokenA, 公式为dx = dy * x / (y-dy)
        // 换算到这里就是 amountIn = amountOut * reserveIn / (reserveOut - amountOut)
        // 但是amountIn中的0.3%需要交手续费, 只有99.7%的amountIn能够交换出来amountOut
        // 所以实际上是amountIn * 99.7% = amountOut * reserveIn / (reserveOut - amountOut)
        uint numerator = reserveIn.mul(amountOut).mul(1000);
        uint denominator = reserveOut.sub(amountOut).mul(997);
        // 因为这里是出发, 会抹去小数点, 这样的话导致amountIn比实际的小, 从而导致K变小, 所以需要加1
        // 从而保证K只会不变会在变大
        amountIn = (numerator / denominator).add(1);
    }

    // performs chained getAmountOut calculations on any number of pairs
    // 返回一个数组, 数组中的元素表示能够交换出来的path对应的tokenB的数量
    // 比如使用x数量的DAI交换WETH, 路径为[DAI, USDT, WETH],
    //      则返回的数组[x, y, z]分别表示x数量的DAI, y表示x DAI能够交换出来的USDT, z表示y DAI能够交换出来的WETH
    function getAmountsOut(address factory, uint amountIn, address[] memory path) internal view returns (uint[] memory amounts) {
        require(path.length >= 2, 'UniswapV2Library: INVALID_PATH');
        amounts = new uint[](path.length);
        // amounts[0]表示输入的token的数量, 其他的表示输出的token的数量
        amounts[0] = amountIn;
        for (uint i; i < path.length - 1; i++) {
            // 返回指定路径上的交易对的余额
            (uint reserveIn, uint reserveOut) = getReserves(factory, path[i], path[i + 1]);
            // 计算这一步能够交换出多少token
            amounts[i + 1] = getAmountOut(amounts[i], reserveIn, reserveOut);
        }
    }

    // performs chained getAmountIn calculations on any number of pairs
    // 计算每个路径上需要输入的token的数量
    function getAmountsIn(address factory, uint amountOut, address[] memory path) internal view returns (uint[] memory amounts) {
        require(path.length >= 2, 'UniswapV2Library: INVALID_PATH');
        amounts = new uint[](path.length);
        amounts[amounts.length - 1] = amountOut;
        for (uint i = path.length - 1; i > 0; i--) {
            (uint reserveIn, uint reserveOut) = getReserves(factory, path[i - 1], path[i]);
            amounts[i - 1] = getAmountIn(amounts[i], reserveIn, reserveOut);
        }
    }
}
