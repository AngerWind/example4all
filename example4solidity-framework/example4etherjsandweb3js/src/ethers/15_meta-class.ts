/**
 * 元类在运行时确定的其所有属性的类, contract对象使用abi来确定可以使用的方法
 */

/**
 * 对于合约的view和pure方法, 可以使用
 *      contract.methodName(...args, overrides) => Promise<any>
 *          如果方法返回单个值，则将直接返回该值，否则将返回一个Result对象， 
 *          其中包含每个位置可用的参数， 如果参数被命名，那么就按照命名后的值去执行
 *      contract.methodName(...args, overrides) => Promise<Result>
 *          永远返回一Result对象
 * 
 * 如果合约抛出异常, 那么这个方法会抛出一个CALL_EXCEPTION异常, 其中包括error.addrsss(合约地址), error.args(参数), error.transaction(交易)
 * 
 * overrides可以包含如下属性:
 *     from: 代码执行期的msg.sender
 *     value: 合约执行时的msg.value
 *     gasPrice - 每个gas的价格(理论上);因为没有交易，所以不会收取任何费用，但EVM仍然需要向tx.gasprice(或GASPRICE)传递value值
 *     gasLimit - 在执行代码期间允许节点使用的gas数量(理论上);因为没有交易，所以不会有任何费用，但EVM仍然估计gas总数量，因此会向gasleft (或 GAS) 传递这些值。
 */

/**
contract.METHOD_NAME( ...args [ , overrides ] ) ⇒ Promise< TransactionResponse >
    交易被发送到网络后，返回交易的TransactionResponse。 Contract合约对象需要signer。
    它不能返回结果。如果需要一个结果，那么应该使用Solidity事件(或EVM日志)对其进行记录，
    然后可以从交易收据中查询该事件。

    overrides对象可以是以下任何一个:
        overrides.gasPrice - 每个gas的价格
        overrides.gasLimit - 该笔交易允许使用的gas的最大数量，未被使用的gas按照gasPrice退还
        overrides.value - 调用中转账的ether (wei格式)数量
        overrides.nonce - Signer使用的nonce值

    如果返回的TransactionResponse使用了wait()方法，那么在收据上将会有额外的属性:
        receipt.events - 带有附加属性的日志数组(如果ABI包含事件的描述)
        receipt.events[n].args - 解析后的参数
        receipt.events[n].decode - 可以用来解析日志主题（topics）和数据的方法(用于计算args)
        receipt.events[n].event - 事件的名称
        receipt.events[n].eventSignature - 这个事件完整的签名
        receipt.removeListener() - 用于移除触发此事件的监听器的方法
        receipt.getBlock() - 返回发生触发此事件的Block
        receipt.getTransaction() - 返回发生触发此事件的Transaction
        receipt.getTransactionReceipt() - 返回发生触发此事件的Transaction Receipt
 */

/**
contract.estimateGas.METHOD_NAME( ...args [ , overrides ] ) ⇒ Promise< 大数(BigNumber) >
    返回使用 args和overrides执行METHOD_NAME所需的gas的估计单位。
    overrides与上面针对只读或写方法的overrides相同，具体取决于 METHOD_NAME调用的类型。

contract.populateTransaction.METHOD_NAME( ...args [ , overrides ] ) ⇒ Promise< UnsignedTx >
    返回一个未签名交易(UnsignedTransaction)，它表示需要签名并提交给网络的交易，以执行带有args和overrides的METHOD_NAME。
    overrides与上面针对只读或写方法的overrides相同，具体取决于 METHOD_NAME调用的类型。

contract.callStatic.METHOD_NAME( ...args [ , overrides ] ) ⇒ Promise< any >
    比起执行交易状态更改，更可能是会要求节点尝试调用不进行状态的更改并返回结果。
    这实际上并不改变任何状态，而是免费的。在某些情况下，这可用于确定交易是失败还是成功。
    otherwise函数与Read-Only Method相同。
    overrides与上面的只读操作相同。
 */