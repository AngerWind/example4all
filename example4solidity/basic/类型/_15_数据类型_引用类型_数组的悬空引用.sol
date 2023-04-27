// SPDX-License-Identifier:GPL-3.0

pragma solidity ^0.8.17;

contract Test2 {
    // uint256[] public  l = [1, 2, 3];
    uint256[2][] public list1;
    uint256[][] public list2;

    constructor() {
        list1.push([1, 2]);
        list1.push([2, 3]);
        list1.push([3, 4]);

        list2.push([1, 2]);
        list2.push([2, 3]);
        list2.push([3, 4]);
    }

    // function lpush() public {
    //     l.pop();
    //     lpush();

    // }
    // function lpush() public {

    //     l.pop();
    //     lpush();

    // }

    function pushlist1() public {
        // 产生悬空引用, 案例来说pop的元素要被delete掉, 再次push后是新的元素, 但是这里没有
        // pop后再push的元素还是s
        uint[2] storage s = list1[list1.length - 1];
        list1.pop();
        s[0] = 100;
        s[1] = 1290;
        list1.push();
    }

    function pushlist1_simple() public {
        // 不产生悬空引用, pop后再push是新的元素
        list1.pop();
        list1.push();
    }

    function pushlist2() public {
        // 产生悬空引用, 案例来说pop的元素要被delete掉, 再次push后是新的元素, 但是这里没有
        // pop后再push的元素还是s
        uint[] storage s = list2[list2.length - 1];
        list2.pop();
        s.push(2);
        s.push(999);
        list2.push();
    }

    function pushlist2_simple() public {
        // 不产生悬空引用, pop后再push是新的元素
        list2.pop();
        list2.push();
    }
}
