package zkconfigcenter.core;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class ConfigController {

    @Value("${name}")
    private String name;

    @Value("${job}")
    private String job;

    @GetMapping
    public String get(){
        return name+":"+job;
    }
}