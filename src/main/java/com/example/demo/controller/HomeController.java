package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/***
 *
 * Description: 
 *
 * <p>@author: Ives.l
 * <p>@date: 2018/11/27
 * <p>@time: 18:13
 *
 */
@Controller
public class HomeController {

    @RequestMapping(value = {"/", "/upload"})
    public String upload() {
        return "/upload";
    }

    @RequestMapping(value = "/uploads")
    public String uploads() {
        return "/uploads";
    }
}
