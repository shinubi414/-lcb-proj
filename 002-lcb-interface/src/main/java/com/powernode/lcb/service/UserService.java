package com.powernode.lcb.service;

import com.powernode.lcb.model.User;

public interface UserService {

    long queryUserCount();

    String sendSmsCode(String phone);

    boolean checkSmsCode(String phone,String code);

    boolean addUser(User user);

    boolean queryByPhone(String phone);

    User login(String phone,String password);

    String realName(String phone,String realName,String idCard);

}
