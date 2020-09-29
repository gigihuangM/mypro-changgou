package com.changgou.order.controller;

import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import entity.Result;
import entity.StatusCode;
import entity.TokenDecode;
import io.swagger.models.auth.In;
import org.omg.PortableInterceptor.Interceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.security.util.SecurityConstants;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/cart")
public class CartContronller {

    @Autowired
    private CartService cartService;


    @GetMapping(value = "/list")
    public Result<List<OrderItem>> list(){
//        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
////        details.getTokenValue();
        Map<String, String> userInfo = TokenDecode.getUserInfo();
        System.out.println(userInfo);
        String username = userInfo.get("username");
       // String username="itheima";
        List<OrderItem> orderItems=cartService.list(username);
        return new Result<List<OrderItem>>(true,StatusCode.OK,"购物车列表查询成功",orderItems);
    }



    @GetMapping(value = "/add")
    public Result add(Integer num,Long id){
        Map<String, String> userInfo = TokenDecode.getUserInfo();
        System.out.println(userInfo);
        String username = userInfo.get("username");
        cartService.add(num,id,username);
        return new Result(true, StatusCode.OK,"添加成功呢");
    }


}
