package zkconfigcenter.core;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ConfigurationPropertiesRebinder implements ApplicationListener<EnvironmentChangeEvent> {
    private ConfigurationPropertiesBeans beans;
    private Environment environment;
    public ConfigurationPropertiesRebinder(ConfigurationPropertiesBeans beans,Environment environment) {
        this.beans = beans;
        this.environment=environment;
    }

    @Override
    public void onApplicationEvent(EnvironmentChangeEvent event) {
        rebind();
    }
    public void rebind(){
        this.beans.getFieldMapper().forEach((k,v)->{
            v.forEach(f->f.resetValue(environment));
        });
    }
}