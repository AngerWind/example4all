package zkconfigcenter.core;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class ConfigurationPropertiesBeans implements BeanPostProcessor {

    private Map<String,List<FieldPair>> fieldMapper=new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
        throws BeansException {
        Class clz=bean.getClass();
        if(clz.isAnnotationPresent(RefreshScope.class)){ //如果某个bean声明了RefreshScope注解，说明需要进行动态更新
            for(Field field:clz.getDeclaredFields()){
                Value value=field.getAnnotation(Value.class);
                List<String> keyList=getPropertyKey(value.value(),0);
                for(String key:keyList){
                    //使用List<FieldPair>存储的目的是，如果在多个bean中存在相同的key，则全部进行替换
                    fieldMapper.computeIfAbsent(key,(k)->new ArrayList()).add(new FieldPair(bean,field,value.value()));
                }
            }
        }
        return bean;
    }
    //获取key信息，也就是${value}中解析出value这个属性
    private List<String> getPropertyKey(String value,int begin){
        int start=value.indexOf("${",begin)+2;
        if(start<2){
            return new ArrayList<>();
        }
        int middle=value.indexOf(":",start);
        int end=value.indexOf("}",start);
        String key;
        if(middle>0&&middle<end){
            key=value.substring(start,middle);
        }else{
            key=value.substring(start,end);
        }
        //如果是这种用法，就需要递归，@Value("${swagger2.host:127.0.0.1:${server.port:8080}}")
        List<String> keys=getPropertyKey(value,end);
        keys.add(key);
        return keys;
    }

    public Map<String, List<FieldPair>> getFieldMapper() {
        return fieldMapper;
    }
}