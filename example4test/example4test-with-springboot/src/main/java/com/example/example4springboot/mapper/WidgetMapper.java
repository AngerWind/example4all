package com.example.example4springboot.mapper;

import com.example.example4springboot.entity.Widget;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/8/28
 * @description
 */
public interface WidgetMapper {

    @Select("select * from widget where id = #{id}")
    Optional<Widget> findById(Long id);

    @Select("select * from widget")
    List<Widget> findAll();

    @Insert("insert into widget(name, description, version) values ( #{name}, #{description}, #{version})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int save(Widget widget);

    @Delete("delete from widget where id = #{id} ")
    int deleteById(Long id);
}
