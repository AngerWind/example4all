'''
with关键字用于异常处理, 封装了try except finally编码范式, 类似于java中的try-with-resource
'''


# 下面这个函数用于写文本
def write(path):
    file = open(path, "w")
    try:
        file.write("hello")
    except BufferError as e:
        print(e)
    finally:
        file.close()


# 上面的函数通过with来改写
def write1(path):
    with open(path, "w") as file:
        file.write("hello")

'''
with语句在语义上等价于
    context_manager = context_expression # 执行上下文表达式(with后面的表达式)获取上下文管理器
    exit = type(context_manager).__exit__ # 获取上下文管理器的__exit__方法
    value = type(context_manager).__enter__(context_manager) # 执行上下文管理器的__enter__方法
    exc = True   # True 表示正常执行，即便有异常也忽略；False 表示重新抛出异常，需要对异常进行处理
    try:
        try:
            target = value  # 如果使用了 as 子句
            with-body     # 执行 with-body
        except:
            # 执行过程中有异常发生
            exc = False
            # 如果 __exit__ 返回 True，则异常被忽略；如果返回 False，则重新抛出异常
            # 由外层代码对异常进行处理
            if not exit(context_manager, *sys.exc_info()):
                raise
    finally:
        # 正常退出，或者通过 statement-body 中的 break/continue/return 语句退出
        # 或者忽略异常退出
        if exc:
            exit(context_manager, None, None, None)
        # 缺省返回 None，None 在布尔上下文中看做是 False
        
        
with关键字原理:
    1. 对with对象后面的语句进行求值, 返回对象context_manager
    2. 调用返回对象context_manager的__enter__方法, 将返回对象赋值给as后面的引用
    3. 执行with体
    4. 如果发生了异常, 执行context_manager的__exit__方法并传入异常信息, 
        如果exit方法返回true则异常被忽略, 否则继续向上抛出
    5. 如果没有发生异常, 执行执行context_manager的__exit__方法并传入None
'''
class Sample():
    def __enter__(self):
        print('in enter')
        return self

    def __exit__(self, exc_type, exc_val, exc_tb):
        print("type: ", exc_type)
        print("val: ", exc_val)
        print("tb: ", exc_tb)
        # 返回true表示异常被忽略, false表示继续向上抛出异常
        return False

    def cause_error(self):
        bar = 1 / 0
        return bar + 10
    def do_something(self):
        print("do...")

with Sample() as sample:
    sample.do_something()

with Sample() as sample:
    sample.cause_error()

'''
还支持多个with语句嵌套来处理多个上下文管理器
with A() as a, B() as b:
    SUITE
在语义上等价于:
with A() as a:
    with B() as b:
        SUITE
'''
