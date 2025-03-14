import random
from dataclasses import dataclass, field
from typing import ClassVar

"""
自动生成__init__方法来生成name和age属性, 
自动生成__repr__方法, 打印对象时会输出属性
自动生成__eq__方法, 所以在调用 == 比较两个对象的时候, 比较的是属性, 而不是内存地址
"""
# 还可以通过装饰器的属性来详细设置, 比如这里设置生成比较相关的方法, 比较是按照属性的字典序来的
@dataclass(order=True)
class Person:
    name: str = "null"
    age: int = 0
    # 通过field来精确控制属性
    # default为0, height不作为对象属性, 打印的时候不输出, 不在比较的时候使用
    height: int = field(default=0, init=False, repr=False, compare=False)
    # default_factory表示通过这个函数来生成属性的默认值
    weight: int = field(default_factory=lambda: random.randint(1, 100))

    # 因为dataclass通过静态属性来生成对象属性, 如果你要定义真正的静态属性
    # 你需要通过如下方式来进行
    people_num: ClassVar[int] = 0

    # 对象生成之后, 会自动调用生命周期函数
    def __post_init__(self):
        Person.people_num += 1


class Person1:
    def __init__(self, name: str, age: int):
        self.name = name
        self.age = age


if __name__ == '__main__':
    p1 = Person()
    print(Person.name)
    Person.age = 100
    print(Person.age)