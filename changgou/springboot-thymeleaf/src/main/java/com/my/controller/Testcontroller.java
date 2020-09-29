package com.my.controller;

import com.my.domain.User;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequestMapping(value = "test")
public class Testcontroller {

    @GetMapping(value = "hello")
    public String hello(Model model){
        model.addAttribute("message","hello thymeleaf");

        List<User> users = new ArrayList<User>();
        users.add(new User(1,"张三","深圳"));
        users.add(new User(2,"李四","北京"));
        users.add(new User(3,"王五","武汉"));
        model.addAttribute("users",users);

        Map<String,Object> dataMap = new HashMap<String,Object>();
        dataMap.put("No","123");
        dataMap.put("address","深圳");
        model.addAttribute("dataMap",dataMap);

        model.addAttribute("now",new Date());
        model.addAttribute("age",18);


        return "demo1";
    }
}
