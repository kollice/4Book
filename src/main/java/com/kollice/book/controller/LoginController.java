package com.kollice.book.controller;

import com.kollice.book.dao.PersonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by 00259 on 2016/10/8.
 */
@RestController
public class LoginController {
    @Autowired
    PersonDao personDao;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    ModelAndView login() throws Exception {

//        return "hello!\nMy name is " + authorSettings.getName() + ".\nMy email is " + authorSettings.getEmail();
        return new ModelAndView("main");
    }

}
