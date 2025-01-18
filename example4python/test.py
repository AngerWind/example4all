


# 元类会自动将你通常传给‘type’的参数作为自己的参数传入
def upper_attr(class_name, class_parents, class_attr):
    '''返回一个类对象，将属性都转为大写形式'''
    #  选择所有不以'__'开头的属性
    #  (a for value in collection if condition)是一个生成器语法, 用来生成迭代器
    attrs = ((name, value) for name, value in class_attr.items() if not name.startswith('__'))
    # 将它们转为大写形式
    uppercase_attr = dict((name.upper(), value) for name, value in attrs)

    print("call upper")

    # 通过'type'来做类对象的创建
    return type(class_name, class_parents, uppercase_attr)

class Person(metaclass=upper_attr):
    bar = 'bip'

print(f'{hasattr(Person, "BAR")}') # False