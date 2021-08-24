package com.powernode.lcb.service;

import com.powernode.lcb.model.User;
import com.sun.deploy.net.HttpRequest;

public interface UserService {

    long queryUserCount();

    String sendSmsCode(String phone);

    boolean checkSmsCode(String phone,String code);

    boolean addUser(User user);

    User queryByPhone(String phone);

    User login(String phone,String password);

    String realName(String phone, String realName, String idCard);

}
