package com.example.example4springboot.service;

import com.example.example4springboot.entity.Widget;

import java.util.List;
import java.util.Optional;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/8/28
 * @description
 */
public interface WidgetService {
    Optional<Widget> findById(Long id);
    List<Widget> findAll();
    Widget save(Widget widget);
    void deleteById(Long id);
}
