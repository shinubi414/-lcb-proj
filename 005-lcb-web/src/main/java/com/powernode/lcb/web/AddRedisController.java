package com.powernode.lcb.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.powernode.lcb.service.BidInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AddRedisController {

    @Reference(interfaceClass = BidInfoService.class,version = "1.0.0",timeout = 15000)
    BidInfoService bidInfoService;

    @RequestMapping("/addInvestRecord")
    @ResponseBody
    public String addInvestRecord(){
        long add = bidInfoService.addInvestRecordRedis();
        if (add > 0){
            return "success";
        }else {
            return "false";
        }

    }


}
