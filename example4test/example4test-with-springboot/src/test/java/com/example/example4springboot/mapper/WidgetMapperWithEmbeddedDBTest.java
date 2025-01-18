package com.example.example4springboot.mapper;


import com.example.example4springboot.entity.Widget;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.jdbc.SqlMergeMode.MergeMode;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/8/28
 * @description
 */
@MybatisTest // 替换SpringTest
// 默认情况下replace为ANY, 此时会创建一个内嵌的内存数据库作为我们的测试数据库,
// 所以我们在maven的test环境下必须导入一个内嵌数据库(比如h2)的依赖, 否则会找不到内嵌数据库支持而报错
// 如果我们想要使用真实的数据库, 那么我们可以设置replace为NONE
@AutoConfigureTestDatabase()

// 默认情况下, 在执行完测试方法后, 会自动回滚事务, 防止我们的测试代码污染数据库
// 可以添加@Rollback(false)来取消事务回滚, 这样测试代码会真正的影响数据库
// 这个配置可以添加在类上, 也可以添加在方法上
// @Rollback(false)
// @Commit // @Commit等效于@Rollback(false)

// 如果是使用内嵌的数据库, 因为是全新的数据库, 所以我们必须在执行测试代码之前, 先执行一些建表/插入数据的操作
// 可以使用下面这个注解来配置执行测试之前/之后执行指定的sql语句
// @Sql可以添加在class上, 也可以添加在method上
// 可以指定多个@Sql注解
// 默认情况下, 方法上的@Sql会覆盖class上的@Sql注解, 但是
@SqlGroup(value = {@Sql(scripts = {"classpath:/schema.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_CLASS),
        @Sql(statements = {"select * from widget;"}, executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)})

// 这个注解可以放在class上, 也可以放在method上
// 如果放在class上, 表示所有的方法级别的@Sql注解都会与class上的@Sql注解合并/覆盖
// 如果放在method上, 表示当前方法的@Sql注解会与当前class上的@Sql注解合并/覆盖
@SqlMergeMode(MergeMode.MERGE)

public class WidgetMapperWithEmbeddedDBTest {

    @Autowired
    private WidgetMapper widgetMapper;

    @Test
    @Sql(scripts = "classpath:/data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD) // 指定在当前测试之前要执行的sql
    @SqlMergeMode(MergeMode.MERGE) // 指定当前方法上的@Sql与class上的@Sql合并, 而不是覆盖
    @Commit // 标注在方法上, 那么在执行完测试的时候会提交事务, 而不是回滚事务
    @Rollback(false) // 作用与@Commit一样
    public void testSelectByPrimaryKey() {
        Optional<Widget> result = widgetMapper.findById(1L);
        assertThat(result.isPresent(), equalTo(true));
        assertThat(result.get(), equalTo(new Widget(1L, "Widget1", "This is the first widget", 1)));
    }

    @Test
    public void testInsert() {
        Widget widget = new Widget("Widget1", "This is the fist widget", 1);
        int row = widgetMapper.save(widget);
        assertThat(row, is(1));
        assertThat(widget.getId(), notNullValue());
    }

}