package com.changgou.oauth.util;

import com.alibaba.fastjson.JSON;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

public class AdminToken {

    public  static String adminToken(){
        ClassPathResource resource=new ClassPathResource("changgou68.jks");

        KeyStoreKeyFactory keyStoreKeyFactory=new KeyStoreKeyFactory(resource,"changgou68".toCharArray());

        //获取证书中的以对秘钥
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair("changgou68","changgou68".toCharArray());
        //获取私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        //创建令牌 私钥加盐
        Map<String,Object> payload=new HashMap<>();
        payload.put("nikename","tomcat");
        payload.put("address","cq");
        payload.put("authorities",new String[]{"admin","oauth"});

        Jwt jwt = JwtHelper.encode(JSON.toJSONString(payload), new RsaSigner(privateKey));

        String token = jwt.getEncoded();

        return token;
    }
}
