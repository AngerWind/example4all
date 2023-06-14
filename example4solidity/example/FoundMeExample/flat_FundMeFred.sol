
/** 
 *  SourceUnit: c:\Users\Administrator\Desktop\example4all\example4solidity\example\FoundMeExample\FundMeFred.sol
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


/** 
 *  SourceUnit: c:\Users\Administrator\Desktop\example4all\example4solidity\example\FoundMeExample\FundMeFred.sol
*/

////// SPDX-License-Identifier-FLATTEN-SUPPRESS-WARNING:GPL-3.0

pragma solidity ^0.8.17;

////import "./ETHToUSD.sol";

// 最小捐献金额
uint256 constant MINIMUM_USD = 100;

contract FundMeFred {
    // 将库附加到uint上
    using ETHToUSD for uint256;

    address private immutable owner;
    // 保存所有地址和对应的金额
    mapping(address => uint256) private addressToAmount;
    // 保存所有捐献的地址
    address[] private funders;

    constructor() {
        owner = msg.sender;
    }

    // 接收存款的接口
    function fund() public payable {
        // 必须要捐款大于100U
        require(msg.value.etherToUSD() > MINIMUM_USD, "must greater than 100U");
        funders.push(msg.sender);
        addressToAmount[msg.sender] = msg.value;
    }

    // 提取eth
    function withdraw() public onlyOwner {
        // 提取eth的金额
        // send, transfer消耗固定2300gas, 而call可以修改gas, send
        // send, transfer, call返回值不同
        // bool isSuccess = payable(msg.sender).send(address(this).balance);
        // payable(msg.sender).transfer(address(this).balance);
        (bool success, ) = payable(msg.sender).call{
            value: address(this).balance
        }("");
        require(success, "fail withdraw");
        // 清除所有的余额
        for (uint256 i = 0; i < funders.length; i++) {
            delete addressToAmount[funders[i]];
        }
        delete funders;
    }

    modifier onlyOwner() {
        require(msg.sender == owner, "only owner enable to call this function");
        _;
    }

    function getBalance() public view returns(uint){
        return address(this).balance;
    }

    function getETHPrice() public view returns (uint) {
        return uint(1).etherToUSD();
    }
}

