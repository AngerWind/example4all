
/** 
 *  SourceUnit: c:\Users\Administrator\Desktop\example4all\example4solidity\hardhat-fcc\hardhat-fund-me-fcc-typescript\contracts\FunWithStorage.sol
*/

////// SPDX-License-Identifier-FLATTEN-SUPPRESS-WARNING: MIT
pragma solidity ^0.8.7;

contract FunWithStorage {
    struct Pair {
        uint8 left;
        uint8 middle;
        uint right;
    }
    enum Status {
        None,
        Pending,
        Rejected,
        Canceled,
        Shiped,
        Completed
    }
    // slot 0: 0x0000000000000000000000000000000000000000000000000000000004000302
    // 下面3个变量因为不够32字节, 所以打包在一个slot里面
    uint8 u8 = 2; // label: u8, slot: 0, offset: 0
    uint16 u16 = 3; // label: u16, slot: 0, offset: 1
    uint24 u24 = 4; // label: u24, slot: 0, offset: 3

    // slot 1: 0x0000000000000000000000000000000000000000000000000000000000000005
    uint256 u256 = 5; // label: u256, slot: 1, offset: 0  // 这个变量32字节, 所以单独占一个slot

    // slot 2: 0x0000000000000000000000000000000000000000000000000000000004000302
    // 下面三个变量打包在一个slot里面
    int8 i8 = 2; // label: i8, slot: 2, offset: 0
    int16 i16 = 3; // label: i16, slot: 2, offset: 1
    int24 i24 = 4; // label: i24, slot: 2, offset: 3

    // slot 3: 0x0000000000000000000000000000000000000000000000000000000000000005
    int256 i256 = 5; // label: i256, slot: 3, offset: 0 // 独占

    // slot 4: 0x00000000000000000000015b38da6a701c568545dcfcb03fcb875f56beddc401
    // bool占一个字节
    bool b = true; // label: b, slot: 4, offset: 0
    // address占20字节
    address addr = 0x5B38Da6a701c568545dCfcB03FcB875f56beddC4; // label: addr, slot: 4, offset: 1
    bytes1 b1 = 0x01; // label: b1, slot: 4, offset: 21

    // slot 5: 0x0002012304050607080910111213141516171819202122232425262728293031
    bytes31 b2 = 0x02012304050607080910111213141516171819202122232425262728293031; // label: b2, slot: 5, offset: 0
    // slot 6: 0x0301230405060708091011121314151617181920212223242526272829303132
    bytes32 b3 = 0x0301230405060708091011121314151617181920212223242526272829303132; // label: b3, slot: 6, offset: 0

    // slot 7: 0x0000000000000000000000000000000000000000000000000000000000000005
    Status status = Status.Completed; // label: status, slot: 7, offset: 0

    // slot 8: 0x0000000000000000000000000000000000000000000000000000000000000201
    // slot 9: 0x0000000000000000000000000000000000000000000000000000000000000003
	// 成员变量按照声明顺序存储
    Pair pair = Pair(1, 2, 3); // label: pair, slot: 8, offset: 0

    // slot 10: 0x0000000000000000000000000000000000000000000000000000000000000001
    // slot 11: 0x0000000000000000000000000000000000000000000000000000000000000002
    // slot 12: 0x0000000000000000000000000000000000000000000000000000000000000003
    // slot 13: 0x0000000000000000000000000000000000000000000000000000000000000004
    uint[4] uArray = [1, 2, 3, 4]; // label: uArray, slot: 10, offset: 0
    // slot 14: 0x0000000000000000000000000000000200000000000000000000000000000001
    // slot 15: 0x0000000000000000000000000000000000000000000000000000000000000003
    uint128[3] u128Array = [1, 2, 3]; // label: u128Array, slot: 14, offset: 0

    // slot 16: 0x0000000000000000000000000000000000000000000000000000000000000004
    // 数据保存在keccak256(16)的槽位中
    uint[] uDynamicArray = [1, 2, 3, 4]; // label: uDynamicArray, slot: 16, offset: 0

    // slot 17: 0x48656c6c6f20576f726c64000000000000000000000000000000000000000016
    // slot 18: 0x48656c6c6f20576f726c64000000000000000000000000000000000000000016
	// string和bytes长度低于32字节, 直接存储在slot里面, 高位放数据, 最低位放length*2(字节长度11, 11*2=22, 22的二进制为0x16)
    string s = "Hello World"; // label: s, slot: 17, offset: 0
    bytes bs = "Hello World"; // label: bs, slot: 18, offset: 0

    // slot 19: 0x0000000000000000000000000000000000000000000000000000000000000151
	// string和bytes长度大于32字节, 不存储数据, 低位放length*2+1(字节长度115, 115*2+1=231, 231的二进制为0x151)
	// 数据被存储在keccak256(19)中
    // // label: s1, slot: 19, offset: 0
    string s1 =
        "ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss";

    // slot 20: 0x0000000000000000000000000000000000000000000000000000000000000000
	/**
	 * 计算一下 data[4][9].c的存储位置。映射本身的位置是20。 因此data[4]存储在 keccak256(uint256(4).uint256(20))。 
	 * data[4] 的类型又是一个映射， data[4][9]的数据开始于槽位keccak256(uint256(9).keccak256(uint256(4).uint256(20))。
	 * 在结构 S 的成员 c 中的槽位偏移是 1，因为 a 和 b``被装在一个槽位中。 
	 * 最后 ``data[4][9].c 的插槽位置是 keccak256(uint256(9).keccak256(uint256(4).uint256(20)) + 1. 
	 * 该值的类型是 uint256，所以它使用一个槽。
	 */
	struct S { uint16 a; uint16 b; uint256 c; }
    mapping(uint => mapping(uint => S)) map; // label: map, slot: 20, offset: 0
}

