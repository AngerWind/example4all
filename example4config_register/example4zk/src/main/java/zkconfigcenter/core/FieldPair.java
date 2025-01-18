package zkconfigcenter.core;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.PropertyPlaceholderHelper;

public class FieldPair {
    private static PropertyPlaceholderHelper propertyPlaceholderHelper=new PropertyPlaceholderHelper("${","}",":",true);
    private Object bean;
    private Field field;
    private String value;

    public FieldPair(Object bean, Field field, String value) {
        this.bean = bean;
        this.field = field;
        this.value = value;
    }

    public void resetValue(Environment environment){
        boolean access=field.isAccessible();
        if(!access){
            field.setAccessible(true);
        }
        //从新从environment中将占位符替换为新的值
        String resetValue=propertyPlaceholderHelper.replacePlaceholders(value,((ConfigurableEnvironment) environment)::getProperty);
        try {
           //通过反射更新
            field.set(bean,resetValue);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}