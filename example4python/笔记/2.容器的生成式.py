'''
生成器是一个特殊的迭代器对象, 他本身是可迭代的,即他有__iter__函数, 可以通过for in来迭代
同时__iter__生成的迭代器是他本身(说明生成器也有__next__函数)

有两种方式可以创建生成器
    生成器表达式
    生成器函数
'''
# 生成器表达式创建一个表达式
g = (i for i in range(5))
print(g, type(g), g == g.__iter__()) # 生成器返回的迭代器对象是他本身
# 通过for in 来迭代这个迭代器
for i in g:
    print(i)


# 通过函数来创建一个生成器
def gen(n):
    for i in range(n):
        yield i
g = gen(5)
print(g, type(g))
# 通过for in 来遍历
for i in g:
    print(i)




'''
通过生成器函数来快速创建一个可迭代对象
'''
print("------------------------------")
class MyList:
    def __init__(self, n):
        self.n = n
    def __iter__(self):
        for i in range(self.n):
            yield i
for i in MyList(10):
    print(i)





'''
通过生成器来生成list
'''
def double(list):
    return [i * 2 for i in list]

# 使用列表生成式要求列表中的元素都有一定的规则
lst = [i for i in range(1, 10)]
print(lst)
lst1 = [i*i for i in range(1, 10)]
print(lst1)

# 要求：列表中的元素的值为2,4,6,8,10
lst2 = [i*2 for i in range(1, 6)]
print(lst2)


'''
通过生成器来生成dict
'''
items = ['Fruits', 'Books', 'Others']
prices = [96, 78, 85]
# zip用于将可迭代的对象作为参数, 将对象中对应的参数打包成一个元组, 然后返回元组的列表
# 下面这个生成式的步骤为
# 1. 通过zip生成元组的列表
# 2. 通过for in 迭代每一个元组
# 3. 从元组中提取元素组合成一个键值对放入字典中
d = {items.upper(): prices   for items, prices in zip(items, prices)}
print(d)


'''
通过生成器来生成set
'''
# 集合生成式
s = {i*i for i in range(10)}  # 无序
print(s)
