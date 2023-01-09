package com.dire.examples;

import com.dire.core.context.response.RestResult;
import com.dire.util.BatchWrapper;
import com.dire.util.Target;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SpringBootApplication
@EnableWebSecurity(debug = true)
@MapperScan("com.dire.guard.mapper")
@RestController
public class CoreExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoreExampleApplication.class, args);
    }

//    @Bean
//    public RedisTemplate<Object, Object> redisTemplate() {
//        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//        return redisTemplate;
//    }

    @GetMapping("")
    public RestResult<String> test(@RequestParam Target<String> test) {
        return RestResult.success(test.getTarget());
    }

    @PostMapping("")
    public RestResult<List<String>> test1(@RequestBody BatchWrapper<String> wrapper) {
        return RestResult.success(wrapper.getTargets());
    }
}
