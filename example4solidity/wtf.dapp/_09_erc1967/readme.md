#### 透明代理和UUPS代理 

参考: https://docs.openzeppelin.com/contracts/4.x/api/proxy#ERC1967Upgrade
    https://www.wtf.academy/solidity-application/TransparentProxy/
    https://blog.openzeppelin.com/the-state-of-smart-contract-upgrades/   



​	https://learnblockchain.cn/article/3066    



​	https://eips.ethereum.org/EIPS/eip-1967

​	https://eips.ethereum.org/EIPS/eip-1822



最基础的可升级的代理就是proxy通过在fallback中调用delegatecall来调用logic合约的方法
但是这样会有以下的问题：

1. delegatecall对变量的修改是通过槽位来修改的, 而槽位是通过变量的顺序来确定的, 所以proxy和logic的存储变量要完全一致, 否则会导致未知的问题, 所以在logic中也要像proxy一样保存一个无用的logic地址

   ~~~solidity
   contract Proxy {
   	address logic;
   	uint value;
   	fallback () external {...}
   }
   
   contract Proxy {
   	address logic; // 这个地址是无效的, 但是为了槽位的对齐, 必须存在
   	uint value;
   }
   ~~~

   

2. 函数选择器冲突, 在solidity中函数选择器是函数签名keccak256的前四个字节, 因为只有四个字节, 两个不同函数签名的函数可以会有相同的选择器
            如果选择器冲突发生在一个合约中, 那么编译器能够检测到, 但是proxy和logic中有函数选择器冲突, 编译器无法检测到, 可能会导致未知的问题
            例如: 普通用户想要通过proxy的fallback调用logic中的a方法, 但是logic中的a方法和proxy中的b方法选择器冲突, 那么普通用户就会调用到proxy的b方法
                  管理员想要调用proxy的a方法, 但是proxy中没有a方法, 并且a方法和logic中的b方法选择器冲突, 那么管理员就会调用到logic中的b方法

解决上面两个问题的办法是: 

1. 将和代理相关的变量保存在一个固定的槽位中, 这样proxy中就不需要保存相关的变量了, 需要用的时候通过固定的槽位来获取就行了, 同时logic也不需要保存无用的变量

2. 因为函数选择器冲突的问题, 会导致管理员和普通用户都调用到意想不到的函数, 所有必须要求普通用户只能调用fallback方法

   而管理员只能调用proxy中的方法. 这样就可以避免函数选择器冲突的问题

   上面所说的就是!!!Eip1967!!!的基本思路, 即!!!透明代理!!!

另外一种解决方案是:

1. 将所有代码(包括升级代码)都放在logic代码中, 这样的话proxy就不会比logic多保存变量了

   同时因为所有代码都在logic中, 所以不会有函数选择器冲突的问题

   上面说的就是!!!ERC1822!!!的基本思路, 即!!!UUPS代理!!!!!!!!

   同时在openzeeplin的erc1822的实现中, 也采用了erc1967的将所有和代理相关的变量放在固定槽位的思路

   但是这种方式也会有一个弊端就是, 因为升级的代码也在logic中, 如果升级到了一个其中不包含升级代码的logic中, 那么这个logic就再也无法升级了

   

同时考虑另外一个问题: 

​	因为多个proxy可用公用一个logic, 如果logic进行了升级的话, 那么多个proxy都需要升级, 这样就会导致升级的成本很高
​    所以需要一个中间层beacon, proxy在调用logic的时候, 需要通过beacon来获取logic的地址, 这样只需要升级一次beacon, 就可以让所有的proxy都升级了






如果要使用透明代理的话, 可以使用openzeeplin中的TransparentUpgradeableProxy
如果要使用UUPS代理的话, 可以使用openzeeplin中的UUPSUpgradeable
