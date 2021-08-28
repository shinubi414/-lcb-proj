package com.powernode.lcb.service;

import com.powernode.lcb.model.User;

public interface DealService {

    int invest(int loanInfoId, double bidMoney, User user);

}
