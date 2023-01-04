package com.dire.examples;

import com.dire.core.context.response.RestResult;
import com.dire.util.BatchWrapper;
import com.dire.util.Target;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SpringBootApplication
@EnableWebSecurity(debug = true)
@MapperScan("com.dire.guard.mapper")
@RestController
public class CoreExampleApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(CoreExampleApplication.class, args);
    }

    @GetMapping("")
    public RestResult<String> test(@RequestParam Target<String> test) {
        return RestResult.success(test.getTarget());
    }

    @PostMapping("")
    public RestResult<List<String>> test1(@RequestBody BatchWrapper<String> wrapper) {
        return RestResult.success(wrapper.getTargets());
    }
}
