package com.claw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * 程序员面试知识答题系统主启动类
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
public class ClawApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClawApplication.class, args);
    }
}