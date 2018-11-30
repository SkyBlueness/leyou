package com.leyou.gateway.filter;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.common.utils.CookieUtils;
import com.leyou.common.utils.JsonUtils;
import com.leyou.gateway.properties.FilterProperties;
import com.leyou.gateway.properties.JwtProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
public class AuthFilter extends ZuulFilter {

    @Autowired
    private JwtProperties properties;

    @Autowired
    private FilterProperties filterProperties;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 5;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        String uri = request.getRequestURI();
        return !isAllowPath(uri);
    }

    private boolean isAllowPath(String uri) {
        boolean flag = false;
        List<String> allowPaths = filterProperties.getAllowPaths();
        for (String allowPath:allowPaths){
            if (uri.startsWith(allowPath)){
                flag = true;
                break;
            }
        }
        return flag;
    }

    @Override
    public Object run() throws ZuulException {
        //获取上下文
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        try {
            //校验通过就放行
            String cookieValue = CookieUtils.getCookieValue(request, properties.getCookieName());
            UserInfo userInfo = JsonUtils.parse(cookieValue, UserInfo.class);
        }catch (Exception e){
            //校验没通过就返回403
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(403);
        }
        return null;
    }
}
