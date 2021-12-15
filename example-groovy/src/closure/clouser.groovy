package closure
// 无参, 箭头前是参数， 箭头后是参数体
def clouser = {
    println 'hello clouser'
}
def clouser1 = { ->
    println 'hello clouser'
}
clouser.call()

// 有参，参数可以省略类型
def sayHello = { String name, age ->
    println "${name}, ${age}"
}
sayHello.call("zhangsan", 4)

// 所有闭包都有it这个默认参数，不需要显式声明
def say = {
    println "hello ${it}"
}
say.call("zhangsan")

// 闭包一定有返回值， 没有显式return的话返回的是null
println say.call("zhangsan")

