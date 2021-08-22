package com.powernode.lcb.web;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import com.alibaba.dubbo.config.annotation.Reference;
import com.powernode.lcb.model.User;
import com.powernode.lcb.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Controller
public class UserController {

    @Reference(interfaceClass = UserService.class,version = "1.0.0",timeout = 15000)
    UserService userService;

    @RequestMapping("/loan/page/login")
    public String login(){
        return "login";
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
            //图形验证码写出，可以写出到文件，也可以写出到流
//            captcha.write("/Users/sunww/Desktop/shear.png");
            captcha.write(out);
            //验证图形验证码的有效性，返回boolean值
            boolean checkPass = captcha.verify(captcha.getCode());
            // 将生成的验证码code放入sessoin中
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
