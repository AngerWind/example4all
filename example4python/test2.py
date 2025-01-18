class UpperAttrMetaclass(type):
    def __new__(cls, name, bases, dct):
        attrs = ((name, value) for name, value in dct.items() if not name.startswith('__'))
        uppercase_attr = dict((name.upper(), value) for name, value in attrs)
        # 这里调用父类的__new__来构建类对象
        # 下面两种方式是一样的, 推荐使用super这种方式,
        # return super(UpperAttrMetaclass, cls).__new__(cls, name, bases, uppercase_attr)
        return type.__new__(cls, name, bases, uppercase_attr)


class FilterMetaclass(UpperAttrMetaclass):
    def __new__(cls, name, bases, dct):
        # 过滤掉xxx开头的属性和方法
        attrs = ((name, value) for name, value in dct.items() if not name.startswith('xxx'))
        uppercase_attr = dict((name.upper(), value) for name, value in attrs)
        # 调用父类UpperAttrMetaclass来构建类对象
        return super(UpperAttrMetaclass, cls).__new__(cls, name, bases, uppercase_attr)

class Person(metaclass=FilterMetaclass):
    xxxaaa = 'hello'
    bb = "bb"
print(f'{hasattr(Person, "BB")}, {hasattr(Person, "xxxaaa")}, {hasattr(Person, "XXXAAA")}')
# True, False, False