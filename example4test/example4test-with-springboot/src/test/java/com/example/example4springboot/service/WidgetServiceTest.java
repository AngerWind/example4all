package com.example.example4springboot.service;

import com.example.example4springboot.entity.Widget;
import com.example.example4springboot.mapper.WidgetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;


/**
 * JUnit 5 定义了一个扩展接口，通过该接口，类可以在执行生命周期的各个阶段与 JUnit 测试集成。
 * 我们可以通过向测试类添加@ExtendWith注释并指定要加载的扩展类来启用扩展。
 * 然后，扩展可以实现各种回调接口，这些接口将在整个测试生命周期中调用：
 * 在所有测试运行之前、每个测试运行之前、每个测试运行之后以及所有测试运行之后。
 *
 * Spring 定义了一个SpringExtension类，该类订阅 JUnit 5 生命周期,
 * 同时注解会扫描应用程序的主配置类，并加载所有的 Bean（包括依赖的 Bean）到测试上下文中
 *
 * 正常情况下, 我们要使用junit5来测试Spring的话, 那么我们需要使用如下代码
 * @ExtendWith(SpringExtension.class)
 * class MyTests {
 *     // ...
 * }
 *
 *
 * 幸运的是@SpringBootTest注释已经包含 @ExtendWith(SpringExtension.class) 注解，
 * 所以我们只需要包含@SpringBootTest 。
 */
@SpringBootTest // 相当于@ExtendWith(SpringExtension.class)
public class WidgetServiceTest {
    // 自动注入我们需要测试的类
    @Autowired
    private WidgetService service;

    // 自动注入WidgetService依赖的WidgetRepository, 这个WidgetRepository是mockito创建的一个mock对象, 用来模拟各种情况下WidgetService的行为
    @MockBean
    private WidgetMapper widgetMapper;

    @Test
    @DisplayName("Test findById Success")
    void testFindById() {
        // 定义WidgetRepository的行为
        // 在调用他的findById()方法时, 如果参数是1l, 那么返回一个Optional.of(widget)
        Widget widget = new Widget(1L, "Widget Name", "Description", 1);
        doReturn(Optional.of(widget)).when(widgetMapper).findById(1L);

        // 执行调用
        Optional<Widget> returnedWidget = service.findById(1L);

        // 断言
        Assertions.assertTrue(returnedWidget.isPresent(), "Widget was not found");
        Assertions.assertSame(returnedWidget.get(), widget, "The widget returned was not the same as the mock");
    }

    @Test
    @DisplayName("Test findById Not Found")
    void testFindByIdNotFound() {
        // 定义在调用repository.findById(1l)的时候, 返回一个Optional.empty()
        doReturn(Optional.empty()).when(widgetMapper).findById(1l);

        // 执行调用
        Optional<Widget> returnedWidget = service.findById(1l);

        // 断言
        Assertions.assertFalse(returnedWidget.isPresent(), "Widget should not be found");
    }

    @Test
    @DisplayName("Test findAll")
    void testFindAll() {
        // Setup our mock repository
        Widget widget1 = new Widget(1l, "Widget Name", "Description", 1);
        Widget widget2 = new Widget(2l, "Widget 2 Name", "Description 2", 4);
        doReturn(Arrays.asList(widget1, widget2)).when(widgetMapper).findAll();

        // Execute the service call
        List<Widget> widgets = service.findAll();

        // Assert the response
        Assertions.assertEquals(2, widgets.size(), "findAll should return 2 widgets");
    }

    @Test
    @DisplayName("Test save widget")
    void testSave() {
        // Setup our mock repository
        Widget widget = new Widget(1L, "Widget Name", "Description", 1);
        doReturn(1).when(widgetMapper).save(any());

        // Execute the service call
        Widget returnedWidget = service.save(widget);

        // Assert the response
        Assertions.assertNotNull(returnedWidget, "The saved widget should not be null");
        Assertions.assertEquals(2, returnedWidget.getVersion(), "The version should be incremented");
    }
}