
/** 
 *  SourceUnit: c:\Users\Administrator\Desktop\example4all\example4solidity\advanced\assembly\_01_basic.sol
*/

////// SPDX-License-Identifier-FLATTEN-SUPPRESS-WARNING:GPL-3.0
pragma solidity ^0.8.17;

contract Tester {
    function test() public {
        uint var1 = 3;

        assembly {
            // 只有uint256一种类型, 并且只能是十六进制数, 十进制数, ascii字符串
            let a := 1 // 赋值使用 :=
            let d := hex"923892" // 十六进制
            let b  // 自动初始化为0
            let s := "aaaaaaaaaa_aaaaaaaaaa_aaaaaaaaaa" // 字符串字面量最多包含32个字符, 否则报错
            let c := add(1, var1) // 可以直接使用函数中的变量
        }

        assembly {
            let a := 2 // 内联汇编, 不同的代码块不共享名称空间
        }

        assembly {
            // for循环
            let n := 10
            let result := 0
            for {
                let i := 0 // 定义初始变量
            } lt(i, n) {
                // 定义执行条件, 必须是函数风格表达式
                i := add(i, 1) // 定义迭代步骤
            } {
                result := add(result, i) // 循环体
            }
            // for ( uint i = 0; i < n; i++ ) {
            //     value += i;
            // }
        }

        assembly {
            // 在solidity汇编中实际上是没有while循环的, 但是可以用for循环模拟while循环
            let i
            let result
            for {

            } lt(i, 100) {

            } {
                // 等价 while(i < 0x100)
                result := add(result, i)
                i := add(i, 1)
            }
        }

        assembly {
            // 内联汇编中的if没有else部分, 并且代码块大括号不能省略
            let i
            if eq(i, 0) {
                // 这里的条件也是只能使用函数风格表达式
                i := 1
            }
        }

        assembly {
            // 如果需要检查多种条件, 考虑switch
            // switch 将一个表达式的值对多个!!常量!!进行比较, 来选择分支
            // 所有分支必须具有相同的类型和不同的值
            // case语句会自动break
            let x := 0
            switch calldataload(4)
            case 0 {
                x := calldataload(0x24)
            }
            default {
                x := calldataload(0x44)
            }
        }
    }

    function test2() public {
        assembly {
            /**
             * 定义函数
             * 参数不需要类型
             * 不需要可见性(public,private), 因为函数仅在汇编代码块内有效
             * 返回值使用 ->, 不需要显示return, 只需要给返回值赋值即可
             * 函数无法访问函数之外声明的变量
             *
             * leave关键字可以放在函数的任意位置, 以停止其执行并退出 (return当前函数)
             */
            function func(param1, param2) -> result, result2 {
                result2 := 2
                result := 1
                leave
            }

            let c, d := func(1, 2) // 接收函数返回值
        }
    }
}

