package com.powernode.lcb.service;

import com.powernode.lcb.model.RechargeRecord;

import java.util.List;

public interface RechargeRecordService {

    List<RechargeRecord> queryByUId(int uId,int startIndex,int pageSize);

    int queryRows(int uId);
}
