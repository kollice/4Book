package com.kollice.book.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by 白建业 on 2016/10/9.
 */
@RestController
public class IndexController {
    @RequestMapping(value = "/index")
    ModelAndView manage() throws Exception {
        return new ModelAndView("login");
    }

    @RequestMapping(value = "/")
    ModelAndView index() throws Exception {
        return new ModelAndView("index");
    }
}
