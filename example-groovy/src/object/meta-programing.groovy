package object

class Person {
    String name
}

def person = new Person(name: "zhangsna");

class Student1 implements GroovyInterceptable {
    protected dynamicProps = [:]

    void setProperty(String pName, val) {
        dynamicProps[pName] = val
    }

    def getProperty(String pName) {
        dynamicProps[pName]
    }

    def invokeMethod(String name, Object args) {
        return "called invokeMethod $name $args"
    }
}

def s = new Student1()
s.name = "aaa"
s.age = 10