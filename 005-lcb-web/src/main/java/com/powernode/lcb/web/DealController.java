package com.powernode.lcb.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.powernode.lcb.common.constant.Constants;
import com.powernode.lcb.common.constant.ResponseResult;
import com.powernode.lcb.model.User;
import com.powernode.lcb.service.DealService;
import com.powernode.lcb.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class DealController {

    @Reference(interfaceClass = DealService.class,version = "1.0.0",timeout = 15000)
    private DealService dealService;
    @Reference(interfaceClass = UserService.class,version = "1.0.0",timeout = 15000)
    private UserService userService;

    @RequestMapping("/loan/page/toRecharge")
    public String toRecharge(){
        return "toRecharge";
    }

    @RequestMapping("/loan/page/invest")
    @ResponseBody
    public ResponseResult invest(int loanInfoId, double bidMoney, HttpSession session){
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        ResponseResult responseResult = new ResponseResult();
        int result = dealService.invest(loanInfoId, bidMoney, user);
        if (result > 0){
            user = userService.queryByPhone(user.getPhone());
            session.setAttribute(Constants.SESSION_USER,user);
            responseResult.setCode(Constants.STATUS_OK);
            responseResult.setMsg("成功投资");
        }else {
            responseResult.setCode(Constants.STATUS_ERROR);
            responseResult.setMsg("系统繁忙，请稍后重试");
        }
        return responseResult;
    }
}
