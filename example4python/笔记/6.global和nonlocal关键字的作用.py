'''
Python变量的作用域一共有4种，分别是：
    L （Local） 局部作用域
    E （Enclosing） 闭包函数外的函数中
    G （Global） 全局作用域
    B （Built-in） 内建作用域 以 L –> E –> G –>B 的规则查找，
    即：在局部找不到，便会去局部外的局部找（例如闭包），再找不到就会去全局找，再者去内建中找。
    如果找不到则抛出 UnboundLocalError 异常。
    https://zhuanlan.zhihu.com/p/111284408
'''

# 下面这段代码可以将x分别注释掉, 看看打印的是谁
x = 6 # 全局变量
def func():
    x = 5 # 闭包函数外的函数中的变量
    # 闭包函数
    def func1():
        x = 4 # 局部变量
        print(x)
    return  func1
func1 = func()
func1()

'''
但是内部函数有引用外部函数的同名变量或者全局变量，
并且对这个变量有修改的时候，此时 Python 会认为它是一个局部变量，

即函数可以读取闭包外的变量或者全局变量, 但是不能修改
如果要在函数内修改闭包外的变量或者全局变量, 分别使用nonlocal和global关键字
https://blog.csdn.net/mydistance/article/details/86554904
'''

y = 6 # 全局变量
def func():
    y = 5 # 闭包函数外的函数中的变量
    # 闭包函数
    def func1():
        # 这里对y进行了修改, 会被认为是局部变量, 所以会报错
        # nonlocal y  # nonlocal声明变量为非本地变量, y=5
        global y  # global 声明变量为全局变量, y=6
        y += 1
        print(y)
    return  func1
func1 = func()
func2 = func()
func1()
func2()
