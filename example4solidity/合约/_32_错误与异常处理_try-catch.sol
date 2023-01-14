// SPDX-License-Identifier: GPL-3.0
pragma solidity >=0.8.1;

/**
    - 外部调用的失败，可以通过 try/catch 语句来捕获
    - try 关键词后面必须有一个表达式，代表外部函数调用或合约创建（ new ContractName()）。
    - 在表达式上的错误不会被捕获（例如，如果它是一个复杂的表达式，还涉及内部函数调用），只有外部调用本身发生的revert 可以捕获。 
    - 接下来的 returns 部分（是可选的）声明了与外部调用返回的类型相匹配的返回变量。 在没有错误的情况下，这些变量被赋值，合约将继续执行第一个成功块内代码。
    
    Solidity 根据错误的类型，支持不同种类的捕获代码块：
        catch Error(string memory reason) { ... }: 如果错误是由 revert("reasonString") 或 require(false, "reasonString") （或导致这种异常的内部错误）引起的，则执行这个catch子句。
        catch Panic(uint errorCode) { ... }: 如果错误是由 panic 引起的（如： assert 失败，除以0，无效的数组访问，算术溢出等），将执行这个catch子句。
        catch (bytes memory lowLevelData) { ... }: 如果错误签名不符合任何其他子句，如果在解码错误信息时出现了错误，或者如果异常没有一起提供错误数据。在这种情况下，子句声明的变量提供了对低级错误数据的访问。
        catch { ... }: 如果你对错误数据不感兴趣，你可以直接使用 catch { ... } (甚至是作为唯一的catch子句) 而不是前面几个catch子句。
 */
interface DataFeed { function getData(address token) external returns (uint value); }

contract FeedConsumer {
    DataFeed feed;
    uint errorCount;
    function rate(address token) public returns (uint value, bool success) {
        // 如果错误超过 10 次，永久关闭这个机制
        require(errorCount < 10);
        try feed.getData(token) returns (uint v) {
            return (v, true);
        } catch Error(string memory /*reason*/) {
            // This is executed in case
            // revert was called inside getData
            // and a reason string was provided.
            errorCount++;
            return (0, false);
        }  catch Panic(uint /*errorCode*/) {
            // This is executed in case of a panic,
            // i.e. a serious error like division by zero
            // or overflow. The error code can be used
            // to determine the kind of error.
            errorCount++;
            return (0, false);
        } catch (bytes memory /*lowLevelData*/) {
            // This is executed in case revert() was used。
            errorCount++;
            return (0, false);
        }
    }
}
