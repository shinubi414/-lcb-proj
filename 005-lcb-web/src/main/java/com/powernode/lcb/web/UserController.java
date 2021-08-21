package com.powernode.lcb.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {

    @RequestMapping("/loan/page/login")
    public String login(HttpServletResponse response){

        return "login";
    }

    @RequestMapping("/loan/page/register")
    public String registry(){
        return "register";
    }

}
