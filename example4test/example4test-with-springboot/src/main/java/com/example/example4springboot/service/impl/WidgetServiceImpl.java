package com.example.example4springboot.service.impl;

import com.example.example4springboot.entity.Widget;
import com.example.example4springboot.mapper.WidgetMapper;
import com.example.example4springboot.service.WidgetService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WidgetServiceImpl implements WidgetService {

    private WidgetMapper widgetMapper;

    // 构造函数注入
    public WidgetServiceImpl(WidgetMapper widgetMapper) {
        this.widgetMapper = widgetMapper;
    }

    @Override
    public Optional<Widget> findById(Long id) {
        return widgetMapper.findById(id);
    }

    @Override
    public List<Widget> findAll() {
        return widgetMapper.findAll();
    }

    @Override
    public Widget save(Widget widget) {
        // Increment the version number
        widget.setVersion(widget.getVersion() + 1);

        // Save the widget to the repository
        widgetMapper.save(widget);

        return widget;
    }

    @Override
    public void deleteById(Long id) {
        widgetMapper.deleteById(id);
    }
}