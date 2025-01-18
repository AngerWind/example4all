package zkconfigcenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/9/3
 * @description
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class ConfigCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigCenterApplication.class, args);
    }
}