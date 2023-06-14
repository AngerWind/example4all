
/** 
 *  SourceUnit: c:\Users\Administrator\Desktop\example4all\example4solidity\basic\类型\_16_数据类型_引用类型_结构体.sol
*/

////// SPDX-License-Identifier-FLATTEN-SUPPRESS-WARNING:GPL-3.0

pragma solidity ^0.8.17;

// 放在这里, 当前文件中所有合约都能使用
struct Movie1 {
    string title;
    string director;
    uint id;
}

contract LearnStructs {
    // 放在这里只有当前合约能用
    struct Movie {
        string title;
        string director;
        uint id;
    }
    Movie public movie;
    Movie[] public ms;

    constructor() {
        // pass param by index
        movie = Movie("xcad", "sdfa", 12);
        // pass param by name
        movie = Movie({title: "sdfa", director: "sdfa", id: 123});

        // 结构体数组的赋值
        // 方法1
        ms.push(Movie("xsd", "sdsd", 13));

        // 方法2
        Movie storage m = ms.push();
        m.title = "sd";
        m.director = "sdf";
        m.id = 12;

        // 方法3
        Movie storage m1 = ms[2];
        m1.title = "sddfg";
        m1.director = "sdfasdf";
        m1.id = 12;
    }

    struct Tv {
        string title;
        string director;
        uint id;
        string[] maker;
        mapping(string => uint) makerAndAge;
    }
    Tv[] public tvs;
    mapping(uint => Tv) idAndTv;

    // 包含mapping的结构体
    function test() public {
        // 因为Tv结构体中包含了mapping, 所以实际上无法创建出Tv memory类型, 如果想要在数组或者映射中添加TV, 使用以下方式
        Tv storage tv = tvs.push();
        tv.title = "title1";
        // ... other property assingment

        Tv storage tv1 = idAndTv[4];
        tv1.title = "title2";
        // ... other property assingment
    }

    // 从memory的结构体赋值到storage的结构体, 深拷贝
    struct Array {
        string[] value;
    }
    Array a;

    function test1() public {
        Array memory a1 = Array(new string[](1));
        a1.value[0] = "zhangsan";

        a = a1;
        a1.value[0] = "lisi"; // a.value[0] 依然是zhangsan
    }

    function getA() public view returns (string[] memory) {
        return a.value;
    }
}

