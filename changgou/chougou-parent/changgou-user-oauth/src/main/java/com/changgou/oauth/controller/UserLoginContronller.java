package com.changgou.oauth.controller;

import com.changgou.oauth.service.UserService;
import com.changgou.oauth.util.AuthToken;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserLoginContronller {

    @Value("${auth.clientId}")
    private String clientId;

    @Value("${auth.clientSecret}")
    private String clientSecret;

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/login")
    public Result login(String username,String password)throws Exception{
        String grant_type="password";
        AuthToken authToken = userService.login(username, password, clientId, clientSecret, grant_type);
        if(authToken!=null){
            return new Result(true,StatusCode.OK,"登录成功",authToken);
        }
        return new Result(false, StatusCode.LOGINERROR,"登录失败！");
    }



}
