package com.powernode.lcb.web;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import com.alibaba.dubbo.config.annotation.Reference;
import com.powernode.lcb.common.constant.Constants;
import com.powernode.lcb.common.constant.ResponseResult;
import com.powernode.lcb.model.BidInfo;
import com.powernode.lcb.model.IncomeRecord;
import com.powernode.lcb.model.RechargeRecord;
import com.powernode.lcb.model.User;
import com.powernode.lcb.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Controller
public class UserController {

    @Reference(interfaceClass = UserService.class,version = "1.0.0",timeout = 15000)
    private UserService userService;

    @Reference(interfaceClass = BidInfoService.class,version = "1.0.0",timeout = 15000)
    private BidInfoService bidInfoService;

    @Reference(interfaceClass = IncomeRecordService.class,version = "1.0.0",timeout = 15000)
    private IncomeRecordService incomeRecordService;

    @Reference(interfaceClass = RechargeRecordService.class,version = "1.0.0",timeout = 15000)
    private RechargeRecordService rechargeRecordService;
    @Reference(interfaceClass = LoanInfoService.class,version = "1.0.0",timeout = 15000)
    private LoanInfoService loanInfoService;

    @RequestMapping("/loan/page/login")
    public String login(Model model){
        long count = userService.queryUserCount();
        double money = bidInfoService.queryBidMoney();
        double rate = loanInfoService.queryHistoryAvgRate();

        model.addAttribute("rate",rate);
        model.addAttribute("money",money);
        model.addAttribute("count",count);
        return "login";
    }

    @RequestMapping("/loan/page/doLogin")
    @ResponseBody
    public String doLogin(String phone, String password,String captcha,HttpServletRequest request){
        String code = (String) request.getSession().getAttribute("code");
        if (code.equals(captcha)){
            User user = userService.login(phone, password);
            if (user != null){
                User resultUser = userService.queryByPhone(phone);
                request.getSession().setAttribute(Constants.SESSION_USER,resultUser);
                return "";
            }else {
                return "账户或密码错误";
            }
        }else {
            return "输入验证码错误";
        }


    }

    @RequestMapping("/jcaptcha/captcha")
    @ResponseBody
    public String login(HttpServletResponse response, HttpServletRequest request){
        OutputStream out = null;
        try {
            out = response.getOutputStream();// 取得输出流
            //定义图形验证码的长、宽、验证码字符数、干扰线宽度
            ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(300, 100, 4, 4);
            //ShearCaptcha captcha = new ShearCaptcha(200, 100, 4, 4);
            captcha.write(out);
            // 将生成的验证码code放入session中
            request.getSession().setAttribute("code", captcha.getCode());
            out.flush();  // 将缓存中的数据立即强制刷新, 将缓冲区的数据输出到客户端浏览器
            out.close(); // 关闭输出流
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return  "图片获取失败";

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

    @RequestMapping("/checkMessageCode")
    @ResponseBody
    public boolean checkMessageCode(String phone,String code){
        return userService.checkSmsCode(phone,code);
    }

    @RequestMapping("/register")
    @ResponseBody
    public boolean register(User user){
        return userService.addUser(user);
    }

    @RequestMapping("/checkPhone")
    @ResponseBody
    public boolean checkPhone(String phone){
        User user = userService.queryByPhone(phone);
        if (user == null){
            return true;
        }else {
            return false;
        }

    }

    @RequestMapping("/realName")
    public String realName(){
        return "realName";
    }

    @RequestMapping("/doRealName")
    @ResponseBody
    public String doRealName(String phone, String realName, String idCard, String captcha, HttpSession session){
        String code = (String) session.getAttribute("code");
        if (code.equals(captcha)){
            String msg = userService.realName(phone, realName, idCard);
            if ("ok".equals(msg)){
                User user = userService.queryByPhone(phone);
                session.setAttribute(Constants.SESSION_USER,user);
            }
            return msg;
        }else {
            return "验证码错误";
        }
    }

    @RequestMapping("/loan/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/index";
    }

    @RequestMapping("/loan/myCenter")
    public String goMyCenter(Model model,HttpSession session){
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        List<IncomeRecord> incomeRecordList = incomeRecordService.queryByUId(user.getId(),0,5);
        List<BidInfo> bidInfoList = bidInfoService.queryByUId(user.getId(),0,5);
        List<RechargeRecord> rechargeRecordList = rechargeRecordService.queryByUId(user.getId(),0,5);

        model.addAttribute("incomeRecordList",incomeRecordList);
        model.addAttribute("bidInfoList",bidInfoList);
        model.addAttribute("rechargeRecordList",rechargeRecordList);

        return "myCenter";
    }

    @RequestMapping("/checkLogin")
    @ResponseBody
    public ResponseResult checkLoginStatus(double bidMoney,HttpSession session){
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        ResponseResult responseResult = new ResponseResult();
        if (user == null){
            responseResult.setCode(Constants.STATUS_ERROR_LOGIN);
            responseResult.setMsg("未登录，请登录后进行投资");
            return responseResult;
        }else {
            String name = user.getName();
            if (name == null){
                responseResult.setCode(Constants.STATUS_ERROR_REALNAME);
                responseResult.setMsg("未实名，请实名后进行投资");
                return responseResult;
            }else {
                double accountMoney = user.getMoney();
                if (bidMoney > accountMoney){
                    responseResult.setCode(Constants.STATUS_ERROR_MONRY);
                    responseResult.setMsg("账户余额不足");
                    return responseResult;
                }else {
                    responseResult.setCode(Constants.STATUS_OK);
                    return responseResult;
                }
            }
        }
    }





}
