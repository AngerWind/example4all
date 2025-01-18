package com.example.example4springboot.mapper;

import com.example.example4springboot.entity.Widget;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
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
@AutoConfigureTestDatabase(replace = Replace.NONE) // 使用真实的数据库
public class WidgetMapperWithRealDBTest {

    @Autowired
    private WidgetMapper widgetMapper;

    // 在事务中执行, 默认会回滚, 不会影响数据库
    @Test
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