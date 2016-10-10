package com.kollice.book.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by 00259 on 2016/10/9.
 */
@RestController
public class IndexController {
    @RequestMapping(value = "/")
    ModelAndView index() throws Exception {
        return new ModelAndView("login");
    }
}