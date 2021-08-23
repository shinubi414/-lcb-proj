package com.powernode.lcb.web;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import com.alibaba.dubbo.config.annotation.Reference;
import com.powernode.lcb.common.constant.Constants;
import com.powernode.lcb.model.User;
import com.powernode.lcb.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;

@Controller
public class UserController {

    @Reference(interfaceClass = UserService.class,version = "1.0.0",timeout = 15000)
    UserService userService;

    @RequestMapping("/loan/page/login")
    public String login(HttpServletRequest request){
        return "login";
    }

    @RequestMapping("/loan/page/doLogin")
    @ResponseBody
    public String doLogin(String phone, String password,String captcha,HttpServletRequest request){
        String code = (String) request.getSession().getAttribute("code");
        if (code.equals(captcha)){
            User user = userService.login(phone, password);
            if (user != null){
                request.getSession().setAttribute(Constants.SESSION_USER,user);
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
        return userService.queryByPhone(phone);
    }

    @RequestMapping("/realName")
    public String realName(){
        return "realName";
    }

}
