
/** 
 *  SourceUnit: c:\Users\Administrator\Desktop\example4all\example4solidity\example\FoundMeExample\ETHToUSD.sol
*/

////// SPDX-License-Identifier-FLATTEN-SUPPRESS-WARNING:GPL-3.0

pragma solidity ^0.8.17;

////import "github.com/smartcontractkit/chainlink/blob/develop/contracts/src/v0.8/interfaces/AggregatorV3Interface.sol";

address constant ETH_TO_USD_ADDRESS = 0xD4a33860578De61DBAbDc8BFdb98FD742fA7028e;

library ETHToUSD {
    // 通过预言机获取最新的eth/usd的价格
    function getLastedPrice() public view returns (uint256) {
        // 通过地址创建预言机的合约
        AggregatorV3Interface priceFeed = AggregatorV3Interface(
            ETH_TO_USD_ADDRESS
        );
        (, int256 price, , , ) = priceFeed.latestRoundData();
        return uint256(price);
    }

    // 将指定个数的eth转换为usd的价格
    function etherToUSD(uint256 amount) public view returns (uint256) {
        uint256 price = getLastedPrice();
        uint256 ethInUSD = (price * amount) / 1e26;
        return ethInUSD;
    }
}

