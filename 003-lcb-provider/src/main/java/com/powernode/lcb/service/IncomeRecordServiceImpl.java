package com.powernode.lcb.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.powernode.lcb.mapper.BidInfoMapper;
import com.powernode.lcb.mapper.IncomeRecordMapper;
import com.powernode.lcb.mapper.LoanInfoMapper;
import com.powernode.lcb.model.BidInfo;
import com.powernode.lcb.model.IncomeRecord;
import com.powernode.lcb.model.LoanInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;

@Component
@Service(interfaceClass = IncomeRecordService.class,version = "1.0.0",timeout = 15000)
public class IncomeRecordServiceImpl implements IncomeRecordService {

    @Autowired
    IncomeRecordMapper incomeRecordMapper;
    @Autowired
    LoanInfoMapper loanInfoMapper;
    @Autowired
    BidInfoMapper bidInfoMapper;

    @Override
    public List<IncomeRecord> queryByUId(int uId, int startIndex, int pageSize) {
        return incomeRecordMapper.selectByUId(uId,startIndex,pageSize);
    }

    @Override
    public int queryRows(int uId) {
        return incomeRecordMapper.selectRows(uId);
    }

    @Override
    @Transactional
    public int incomePlan() {
        List<LoanInfo> loanInfoList = loanInfoMapper.selectByProductStatus(1);
        int count = 0;
        for (LoanInfo loanInfo : loanInfoList) {
            for (BidInfo bidInfo : bidInfoMapper.selectByBidLoanId(loanInfo.getId())) {
                IncomeRecord incomeRecord = new IncomeRecord();
                incomeRecord.setUid(bidInfo.getUid());
                incomeRecord.setLoanId(loanInfo.getId());
                incomeRecord.setBidId(bidInfo.getId());
                incomeRecord.setBidMoney(bidInfo.getBidMoney());

                //计算收益时间
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(loanInfo.getProductFullTime());
                if ("新手宝".equals(loanInfo.getProductName())){
                    calendar.set(Calendar.DATE,calendar.get(Calendar.DATE) + loanInfo.getCycle());
                    incomeRecord.setIncomeMoney(bidInfo.getBidMoney() * (loanInfo.getRate() / 100 / 365) * loanInfo.getCycle());
                }else {
                    calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH) + loanInfo.getCycle());
                    incomeRecord.setIncomeMoney(bidInfo.getBidMoney() * (loanInfo.getRate() / 100 / 365) * loanInfo.getCycle() * 30);
                }
                incomeRecord.setIncomeDate(calendar.getTime());
                incomeRecord.setIncomeStatus(0);
                count += incomeRecordMapper.insert(incomeRecord);
                System.out.println("计划" + count);
                loanInfo.setProductStatus(2);
                count += loanInfoMapper.updateByPrimaryKey(loanInfo);
            }
        }

        return count;
    }
}
