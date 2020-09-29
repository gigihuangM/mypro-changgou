package com.changgou.oauth.service.impl;


import com.changgou.oauth.service.UserService;
import com.changgou.oauth.util.AuthToken;
import com.sun.istack.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;
    /**
     * 登录操作
     * @param username
     * @param password
     * @param clientId
     * @param clientSecret
     * @param grant_type
     */
    @Override
    public AuthToken login(String username, String password, String clientId, String clientSecret, String grant_type)throws Exception {

        //获取指定的服务
        ServiceInstance serviceInstance = loadBalancerClient.choose("user-auth");
        String url=serviceInstance.getUri()+"/oauth/token";

        MultiValueMap<String,String> parameterMap=new LinkedMultiValueMap() ;
        parameterMap.add("username",username);
        parameterMap.add("password",password);
        parameterMap.add("grant_type",grant_type);

        String Authorization="Basic "+new String(Base64.getEncoder().encode((clientId+":"+clientSecret).getBytes()),"UTF-8");
        MultiValueMap headerMap=new LinkedMultiValueMap() ;
        headerMap.add("Authorization",Authorization);

        HttpEntity httpEntity=new HttpEntity(parameterMap,headerMap);

        ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Map.class);
        Map<String,String> map = responseEntity.getBody();

        AuthToken authorization=new AuthToken();
        authorization.setAccessToken(map.get("access_token"));
        authorization.setRefreshToken(map.get("refresh_token"));
        authorization.setJti(map.get("jti"));
       return authorization;
    }
}
