
/** 
 *  SourceUnit: c:\Users\Administrator\Desktop\example4all\example4solidity\basic\合约\_32_错误与异常处理_try-catch.sol
*/

////// SPDX-License-Identifier-FLATTEN-SUPPRESS-WARNING: GPL-3.0
pragma solidity >=0.8.1;

/**
    - 外部调用的失败，可以通过 try/catch 语句来捕获
    - try 关键词后面必须有一个表达式，代表external函数调用或合约创建（ new ContractName()）。
    - 接下来的 returns 部分（是可选的）声明了与外部调用返回的类型相匹配的返回变量。 

    
    Solidity 根据错误的类型，支持不同种类的捕获代码块：
        catch Error(string memory reason) { ... }: 捕获由 revert("reasonString") 或 require(false, "reasonString") 引起的错误
        catch Panic(uint errorCode) { ... }: 如果错误是由 panic 引起的错误
        catch (bytes memory lowLevelData) { ... }: 如果错误签名不符合任何其他子句，如果在解码错误信息时出现了错误，或者如果异常没有一起提供错误数据。在这种情况下，子句声明的变量提供了对低级错误数据的访问。
        catch { ... }: 捕获所有错误

 */
interface DataFeed {
    function getData(address token) external returns (uint value);
}

contract FeedConsumer {
    DataFeed feed;

    function rate(address token) public returns (uint value, bool success) {
        try feed.getData(token) returns (uint v) {
            v += 1;
            return (v, true);
        } catch Error(string memory reason) {
            // 抛出Error时执行, 通过reason来判断情况
            return (0, false);
        } catch Panic(uint errorCode) {
            // 抛出Panic时执行, 通过code来判断是什么类型的panic
            return (0, false);
        } catch (bytes memory lowLevelData) {
            // This is executed in case revert() was used。
            return (0, false);
        }
    }
}

