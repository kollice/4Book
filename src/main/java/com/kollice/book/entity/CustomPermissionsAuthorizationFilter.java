package com.kollice.book.entity;

import com.kollice.book.service.AuthorizationService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by 00259 on 2016/10/12.
 */
public class CustomPermissionsAuthorizationFilter extends PermissionsAuthorizationFilter {
    private static final Logger log = LoggerFactory.getLogger(CustomPermissionsAuthorizationFilter.class);

    @Autowired
    AuthorizationService loginService;

    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
        boolean isPermitted = true;
        Subject user = SecurityUtils.getSubject();

        if(!SecurityUtils.getSubject().isAuthenticated()){
            HttpServletResponse res = (HttpServletResponse)response;
            HttpServletRequest req = (HttpServletRequest)request;
            String url = req.getScheme() + "://" + req.getServerName() + ":"
                    + req.getServerPort() + req.getContextPath();
            res.sendRedirect(url);
        }

        //admin特殊处理 放行所有url
        if(user.getPrincipal().equals("admin")){
            return isPermitted;
        }


        HttpServletRequest req = (HttpServletRequest) request;
        Subject subject = getSubject(request, response);
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();

        //去掉上下文路径的url
        uri = uri.replaceFirst(contextPath,"").replaceFirst("/+","");

        //获得所有的配置的菜单路径 如果请求的路径存在于配置的路径 则进行匹配 如果不是则放行
        List<String> urlList = loginService.getAllPermissionUrl();

        for(String url:urlList){
            if(url.contains(uri)){
                isPermitted = false;
            }
        }

        if(!isPermitted){
            isPermitted = subject.isPermitted(uri);
        }
        return isPermitted;
    }
}
