package com.changgou.user.feign;

import com.changgou.user.pojo.User;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "user")
@RequestMapping("/user")
public interface UserFeign {

     @GetMapping({"/load/{id}"})
     Result<User> findById(@PathVariable String id);

//     @GetMapping({"/{id}"})
//     Result<User> findById(@PathVariable String id);
}
