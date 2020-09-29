package com.changgou.oauth;

import com.alibaba.fastjson.JSON;
import org.bouncycastle.jcajce.provider.asymmetric.RSA;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

public class CreateJwtTestDemo {

    @Test
    public void testCreateToken(){
        //加载证书 读取数据 获取私钥
        //读取类路径下的文件
        ClassPathResource  resource=new ClassPathResource("changgou68.jks");

        KeyStoreKeyFactory keyStoreKeyFactory=new KeyStoreKeyFactory(resource,"changgou68".toCharArray());

        //获取证书中的以对秘钥
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair("changgou68","changgou68".toCharArray());
        //获取私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        //创建令牌 私钥加盐
        Map<String,Object> payload=new HashMap<>();
        payload.put("nikename","tomcat");
        payload.put("address","cq");
        payload.put("role","admin.user");

        Jwt jwt = JwtHelper.encode(JSON.toJSONString(payload), new RsaSigner(privateKey));

        String token = jwt.getEncoded();
        System.out.println(token);
    }

    @Test
    public void testParseToken(){
        String token="eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhZGRyZXNzIjoiY3EiLCJyb2xlIjoiYWRtaW4udXNlciIsIm5pa2VuYW1lIjoidG9tY2F0In0.U4kPeToxOcrm1vnSlXKGjE0GgutG2wjLtWmwo1cjYGk8vjHLGqjAzH22R5xIKu_5dbZ5YQPck6UknhtCzuJMPX5AMfXFagygopvle6P2_3FuwBQ0RAC_7nxgcnnK26LIheB9MTqoCgVWBvpykcYcLgcKXrNdthz4PdRQkeXtkwTMXJprWtxO9bOzRZ-FjCf6-knjLr8BkIJKR2uc7Vas9QOzreT_Nw1ZS91O2v1-gzZdE29SGGpRuRTX8VkMWSCb0uSMlpVBR7osw3k3CPXjcBrZCCopY4HOmhcYhnX-xrYZeZhgMm_pjMMU-aV04de3yPJPcS_3380FHDr4BuxSYA";
        String publickey="-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuWECpDzCXaK4Zs+ZAowf6wF6gUmiE+Yg3MCp05xQ8NLBUadkbEdas0PvYbSxYSwXIROqfS45dRsNOG35ieSMDwxXsNBD12k495lnGj+Rz4AG9D35kzi2Rb+3okP4b3uEWlRS3PYSjpgilEHseEPEMSxDwHZTrRVuJ9gGoYA2vw82DpeeHzriAoSMvq3voHv8bxPp4kH7i+JdYmikG1wntQhsXYiXGLzINx0LeYLrVYWEFbwf5wvkCBysbFQcSyBIqpt7jetX2NvSw3pfOEYHirOeWgzv3MKOCbR7/HTKM/dya83fw5RKPDUiWtHOpiXoVn25kR4WT0p+trXRZbAHAQIDAQAB-----END PUBLIC KEY-----";
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publickey));
        String claims = jwt.getClaims();
        System.out.println(claims);
    }

}
