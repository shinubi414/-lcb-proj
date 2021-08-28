package com.powernode.lcb.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.powernode.lcb.mapper.RechargeRecordMapper;
import com.powernode.lcb.model.RechargeRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Service(interfaceClass = RechargeRecordService.class,version = "1.0.0",timeout = 15000)
public class RechargeRecordServiceImpl implements RechargeRecordService {

    @Autowired
    RechargeRecordMapper rechargeRecordMapper;


    @Override
    public List<RechargeRecord> queryByUId(int uId, int startIndex, int pageSize) {
        return rechargeRecordMapper.selectByUId(uId,startIndex,pageSize);
    }

    @Override
    public int queryRows(int uId) {
        return rechargeRecordMapper.selectRows(uId);
    }

    @Override
    public int addRechargeRecord(RechargeRecord rechargeRecord) {
        return rechargeRecordMapper.insert(rechargeRecord);
    }

    @Override
    public int modifByRechargeNo(RechargeRecord rechargeRecord) {
        return rechargeRecordMapper.updateByRechargeNo(rechargeRecord);
    }

    @Override
    public RechargeRecord queryByRechargeNo(String rechargeNo) {
        return rechargeRecordMapper.selectByRechargeNo(rechargeNo);
    }


}
