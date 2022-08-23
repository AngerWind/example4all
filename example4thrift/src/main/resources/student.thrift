namespace java com.tiger.example

include 'address/address.thrift'
include 'enumeration.thrift'

const i8 SHORT_VALUE_1 = 100
const i8 SHORT_VALUE_2 = -2
const double DOUBLE_VALUE_1 = 3.14
const double DOUBLE_VALUE_2 = 1.2e-5   // 可以使用科学计数法，表示 0.000012
const double DOUBLE_VALUE_3 = 3.5e8    // 表示 350000000
// 常量 list, 类似 js 中的数组字面量
const list<string> LIST_VALUE = [ 'tom', 'joney', 'catiy' ]  //分割符可以是逗号, 分号, 或者不写
// 常量 map, 类似 js 中的对象字面量
const map<string, string> MAP_VALUE = { 'name': 'johnson', 'age': '20' } //分割符可以是逗号, 分号, 或者不写

typedef i64 long
typedef i32 int

struct Student {
  1: required address.Address addr
  2: required int age = SHORT_VALUE_1
  3: required enumeration.Color color
  4: required UserInfo user_info
}

union UserInfo {
  1: string phone,
  2: string email
}

exception Error {
  1: required i8 code,
  2: required string msg,
}

service StudentService {
    Student getStudentByIdAndName(1: required i32 id) throws (1: Error error)
    void saveStudent(1: required Student student)
}