// SPDX-License-Identifier:GPL-3.0
pragma solidity ^0.8.17;

// vrf协调器的接口
import "@chainlink/contracts/src/v0.8/interfaces/VRFCoordinatorV2Interface.sol";
// vrf协调器执行回调的接口
import "@chainlink/contracts/src/v0.8/VRFConsumerBaseV2.sol";

contract VRFv2Consumer is VRFConsumerBaseV2 { // 实现接口
    event RequestSent(uint256 requestId, uint32 numWords);
    event RequestFulfilled(uint256 requestId, uint256[] randomWords);

    struct RequestStatus { // 用以保存请求的执行状态
        bool fulfilled; // 请求是否已经完成
        bool exists; // 请求ID是否存在
        uint256[] randomWords; // 保存生成的随机数
    }
    mapping(uint256 => RequestStatus) public s_requests; // 请求ID => 请求状态
    VRFCoordinatorV2Interface COORDINATOR; // VRF协调器

    uint64 s_subscriptionId; // 订阅ID

    uint256[] public requestIds; // 记录所有requestId

    // The gas lane to use, which specifies the maximum gas price to bump to.
    // For a list of available gas lanes on each network,
    // see https://docs.chain.link/docs/vrf/v2/subscription/supported-networks/#configurations
    bytes32 keyHash = 0x474e34a077df58807dbe9c96d3c009b23b3c6d0cce433e59bbf5b34f823bc56c;

    // Depends on the number of requested values that you want sent to the
    // fulfillRandomWords() function. Storing each word costs about 20,000 gas,
    // so 100,000 is a safe default for this example contract. Test and adjust
    // this limit based on the network that you select, the size of the request,
    // and the processing of the callback request in the fulfillRandomWords()
    // function.
    uint32 callbackGasLimit = 100000;

    // The default is 3, but you can set this higher.
    uint16 requestConfirmations = 3;

    // For this example, retrieve 2 random values in one request.
    // Cannot exceed VRFCoordinatorV2.MAX_NUM_WORDS.
    uint32 numWords = 2;

    address private owner;
    modifier onlyOwner() {
        require(msg.sender == owner, "Not owner");
        _;
    }

    /**
     * COORDINATOR: 0x8103B0A8A00be2DDC778e6e7eaa21791Cd364625  for sepolia
     */
    constructor(uint64 subscriptionId, address coordinatorAddr) VRFConsumerBaseV2(coordinatorAddr) {
        owner = msg.sender;
        COORDINATOR = VRFCoordinatorV2Interface(coordinatorAddr); // vrf协调器的地址
        s_subscriptionId = subscriptionId; // 订阅的id
    }

    // 该方法用以请求随机数
    function requestRandomWords() external onlyOwner returns (uint256 requestId) {
        // 请求随机数, 如果订阅ID不正确, 或者订阅中没有足够的LINK会报错
        requestId = COORDINATOR.requestRandomWords(
            keyHash, // 生成随机数的链下的vrf任务的id, 同时也定义了愿意支付的最大gas price
            s_subscriptionId, // 订阅id
            requestConfirmations, // 在进行回调之前chainlink应该等待多少个confirmations
            callbackGasLimit, // 用于回调的gas limit, 如果fulfillRandomWords函数很复杂, 就需要多一些gas
            numWords // 要生成的随机数的个数, 可以在一次请求中生成多个随机数
        );
        s_requests[requestId] = RequestStatus({randomWords: new uint256[](0), exists: true, fulfilled: false});
        requestIds.push(requestId);
        emit RequestSent(requestId, numWords);
        return requestId;
    }

    /**
     * 该方法将由链上的VRF协调器进行回调
     * 继承VRFConsumerBaseV2的方法
     * 用以保存生成的随机数
     */
    function fulfillRandomWords(uint256 _requestId, uint256[] memory _randomWords) internal override {
        require(s_requests[_requestId].exists, "request not found");
        s_requests[_requestId].fulfilled = true;
        s_requests[_requestId].randomWords = _randomWords;
        emit RequestFulfilled(_requestId, _randomWords);
    }

    /**
     * 根据请求id获取请求状态
     */
    function getRequestStatus(uint256 _requestId) external view returns (bool fulfilled, uint256[] memory randomWords) {
        require(s_requests[_requestId].exists, "request not found");
        RequestStatus memory request = s_requests[_requestId];
        return (request.fulfilled, request.randomWords);
    }
}