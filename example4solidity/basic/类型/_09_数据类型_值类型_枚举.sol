// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

contract HelloWorld {

    /**
     * 枚举
     *      - 至少需要一个成员, 至多256个成员
     *      - 零值是第一个成员
     *      - 编译的时候会将枚举编译成的uint8类似
     *      - 索引从0开始
     *      - 使用 type(NameOfEnum).min 和 type(NameOfEnum).max得到给定枚举的最小值和最大值
     */
    enum Status {
        None,
        Pending,
        Rejected,
        Canceled, 
        Shiped, 
        Completed
    }

    Status public status; // Status.None
    Status public status1 = Status.Rejected;

    Status public maxStatus = type(Status).max; // Status.Completed
    Status public minStatus = type(Status).min; // Status.None

    // 由于枚举类型不属于 |ABI| 的一部分，因此对于所有来自 Solidity 外部的调用，  
    // getStatus()  会被转换为  function getStatus()  returns (uint8)
    // 所以传参的时候可以使用uint8传给枚举, 返回枚举也会变成unit8
    function getStatus() public view returns (Status) {
        return status;
    }

    // 这里可以给s传递一个uint8类型的2
    function setStatus(Status s) external {
        status = s;
    }
}
