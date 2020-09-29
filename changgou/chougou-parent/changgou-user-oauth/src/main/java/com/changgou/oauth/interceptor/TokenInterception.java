package com.changgou.oauth.interceptor;

import com.changgou.oauth.util.AdminToken;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenInterception implements RequestInterceptor {


    public void apply(RequestTemplate template){
        String token = AdminToken.adminToken();
        template.header("Authorization","bearer "+token);
    }

}
