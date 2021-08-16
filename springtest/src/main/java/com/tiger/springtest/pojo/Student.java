package com.tiger.springtest.pojo;

import com.tiger.springtest.annotation.HelloComponent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.BeanNameAware;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title Student
 * @date 2020/12/7 20:08
 * @description
 */
@Data
@AllArgsConstructor
@HelloComponent(value = "hello world")
@NoArgsConstructor
public class Student implements BeanNameAware {
    @Getter
    @Setter
    private String name;

    @Override
    public void setBeanName(String name){
        this.name = name;
    }
}
