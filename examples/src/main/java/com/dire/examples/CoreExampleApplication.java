package com.dire.examples;

import com.dire.core.context.response.RestResult;
import com.dire.util.BatchWrapper;
import com.dire.util.Target;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.ManagedBean;
import java.util.List;

@SpringBootApplication(scanBasePackages = "com.dire")
@ManagedBean("com.dire")
@RestController
public class CoreExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoreExampleApplication.class, args);
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
