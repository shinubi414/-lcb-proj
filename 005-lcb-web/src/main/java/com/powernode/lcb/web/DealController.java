package com.powernode.lcb.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DealController {

    @RequestMapping("/loan/page/toRecharge")
    public String toRecharge(){
        return "toRecharge";
    }
}
