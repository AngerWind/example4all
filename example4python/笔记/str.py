class A:
    def __init__(self):
        print("a: init_before")
        self.a = 10
        print("a: init_after")
    def __new__(cls, *args, **kwargs):
        print(f"a: new_before, {args}, {kwargs}")
        obj = super().__new__(cls, *args, **kwargs)
        print("a: new_after")
        return obj

    def say(self):
        print("aaa")

a = A()
print("-" * 20)
class B(A):
    def __init__(self):
        print("b: init_before")
        super().__init__()
        self.a = 20
        print("b: init_after")

    def __new__(cls, *args, **kwargs):
        print(f"b: new_before, {args}, {kwargs}")
        obj = super().__new__(cls, *args, **kwargs)
        print("b: new_after")
        return obj

    def say(self):
        print("bbb")

b = B()
print("-" * 20)

class C(A):
    def __init__(self):
        print("c: init_before")
        self.a = 30
        print("c: init_after")

    def __new__(cls, *args, **kwargs):
        print(f"c: new_before, {args}, {kwargs}")
        obj = super().__new__(cls, *args, **kwargs)
        print("c: new_after")
        return obj

    def say(self):
        print("ccc")
class D(A):
    def __init__(self):
        print("d: init_before")
        self.a = 40
        print("d: init_after")

    def __new__(cls, *args, **kwargs):
        print(f"d: new_before, {args}, {kwargs}")
        obj = super().__new__(cls, *args, **kwargs)
        print("d: new_after")
        return obj

    def say(self):
        print("ddd")
class E(B, C, D):
    def __init__(self):
        print("e: init_before")
        self.a = 50
        print("e: init_after")
    def __new__(cls, *args, **kwargs):
        print(f"e: new_before, {args}, {kwargs}")
        obj = super().__new__(cls, *args, **kwargs)
        print("e: new_after")
        return obj
    def say(self):
        print("eee")
e = E()
D.say(e)

print("1" * 20)

class X:
    def say(self):
        print(self.__class__.__name__)
class Y(X):
    def say(self):
        super().say()
        print(self.__class__.__name__)

y = Y()
y.say()