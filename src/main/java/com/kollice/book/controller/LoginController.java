package com.kollice.book.controller;

import com.kollice.book.utils.CryptUtil;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by 白建业 on 2016/10/8.
 */
@RestController
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "unauthorized")
    ModelAndView unauthorized() throws Exception {
        return new ModelAndView("unauthorized");
    }


    @RequestMapping(value = "login",method = RequestMethod.POST)
    ModelAndView login(@RequestParam String username,@RequestParam String password) throws Exception {
        boolean flag = true;
        String passwordtemp = CryptUtil.encrypt(password);
        UsernamePasswordToken token = new UsernamePasswordToken(username, passwordtemp);
        token.setRememberMe(true);
        log.info("为了验证登录用户而封装的token为" + ReflectionToStringBuilder.toString(token, ToStringStyle.MULTI_LINE_STYLE));
        //获取当前的Subject
        Subject currentUser = SecurityUtils.getSubject();
        try {
            currentUser.login(token);
        } catch (UnknownAccountException uae) {
            log.info("未知账户");
            flag = false;
        } catch (IncorrectCredentialsException ice) {
            log.info("密码不正确");
            flag = false;
        } catch (LockedAccountException lae) {
            log.info("账户已锁定");
            flag = false;
        } catch (ExcessiveAttemptsException eae) {
            log.info("用户名或密码错误次数过多");
        } catch (AuthenticationException ae) {
            //通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
            ae.printStackTrace();
            flag = false;
        }
        //验证是否登录成功
        if (currentUser.isAuthenticated() && flag) {
            return new ModelAndView("main");
        } else {
            token.clear();
        }
        return new ModelAndView("login");
    }

    @RequestMapping(value = "/manage/test")
    ModelAndView test() throws Exception {
        return new ModelAndView("test");
    }

    @RequestMapping(value = "/manage/gg")
    ModelAndView gg() throws Exception {
        return new ModelAndView("gg");
    }

    @RequestMapping(value = "logout",method = RequestMethod.POST)
    ModelAndView logout() throws Exception {
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
        return new ModelAndView("login");
    }

}
