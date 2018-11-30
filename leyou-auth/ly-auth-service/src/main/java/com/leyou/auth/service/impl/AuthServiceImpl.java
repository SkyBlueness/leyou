package com.leyou.auth.service.impl;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.service.AuthService;
import com.leyou.common.utils.JsonUtils;
import com.leyou.user.api.UserApi;
import com.leyou.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserApi userApi;

    @Override
    public String auth(String username, String password) {
        User user = userApi.queryUser(username, password);
        if (user == null){
            return null;
        }
        //如果有就生成token
        UserInfo userInfo = new UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        String token = JsonUtils.serialize(userInfo);
        return token;
    }
}
