

// 只有对象类型， 没有基本类型
def int_a = 10
println int_a.class

// 单引号字符串
def singleName = 'single name'
println singleName.class

// 多行字符串, \表示连接符
def thupleName = '''\
line one 
line two
'''
println thupleName.class

// 双引号字符串，可以使用占位符，占位符可以使用任何表达式, 其他两种不行
// GStringImpl和String可以相互转换，在使用上可以相互兼容（方法传参，返回值）
def name = "aaa"
def sayHello = "hello ${name}, ${2 + 3}"
println name.class // String
println sayHello.class // class org.codehaus.groovy.runtime.GStringImpl


// 字符串的特殊用法
def str = "groovy"
def hello_groovy = "hello groovy"
def hello = "hello"
println str > hello // false, compareTo方法
println str[0] // g, charAt方法
println str[0..2] // gro
println hello_groovy - str // hello, minus方法





