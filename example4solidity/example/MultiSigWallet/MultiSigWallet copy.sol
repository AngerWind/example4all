// SPDX-License-Identifier: MIT
pragma solidity ^0.8.17;

contract MultiSigWallet {
    event Desposit(
        address indexed addr,
        uint256 indexed value,
        uint256 balance
    );
    event SubmitTransaction(
        address indexed owner,
        uint256 indexed txIndex,
        address indexed to,
        uint256 value,
        bytes data
    );
    event ConfirmTransaction(
        address indexed owner,
        uint256 txIndex,
        uint256 confirmNum
    );
    event ExecuteTransaction(
        address indexed to,
        uint256 value,
        bytes data,
        uint256 txIndex,
        uint256 balance
    );
    event RevokeConfirmation(
        address indexed owner,
        uint256 txIndex,
        uint256 confirmNum
    );

    struct Transaction {
        address to;
        uint256 value;
        bytes data;
        bool executed;
        uint256 numConfirmations;
    }

    Transaction[] public transactions;
    mapping(uint256 => mapping(address => bool)) isConfirmed;

    address[] public owners;
    mapping(address => bool) isOwner;

    uint256 public immutable requiredConfirmation;

    constructor(address[] memory _owners, uint256 _requiredConfirmation) {
        require(_owners.length > 0, "at least onw owner ");
        require(
            _requiredConfirmation > 0 &&
                _requiredConfirmation <= _owners.length,
            "invalid required confirm number"
        );
        for (uint256 i = 0; i < _owners.length; i++) {
            require(_owners[i] != address(0), "invalid owner address");
            owners.push(_owners[i]);
            isOwner[_owners[i]] = true;
        }
        requiredConfirmation = _requiredConfirmation;
    }

    receive() external payable {
        emit Desposit(msg.sender, msg.value, address(this).balance);
    }

    modifier onlyOwner() {
        require(isOwner[msg.sender], "only owner");
        _;
    }

    modifier txExsits(uint256 txIndex) {
        require(txIndex < transactions.length, "tx not exsists");
        _;
    }

    modifier notConfirmed(uint256 txIndex) {
        require(
            isConfirmed[txIndex][msg.sender] == false,
            "you have already confired"
        );
        _;
    }

    modifier confirmed(uint256 txIndex) {
        require(
            isConfirmed[txIndex][msg.sender] == true,
            "you are not confired yet"
        );
        _;
    }

    modifier confirmFinished(uint256 txIndex) {
        require(transactions[txIndex].numConfirmations >= requiredConfirmation);
        _;
    }

    function submit(
        address _to,
        uint256 _value,
        bytes calldata _data
    ) public onlyOwner {
        uint256 txIndex = transactions.length;

        transactions.push(
            Transaction({
                to: _to,
                value: _value,
                data: _data,
                executed: false,
                numConfirmations: 0
            })
        );

        emit SubmitTransaction(msg.sender, txIndex, _to, _value, _data);
    }

    function confirm(uint256 txIndex)
        public
        onlyOwner
        txExsits(txIndex)
        notConfirmed(txIndex)
    {
        transactions[txIndex].numConfirmations += 1;
        isConfirmed[txIndex][msg.sender] = true;

        emit ConfirmTransaction(
            msg.sender,
            txIndex,
            transactions[txIndex].numConfirmations
        );
    }

    function revoke(uint256 txIndex)
        public
        onlyOwner
        txExsits(txIndex)
        confirmed(txIndex)
    {
        transactions[txIndex].numConfirmations -= 1;
        isConfirmed[txIndex][msg.sender] = false;

        emit RevokeConfirmation(
            msg.sender,
            txIndex,
            transactions[txIndex].numConfirmations
        );
    }

    function execute(uint256 txIndex)
        public
        onlyOwner
        txExsits(txIndex)
        confirmFinished(txIndex)
    {
        Transaction storage trans = transactions[txIndex];
        (bool success, ) = trans.to.call{value: trans.value}(trans.data);
        require(success, "execute error");

        emit ExecuteTransaction(
            trans.to,
            trans.value,
            trans.data,
            txIndex,
            address(this).balance
        );
    }
}
