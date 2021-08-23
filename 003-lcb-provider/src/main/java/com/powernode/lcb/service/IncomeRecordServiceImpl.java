package com.powernode.lcb.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.powernode.lcb.mapper.IncomeRecordMapper;
import com.powernode.lcb.model.IncomeRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Service(interfaceClass = IncomeRecordService.class,version = "1.0.0",timeout = 15000)
public class IncomeRecordServiceImpl implements IncomeRecordService {

    @Autowired
    IncomeRecordMapper incomeRecordMapper;

    @Override
    public List<IncomeRecord> queryByUId(int uId) {

        return incomeRecordMapper.selectByUId(uId);
    }
}
