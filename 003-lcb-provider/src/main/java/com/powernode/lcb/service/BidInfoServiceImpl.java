package com.powernode.lcb.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.powernode.lcb.mapper.BidInfoMapper;
import com.powernode.lcb.model.BidInfo;
import com.powernode.lcb.model.InvestTopVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Service(interfaceClass = BidInfoService.class,version = "1.0.0",timeout = 15000)
public class BidInfoServiceImpl implements  BidInfoService {
    @Autowired
    BidInfoMapper bidInfoMapper;
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public double queryBidMoney() {

        return bidInfoMapper.selectBidMoney();
    }

    @Override
    public List<InvestTopVO> queryBidMoneyRank(int num) {

        return bidInfoMapper.selectBidMoneyRank(num);
    }

    @Override
    public List<BidInfo> queryByBidLoanId(int loanId) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        List<BidInfo> bidInfoList = redisTemplate.opsForList().range("BidInfoRecord" + loanId, 0, -1);
        if (bidInfoList.size() == 0){
            synchronized (this){
                bidInfoList = redisTemplate.opsForList().range("BidInfoRecord" + loanId, 0, -1);
                if (bidInfoList.size() == 0){
                    bidInfoList = bidInfoMapper.selectByBidLoanId(loanId);
                    if (bidInfoList.size() != 0) {
                        redisTemplate.opsForList().leftPushAll("BidInfoRecord" + loanId, bidInfoList);
                    }
                }
            }
        }
        return bidInfoList;
    }

    @Override
    public List<BidInfo> queryByUId(int uId) {

        return bidInfoMapper.selectByUId(uId);
    }
}
