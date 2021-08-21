package com.powernode.lcb.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.powernode.lcb.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {

    @Reference(interfaceClass = UserService.class,version = "1.0.0",timeout = 15000)
    UserService userService;

    @RequestMapping("/loan/page/login")
    public String login(HttpServletResponse response){

        return "login";
    }

    @RequestMapping("/loan/page/register")
    public String registry(){

        return "register";
    }

    @RequestMapping("/sendSmsCode")
    @ResponseBody
    public String sendSmsCode(String phone){
        String result = userService.sendSmsCode(phone);
        return result;
    }


}
