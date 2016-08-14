package org.seckill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by Administrator on 2016/8/6.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "org.seckill.service,org.seckill.web,org.seckill.dao")
@ImportResource("classpath:spring-web.xml")
public class AppMain {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(AppMain.class, args);
    }
}