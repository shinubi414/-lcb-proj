package com.powernode.lcb.service;

import com.powernode.lcb.model.LoanInfo;

import java.util.List;

public interface LoanInfoService {

    double queryHistoryAvgRate();

    List<LoanInfo> queryByProductTypeId(int productTypeId, int startIndex, int pageSize);

    int queryRows(int productTypeId);

    LoanInfo queryByPrimaryKey(Integer id);
}
