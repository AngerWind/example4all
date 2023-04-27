# 下面这个函数对参数和返回值都添加了类型
# 告诉你期待的输入类型和输出类型
# 但是这并不能加快计算, 而且就算限定了int, 但是使用的时候传入float也不会报错
# 归根结底他就是一个注释
def add(a:int, b:int) -> int:
    return a+b

print(add(1.2, 2.4))
print(add.__annotations__) # {'a': <class 'int'>, 'b': <class 'int'>, 'return': <class 'int'>}