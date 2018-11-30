package com.leyou.auth.controller;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.properties.JWTProperties;
import com.leyou.auth.service.AuthService;
import com.leyou.common.utils.CookieUtils;
import com.leyou.common.utils.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@EnableConfigurationProperties(JWTProperties.class)
public class AuthController {

    @Autowired
    private JWTProperties properties;

    @Autowired
    private AuthService authService;

    @PostMapping("accredit")
    public ResponseEntity<Void> auth(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        String token = authService.auth(username,password);
        if (StringUtils.isBlank(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        // 将token写入cookie,并指定httpOnly为true，防止通过JS获取和修改
        CookieUtils.setCookie(request, response, this.properties.getCookieName(), token, this.properties.getCookieMaxAge(),true);
        return ResponseEntity.ok().build();
    }

    @GetMapping("verify")
    public ResponseEntity<UserInfo> verify(@CookieValue("ly_token") String token,HttpServletResponse response,HttpServletRequest request){
        UserInfo userInfo = JsonUtils.parse(token, UserInfo.class);
        CookieUtils.setCookie(request,response,properties.getCookieName(),token,properties.getCookieMaxAge(),true);
        return ResponseEntity.ok(userInfo);
    }
}
