

namespace java com.tiger.example

struct Student {
    1: optional string optional_value
    2: optional string optional_value_1 = 'option value'

    3: string default_value
    4: string default_value_1 = 'default value'

    5: required string required_value
    6: required string required_value_1 = 'required value'

        7: i32 default_value_3
        8: i32 default_value_4 = 123

        9: optional i32 default_value_5
        10: optional i32 default_value_6 = 123
}

struct Student1 {
    1: optional i32 optional_value
    2: optional i32 optional_value_1 = 123

    3: i32 default_value
    4: i32 default_value_1 = 123

    5: required i32 required_value
    6: required i32 required_value_1 = 123
}