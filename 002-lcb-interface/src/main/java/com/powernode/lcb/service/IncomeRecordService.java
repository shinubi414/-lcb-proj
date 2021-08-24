package com.powernode.lcb.service;

import com.powernode.lcb.model.IncomeRecord;

import java.util.List;

public interface IncomeRecordService {


    List<IncomeRecord> queryByUId(int uId,int startIndex,int pageSize);

    int queryRows(int uId);
}
