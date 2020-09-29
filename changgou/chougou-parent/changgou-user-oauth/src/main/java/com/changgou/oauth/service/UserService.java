package com.changgou.oauth.service;

import com.changgou.oauth.util.AuthToken;

public interface UserService {
    /**
     * 登录操作
     * @param username
     * @param password
     * @param clientId
     * @param clientSecret
     * @param grant_type
     */
    AuthToken login(String username, String password, String clientId, String clientSecret, String grant_type)throws Exception;
}
