package entity;


import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;


public class FeginInterception implements RequestInterceptor {


    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Enumeration<String> headerNames = requestAttributes.getRequest().getHeaderNames();
        while(headerNames.hasMoreElements()){
            String headerKey = headerNames.nextElement();
            String headerValue = requestAttributes.getRequest().getHeader(headerKey);
            System.out.println(headerValue);
            requestTemplate.header(headerKey,headerValue);
        }
    }
}
