# 元类会自动将你通常传给‘type’的参数作为自己的参数传入
def upper_attr(name, parents, attr):
    '''返回一个类对象，将属性都转为大写形式'''
    #  选择所有不以'__'开头的属性
    attrs = ((name, value) for name, value in attr.items() if not name.startswith('__'))
    # 将它们转为大写形式
    uppercase_attr = dict((name.upper(), value) for name, value in attrs)

    # 通过'type'来做类对象的创建
    return type(name, parents, uppercase_attr)

# python3不支持这种写法
# 必须在继承的时候显示指定
__metaclass__ = upper_attr  #  这会作用到这个模块中的所有类

class Foo(object):
    # 我们也可以只在这里定义__metaclass__，这样就只会作用于这个类中
    bar = 'bip'
print(hasattr(Foo, 'bar'))

print(hasattr(Foo, 'BAR'))


f = Foo()
print(f.BAR)
