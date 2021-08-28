package com.powernode.lcb.service;

import com.powernode.lcb.model.FinanceAccount;

public interface FinanceAccountService {

    int returnPayment();

    FinanceAccount queryByUId(int uId);

    int updateById(FinanceAccount financeAccount);
}
