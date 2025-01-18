class A:
    def __init__(self, *args, **kwargs):
        print(args, kwargs)
        super().__init__(*args, **kwargs)

    def __new__(cls, msgA, msgB, **kwargs):
        print(msgA)
        return super().__new__(cls, msgB, **kwargs)

class B:
    def __init__(self, msgA, msgB, msgC, a = None, b= None, c= None):
        print(msgA, msgB, msgC, a, b, c)

    def __new__(cls, msgB, **kwargs):
        print(msgB)
        return super().__new__(cls) # 调用object的__new__创建对象

class C(A, B):
    def __new__(cls, *args, **kwargs):
        msgA, msgB, msgC = args[:3]

        print(msgC)
        return super().__new__(cls, msgA, msgB, **kwargs)

C("hello1", "hello2", "hello3", a = 1, b = 2, c = 3)