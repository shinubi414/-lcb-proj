package com.powernode.lcb.interceptor;

import com.powernode.lcb.common.constant.Constants;
import com.powernode.lcb.model.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class MyInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = (User) request.getSession().getAttribute(Constants.SESSION_USER);
        if (user == null){
            response.sendRedirect("/lcb/loan/page/login");
            return false;
        }
        return true;
    }
}
