package com.powernode.lcb.service;

public interface UserService {

    long queryUserCount();

    String sendSmsCode(String phone);
}
