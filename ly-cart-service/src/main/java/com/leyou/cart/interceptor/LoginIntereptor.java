package com.leyou.cart.interceptor;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.cart.properties.JwtProperties;
import com.leyou.common.utils.CookieUtils;
import com.leyou.common.utils.JsonUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginIntereptor implements HandlerInterceptor {

    private JwtProperties jwtProperties;

    public LoginIntereptor(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    //创建一个线程，存放登录用户信息
    private static final ThreadLocal<UserInfo> thread = new ThreadLocal();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            //获取用户的tonken
            String value = CookieUtils.getCookieValue(request, jwtProperties.getCookieName(),"UTF-8");
            //解析成对象
            UserInfo user = JsonUtils.parse(value, UserInfo.class);
            //放到线程中
            thread.set(user);
            return true;
        }catch (Exception e){
            //抛出异常，说明未登录
            response.setStatus(401);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        thread.remove();
    }

    public static UserInfo getLoginUser() {
        return thread.get();
    }
}
