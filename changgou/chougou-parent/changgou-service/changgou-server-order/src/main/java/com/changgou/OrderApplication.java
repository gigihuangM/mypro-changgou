package com.changgou;

//import com.changgou.order.config.TokenDecode;
import entity.FeginInterception;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = {"com.changgou.order.dao"})
@EnableFeignClients(basePackages = {"com.changgou.goods.feign"})
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run( OrderApplication.class);
    }

    @Bean
    public FeginInterception feginInterception(){
        return new FeginInterception();

    }



}