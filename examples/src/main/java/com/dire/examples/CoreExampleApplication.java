package com.dire.examples;

import com.dire.core.context.response.RestResult;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class CoreExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoreExampleApplication.class, args);
    }

    @GetMapping("")
    public RestResult<String> test(String test) {
        return RestResult.success(test);
    }
}
