package com.kollice.book.controller;

import com.kollice.book.dao.PersonDao;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * Created by 00259 on 2016/10/8.
 */
@RestController
public class LoginController {
    @Autowired
    PersonDao personDao;

    @RequestMapping(value = "login",method = RequestMethod.POST)
    ModelAndView login(@RequestParam String username,@RequestParam String password) throws Exception {
        System.out.println("--------->here");
//        String username = MapUtils.getString(map,"username");
//        String password = MapUtils.getString(map,"password");

        String username1 = username;
        String password1 = password;

//        return "hello!\nMy name is " + authorSettings.getName() + ".\nMy email is " + authorSettings.getEmail();
        return new ModelAndView("main");
    }

    @RequestMapping(value = "/manage/test")
    ModelAndView test() throws Exception {
        return new ModelAndView("test");
    }

}
