package com.powernode.lcb.service;

import com.alibaba.dubbo.config.annotation.Service;

import com.powernode.lcb.mapper.FinanceAccountMapper;
import com.powernode.lcb.mapper.IncomeRecordMapper;
import com.powernode.lcb.model.FinanceAccount;
import com.powernode.lcb.model.IncomeRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
@Service(interfaceClass = FinanceAccountService.class,version = "1.0.0",timeout = 15000)
public class FinanceAccountServiceImpl implements FinanceAccountService {

    @Autowired
    IncomeRecordMapper incomeRecordMapper;
    @Autowired
    FinanceAccountMapper financeAccountMapper;


    @Override
    @Transactional
    public int returnPayment() {
        List<IncomeRecord> incomeRecordList = incomeRecordMapper.selectByIncomeStatus(0);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();  //当前时间
        int count = 0;
        for (IncomeRecord incomeRecord : incomeRecordList) {
            if (format.format(date).equals(format.format(incomeRecord.getIncomeDate())) && incomeRecord.getIncomeStatus() == 0) {
                double money = incomeRecord.getBidMoney() + incomeRecord.getIncomeMoney();
                FinanceAccount financeAccount = financeAccountMapper.selectByUId(incomeRecord.getUid());
                financeAccount.setAvailableMoney(financeAccount.getAvailableMoney() + money);
                count += financeAccountMapper.updateByPrimaryKey(financeAccount);
                System.out.println("回款" + count);
                incomeRecord.setIncomeStatus(1);
                count += incomeRecordMapper.updateByPrimaryKey(incomeRecord);
            }
        }
        return count;
    }
}
