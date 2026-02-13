package com.opencode;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.opencode.mapper")
public class OpencodeApplication {
    public static void main(String[] args) {
        SpringApplication.run(OpencodeApplication.class, args);
    }
}
