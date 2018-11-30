package com.leyou.user.service.impl;

import com.leyou.common.utils.Md5Utils;
import com.leyou.common.utils.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import com.leyou.user.utils.CodecUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class UserviceImpl implements UserService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private AmqpTemplate amqpTemplate;

    private final static String KEY_PREFIX = "user:code:phone";

    @Autowired
    private UserMapper userMapper;

    @Override
    public Boolean checkUser(String data, Integer type) {
        User user = new User();
        switch (type){
            case 1:
                user.setUsername(data);
                break;
            case 2:
                user.setPhone(data);
        }
            return userMapper.selectCount(user) == 0;
    }

    /**
     * 用户注册
     * @param user
     * @param code
     */
    @Override
    public Boolean register(User user, String code) {
        String key = KEY_PREFIX + user.getPhone();
        String value = stringRedisTemplate.opsForValue().get(key);
        if (!code.equals(value)){
            return false;
        }
        user.setCreated(new Date());
        String salt = CodecUtils.generateSalt();
        String password = CodecUtils.md5Hex(user.getPassword(),salt);
        user.setSalt(salt);
        user.setPassword(password);
        int i = userMapper.insert(user);
        if (1 != i){
            return false;
        }
        //删除redis中的验证码
        stringRedisTemplate.delete(key);
        return true;
    }

    /**
     * 生成验证码
     * @param phone
     * @return
     */
    public Boolean sendVerifyCode(String phone){
        try {
            String code = NumberUtils.generateCode(6);
            Map<String, String> msg = new HashMap<>();
            msg.put("phone",phone);
            msg.put("code",code);
            //发送消息
            amqpTemplate.convertAndSend("leyou.sms.exchange","sms.verify.code",msg);
            //将验证码存入redis中，设置缓存时间5m
            stringRedisTemplate.opsForValue().set(KEY_PREFIX + phone,code,5, TimeUnit.MINUTES);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public User queryUser(String userName, String password) {
        User user = new User();
        user.setUsername(userName);
        User u = userMapper.selectOne(user);
        if (u == null){
            return null;
        }
        //根据password和salt生成加密后的密码
        String encryptPassword = CodecUtils.md5Hex(password,u.getSalt());
        //如果数据库中的密码和生成的密码相等就返回该user
        if (!encryptPassword.equals(u.getPassword())){
            return null;
        }
        return u;
    }
}
