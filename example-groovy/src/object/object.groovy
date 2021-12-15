package object

// 一个groovy文件可以表示类，脚本，接口，注解，Trait，枚举
// 存在不在类中的代码，将被视为脚本，将自动继承Script。
// 脚本中public的class编译后变成单独的class文件，所以同包下不同脚本中的同名public类会冲突
// 脚本中不在类中的代码将放在编译后的main方法中


// 访问权限修饰符默认是public
class Student {
    String name
    // 没有基础类型，int也是Integer类型, 使用时自动解保证，null解包装会报错
    int age
    private int sex

    // 使用def和返回值类型定义方法, def默认返回Object类型
    // 没有return返回null
    def say() { }
    Integer sayHello() { }
    // 方法参数可以有默认值，缺省参数不必在最后，省略参数类型默认为Object
    Integer sayHi(String name = "zhangsan", age) {}
}

// 为所有属性自动添加构造方法，包括private
def student = new Student(name: "zhangsan", age: 33, sex: 1)

// 对属性的调用自动转换为getter/setter的调用
student.name = "lisi"

