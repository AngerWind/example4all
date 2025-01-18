package com.example.example4springboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/8/28
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Widget {

    private Long id;
    private String name;
    private String description;
    private Integer version;

    public Widget(String name, String description) {
        this.name = name;
        this.description = description;
    }


    public Widget(String name, String description, Integer version) {
        this.name = name;
        this.description = description;
        this.version = version;
    }
}
