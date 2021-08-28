package com.powernode.lcb.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.powernode.lcb.common.constant.Constants;
import com.powernode.lcb.mapper.BidInfoMapper;
import com.powernode.lcb.mapper.FinanceAccountMapper;
import com.powernode.lcb.mapper.LoanInfoMapper;
import com.powernode.lcb.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Component
@Service(interfaceClass = DealService.class,version = "1.0.0",timeout = 15000)
public class DealServiceImpl implements DealService {

    @Autowired
    LoanInfoMapper loanInfoMapper;
    @Autowired
    FinanceAccountMapper financeAccountMapper;
    @Autowired
    BidInfoMapper bidInfoMapper;
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    @Transactional
    public int invest(int loanInfoId, double bidMoney, User user) {
        //更新产品信息
        LoanInfo loanInfo = loanInfoMapper.selectByPrimaryKey(loanInfoId);
        loanInfo.setLeftProductMoney(loanInfo.getLeftProductMoney() - bidMoney);
        loanInfo.setProductStatus(loanInfo.getLeftProductMoney() == 0?1:0);
        if (loanInfo.getLeftProductMoney() == 0){
            loanInfo.setProductFullTime(new Date());
        }
        int count = loanInfoMapper.updateByPrimaryKey(loanInfo);
        //更新账户信息
        FinanceAccount financeAccount = financeAccountMapper.selectByUId(user.getId());
        financeAccount.setAvailableMoney(financeAccount.getAvailableMoney() - bidMoney);
        count += financeAccountMapper.updateByPrimaryKey(financeAccount);
        //添加投资记录
        BidInfo bidInfo = new BidInfo();
        bidInfo.setLoanId(loanInfoId);
        bidInfo.setUid(user.getId());
        bidInfo.setBidMoney(bidMoney);
        bidInfo.setBidTime(new Date());
        bidInfo.setBidStatus(1);
        count += bidInfoMapper.insert(bidInfo);
        //更新redis数据
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.opsForZSet().incrementScore(Constants.INVEST_TOP,user.getPhone(),bidMoney);
        return count;
    }




}
