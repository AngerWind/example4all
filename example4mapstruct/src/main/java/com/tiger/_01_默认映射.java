package com.tiger;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * 默认映射规则如下:
 *   1. 字段名和类型相同, 则会进行映射
 *   2. 8种基本类型和他们对应的包装类型之间
 *   3. 8种基本类型(和他们的包装类型) 和 String 之间
 *   4. 日期类型和 String 之间
 *   5. 等等
 */
public class _01_默认映射 {

    @Data
    public static class SimpleSource {
        private String name;
        private String description;

    }

    @Data
    public static class SimpleDestination {
        private String name;
        private String description;
    }


    /**
     * 在执行mvn clean install 的时候, MapStruct插件会自动生成代码
     * 生成的代码位于/target/generated-sources/annotations/ 目录下。
     */
    @Mapper
    public static interface SimpleSourceDestinationMapper {

        // 通过Mappers.getMapper()方法获取SimpleSourceDestinationMapper的实例。
        public static final SimpleSourceDestinationMapper INSTANCE = Mappers.getMapper(SimpleSourceDestinationMapper.class);

        SimpleDestination sourceToDestination(SimpleSource source);

        SimpleSource destinationToSource(SimpleDestination destination);
    }


    @Test
    public void givenSourceToDestination_whenMaps_thenCorrect() {
        // 创建源对象并赋值
        SimpleSource simpleSource = new SimpleSource();
        simpleSource.setName("SourceName");
        simpleSource.setDescription("SourceDescription");

        // 拷贝
        SimpleDestination destination = SimpleSourceDestinationMapper.INSTANCE.sourceToDestination(simpleSource);

        // 比较值是否一样
        assertEquals(simpleSource.getName(), destination.getName());
        assertEquals(simpleSource.getDescription(), destination.getDescription());
    }

    @Test
    public void givenDestinationToSource_whenMaps_thenCorrect() {
        SimpleDestination destination = new SimpleDestination();
        destination.setName("DestinationName");
        destination.setDescription("DestinationDescription");

        SimpleSource source = SimpleSourceDestinationMapper.INSTANCE.destinationToSource(destination);

        assertEquals(destination.getName(), source.getName());
        assertEquals(destination.getDescription(), source.getDescription());
    }
}
