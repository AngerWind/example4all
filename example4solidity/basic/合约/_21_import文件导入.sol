// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

/**
 *  import "filename";  从filename中导入所有的全局符号到当前作用域中, 会污染当前命名空间
    import "filename" as symbolName; 之后就可以使用 symbolName.  来使用filename中的全局符号了
    import {symbol1 as alias, symbol2} from "filename"; 之后可以使用alias和symbol2

    filename可以是相对路径, github网站, npm包中的合约
 */

import "./_21_import-lib.sol"; // 直接使用Data来引用lib中的Data
import "./_21_import-lib.sol" as lib; // 需要通过lib.Data来引用lib中的Data
import {Data as MyData, Set} from "./_21_import-lib.sol"; // 需要通过MyData来引用Data

// 通过网址来导入
import "https://github.com/OpenZeppelin/openzeppelin-contracts/blob/master/contracts/utils/Address.sol";
// 通过npm来导入
import "@openzeppelin/contracts/access/Ownable.sol";

contract Test {
    Data data0;
    lib.Data data;
    MyData data1;
}
