package com.leyou.user.service;

import com.leyou.user.pojo.User;

public interface UserService {
    Boolean checkUser(String data, Integer type);

    Boolean register(User user, String code);

    Boolean sendVerifyCode(String phone);

    User queryUser(String userName, String password);
}
