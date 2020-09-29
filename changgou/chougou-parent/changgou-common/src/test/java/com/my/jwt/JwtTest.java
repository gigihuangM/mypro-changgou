package com.my.jwt;

import io.jsonwebtoken.*;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {

    @Test
    public void testCreateJwt(){
        JwtBuilder builder= Jwts.builder();
        builder.setIssuer("黑马训练营");
        builder.setIssuedAt(new Date());
        builder.setExpiration(new Date(System.currentTimeMillis()+3600000));
        builder.setSubject("JWT令牌测试");

        Map<String,Object> maps=new HashMap<>();
        maps.put("company","黑马训练营");
        maps.put("address","钟南山");
        maps.put("money",3500);

        builder.addClaims(maps);  //添加载荷

        builder.signWith(SignatureAlgorithm.HS256,"itcast");
        String token = builder.compact();
        System.out.println(token);
    }


    /**
     * 令牌解析
     */
    @Test
    public void parseToken(){
        String token="eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiLpu5Hpqazorq3nu4PokKUiLCJpYXQiOjE1OTM5MzExMzUsImV4cCI6MTU5M" +
                "zkzNDczNSwic3ViIjoiSldU5Luk54mM5rWL6K-VIiwiYWRkcmVzcyI6IumSn-WNl-WxsSIsIm1vbmV5IjozNTAwLCJjb21wYW55Ijoi6buR6" +
                "ams6K6t57uD6JClIn0.I3r3hFRZm4AzVk6msy4SaSB1qbGNVK7jaKO9FDZU1Sg";
        Claims clains = Jwts.parser().setSigningKey("itcast").parseClaimsJws(token).getBody();
        System.out.println(clains.toString());
    }

}
