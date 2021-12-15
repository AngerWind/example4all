package closure

class Student {
    static def static_closure = {
        println "static_closure this: " + this
        println "static_closure owner: " + owner
        println "static_closure delegate: " + delegate
        // static_closure this: class Student
        // static_closure owner: class Student
        // static_closure delegate: class Student

        def static_inner_closure = {
            println "static_inner_closure this: " + this
            println "static_inner_closure owner: " + owner
            println "static_inner_closure delegate: " + delegate
        }
        static_inner_closure.call()
        // static_inner_closure this: class Student
        // static_inner_closure owner: Student$__clinit__closure2@4d48bd85
        // static_inner_closure delegate: Student$__clinit__closure2@4d48bd85
    }

    def instance_closure = {
        println "instance_closure this: " + this
        println "instance_closure owner: " + owner
        println "instance_closure delegate: " + delegate
        // instance_closure this: Student@4f63e3c7
        // instance_closure owner: Student@4f63e3c7
        // instance_closure delegate: Student@4f63e3c7

        def instance_inner_closure = {
            println "instance_inner_closure this: " + this
            println "instance_inner_closure owner: " + owner
            println "instance_inner_closure delegate: " + delegate
        }
        instance_inner_closure.call()
        // instance_inner_closure this: Student@4f63e3c7
        // instance_inner_closure owner: Student$_closure1@dca2615
        // instance_inner_closure delegate: Student$_closure1@dca2615
    }
}

Student.static_closure.call()
new Student().instance_closure.call()


class Child {
    String name
    def say = {
        println "my name is ${name}"
    }
}

class Parent {
    String name
}

def p = new Parent(name: "parent")
def c = new Child(name: "child")
c.say()
c.say.delegate = p
c.say.resolveStrategy = Closure.DELEGATE_FIRST // 默认策略 OWNER_FIRST
c.say()




