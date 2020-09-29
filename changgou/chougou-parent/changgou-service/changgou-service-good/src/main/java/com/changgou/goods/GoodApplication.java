package com.changgou.goods;

import entity.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = {"com.changgou.goods.dao"})
public class GoodApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoodApplication.class,args);
    }
    @Bean
    public IdWorker idWorker(){
        return new IdWorker(0,0);
    }
}
