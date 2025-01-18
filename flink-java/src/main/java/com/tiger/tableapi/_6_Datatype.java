package com.tiger.tableapi;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/8/17
 * @description
 */
public class _6_Datatype {
    /*
        在flink sql中, 内置的数据类型有如下几种

        char(n): 固定长度字符串, n必须在1-2147483647之间, 如果不指定, n默认为1
        varchar(n): 可变长度字符串, 其中n表示字符串的最大长度,
                    n必须介于1-2147483647之间, 如果不指定, n默认为1
        string: 可变长度字符串, 等同于varchar(2147483647)

        binary(n): 固定长度二进制字符串, 其中n必须介于1-2147483647之间, 如果不指定, n默认为1
        varbinary(n): 可变长度二进制字符串, 其中n表示二进制字符串的最大长度,
                       n必须介于1-2147483647之间, 如果不指定, n默认为1
        bytes: 可变长度二进制字符串, 等同于varbinary(2147483647)

        decimal(p,s): 固定精度的小数, 其中p表示总位数, s表示小数位数
                      p必须在1-38之间, s必须在0-p之间
                      p默认为10, s默认为0

        tinyint: 1 字节有符号整数的数据类型，其值介于-128到127之间。
                  等同于java中的byte
        smallint: 2 字节有符号整数的数据类型，其值介于-32,768到32,767之间
                  等同于java中的short
        integer/int: 4 字节有符号整数的数据类型, 等同于java中的int
        bigint: 8字节的有符号整型, 等同与java中的long
        float: 等同于java中的float
        double: 等同于java中的double

        boolean: 布尔类型, 可选的值有: TRUE, FALSE和UNKNOWN

        date: 日期类型, 值范围为0000-01-01到9999-12-31
        time(p): 时间类型, 由hour:minute:second[.fractional]组成，精度高达纳秒
                 p表示小数秒的位数, 必须在0-9之间, 默认为0, 即没有小数秒
                 值范围从00:00:00.000000000到23:59:59.999999999
        timestamp(p): 不带时区的时间戳的数据类型, 由year-month-day hour:minute:second[.fractional]组成，精度高达纳秒，
                      p表示小数秒的位数, 必须在0-9之间, 默认为0, 即没有小数秒
                      值范围从0000-01-01 00:00:00.000000000到9999-12-31 23:59:59.999999999 。
        timestamp_ltz: 与timestamp(p)类似, 但是带了时区信息

        interval: 用于表示时间间隔, 格式有以下几种
                   1. INTERVAL '2' YEAR,  间隔2年
                   2. INTERVAL '2-07' YEAR TO MONTH, 间隔2年7个月
                   3. INTERVAL '10' MONTH, 间隔10个月
                   4. INTERVAL '100' DAY
                   5. INTERVAL '100 10' DAY TO HOUR
                   6. INTERVAL '100 10:30' DAY TO MINUTE
                   7. INTERVAL '100 10:30:40.999999' DAY TO SECOND
                   8. INTERVAL '123' HOUR
                   9. INTERVAL '123:10' HOUR TO MINUTE
                   10. INTERVAL '123:10:59' HOUR TO SECOND
                   11. INTERVAL '1000' MINUTE
                   12. INTERVAL '1000:01.001' MINUTE TO SECOND
                   13. INTERVAL '1000.000001' SECOND
                   interval的作用主要是用在时间的加减上
        array<t>: 数组类型
        map<k,v>: map类型, key和value都可以为null
        row<n0 t0, n1 t1,...>: 类似与hive中的struct类型
        multiset<t>: MULTISET 允许集合中的元素出现多次
                     并且每个元素有一个与之关联的计数值，表示该元素在集合中出现的次数
                     CREATE TABLE Orders (
                         order_id INT,
                         product_ids MULTISET<INT>
                     ) WITH (...);
                     INSERT INTO Orders VALUES (1, MULTISET[1, 2, 2, 3]);
                     INSERT INTO Orders VALUES (2, MULTISET[4, 4, 4, 5]);
     */

}
