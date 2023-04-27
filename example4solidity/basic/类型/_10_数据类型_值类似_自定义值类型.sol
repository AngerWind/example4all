// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;


contract HelloWorld {
    // solidity允许在一个基本的值类型上零成本抽象一个类型
    // 使用  type UserType is DefaultType 来定义
    //      使用 UserType.wrap(defaultTypeValue) 来将一个底层类型转换为自定义类型
    //      使用 UserType.unwrap(userTypeValue) 来讲一个自定义类型转换成底层类型
    // 抽象出来的类型不支持运算符

    type My256 is uint256;

    My256 public m = My256.wrap(1234556);
    uint256 public u = My256.unwrap(m);

    // My256 public m = My256.wrap(1234556) + My256.wrap(1234556) // 报错, 不支持运算符

}