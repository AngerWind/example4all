class A:
    def __new__(cls, *args, **kwargs):
        print(f"a: new_before, {args}, {kwargs}")
        obj = super().__new__(cls) #
        print("a: new_after")
        return obj
class B(A):
    def __new__(cls, *args, **kwargs):
        print(f"b: new_before, {args}, {kwargs}")
        obj = super().__new__(cls, *args, **kwargs)
        print("b: new_after")
        return obj
class C(A):
    def __new__(cls, *args, **kwargs):
        print(f"c: new_before, {args}, {kwargs}")
        obj = super().__new__(cls, *args, **kwargs)
        print("c: new_after")
        return obj
class D(A):
    def __new__(cls, *args, **kwargs):
        print(f"d: new_before, {args}, {kwargs}")
        obj = super().__new__(cls, *args, **kwargs)
        print("d: new_after")
        return obj
class E(B, C, D):
    def __new__(cls, *args, **kwargs):
        print(f"e: new_before, {args}, {kwargs}")
        obj = super().__new__(cls, *args, **kwargs)
        print("e: new_after")
        return obj
e = E(1, 2, a = 1, b = 1)
