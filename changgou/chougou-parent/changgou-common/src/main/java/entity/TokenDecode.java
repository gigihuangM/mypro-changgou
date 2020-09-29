package entity;




import com.alibaba.fastjson.JSON;
import io.swagger.models.auth.In;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.stream.Collectors;


public class TokenDecode {

    private static final String PUBLIC_KEY="public.key";

    private static String publickey="";

    public static String getPublicKey(){
        if(!StringUtils.isEmpty(publickey)){
            return publickey;
        }
        Resource resource=  new ClassPathResource(PUBLIC_KEY);
        try{
            InputStreamReader inputStreamReader=new InputStreamReader(resource.getInputStream());
            BufferedReader br=new BufferedReader(inputStreamReader);
            publickey=br.lines().collect(Collectors.joining("\n"));
            return publickey;

        }catch (Exception e){
            return null;
        }
    }

    public static Map<String,String> decodeToken(String token){
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(getPublicKey()));
        String claims = jwt.getClaims();
        return JSON.parseObject(claims,Map.class);
    }

    public static Map<String,String> getUserInfo(){
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails)
                SecurityContextHolder.getContext().getAuthentication().getDetails();
        return decodeToken(details.getTokenValue());

    }


}
