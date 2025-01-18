package com.tiger.tableapi.source_sink;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/8/17
 * @description
 */
public class _05_Datagen {

    /**
     * datagen主要用来生成测试数据
     *
     * 他有两种生成数据的方法:
     *   1. 随机生成, 无界的, 永不会停止
     *         在生成数字的时候, 可以指定生成的数字的范围
     *         在生成字符串的时候, 可以指定生成字符串的长度
     *   2. 序列生成器, 有界的
     *         可以指定生成的数字的范围, 当生成的数字达到范围的时候, 就会停止生成
     */
    /*
         create table datagen (
             name string,
             phone int,
             age int
         ) with (
             'connector' = 'datagen', -- 指定连接器
             'fields.name.kind' = 'random', -- 指定name的生成方式, 随机生成
             'fields.name.length' = '10', -- 指定name的长度, 只适用于 char、varchar、string。

              'fields.age.kind' ='sequence', -- 指定age的生成方式, 序列生成
              'fields.age.start' = '18', -- 指定随机生成的范围, 只能是数字, 生成到了60的时候就会停止生成
              'fields.age.end' = '60',

             'fields.phone.kind' = 'random', -- 指定phone的生成方式, 随机生成
             'fields.phone.min' = '100000000', -- 随机生成器的范围, 只对数字类型有效
             'fields.phone.max' = '999999999'
         );
     */
}
