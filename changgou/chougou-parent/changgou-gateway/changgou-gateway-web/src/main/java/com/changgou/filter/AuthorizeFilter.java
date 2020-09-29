package com.changgou.filter;

import com.changgou.util.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 全局过滤器
 * 实现用户全局检验
 */
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {

    private static final String AUTHORIZE_TOKEN="Authorization";

    /**
     * 全局校验
     * @param exchange
     * @param chain
     * @return
     */
        @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            //获取令牌信息 1 在参数中 2在头文件中 3在Cookie中
            String token = request.getHeaders().getFirst(AUTHORIZE_TOKEN);

            boolean hasToken=true;

            if(StringUtils.isEmpty(token)){
                token=request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
                hasToken=false;
            }

            if(StringUtils.isEmpty(token)){
                HttpCookie httpCookie = request.getCookies().getFirst(AUTHORIZE_TOKEN);
                if(httpCookie!=null){
                    token=httpCookie.getValue();
                }
            }

            //如果没有令牌 ，则拦截
            if(StringUtils.isEmpty(token)){
                //设置没有权限的状态码 401
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                //响应空数据
                return response.setComplete();
            }
            //如果有令牌，则检验令牌是否有效
//            try {
//              //  JwtUtil.parseJWT(token);
//                //令牌是否为空 不为空放在同文件中
//
//            }catch (Exception e){
//                //无效令牌拦截
//                response.setStatusCode(HttpStatus.UNAUTHORIZED);
//                return response.setComplete();
//            }
            if (StringUtils.isEmpty(token)) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }else{
                if(!hasToken){
                //判断是会都有前缀 如果没有则添加qianzhui
                if (!token.startsWith("bearer") && !token.startsWith("Bearer")){
                    token="bearer "+token;
                }
                    request.mutate().header(AUTHORIZE_TOKEN,token);
            }
            }

            return chain.filter(exchange);
    }
    /**
     * 排序
     * @return
     */

    @Override
    public int getOrder() {
        return 0;
    }
}
