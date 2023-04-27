'''
调用in 和 not in 会调用对象的__contains__方法
'''
class MyList:
    def __init__(self, list):
        self.list = list

    def __contains__(self, item):
        return self.list.__contains__(item)

list = MyList([1,5,6,8,9])
print(5 in list)
print(10 not in list)