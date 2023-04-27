"""
在对一个对象进行for..in...迭代的时候, 会先调用该对象的__iter__方法获得一个迭代器
然后调用迭代器的__next__获得迭代的元素
如果迭代完成, 那么要在__next__中抛出StopIteration异常
"""


class MyList:
    """MyList 实现了迭代器协议 它的实例就是一个迭代器"""

    def __init__(self, list):
        self.idx = 0
        self.list = list

    # 该函数返回一个迭代器对象, 迭代器对象需要有__next__方法
    def __iter__(self):
        print('__iter__')
        return self

    # 该函数返回迭代的元素, 如果迭代结束, 抛出StopIteration异常
    def __next__(self):
        if self.idx < len(self.list):
            val = self.list[self.idx]
            self.idx += 1
            return val
        else:
            raise StopIteration()


# 迭代元素
a = MyList([1,4,6,846,4156,416,541,65,5,64,56,56,4,6,5])
for i in a:
    print(i)
"""
注意a对象只能迭代一次, 如果要迭代多次, 需要单独返回一个迭代器独享
"""
for i in a:
    print(i)
