package com.powernode.lcb.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.powernode.lcb.common.constant.Constants;
import com.powernode.lcb.mapper.LoanInfoMapper;
import com.powernode.lcb.model.LoanInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Service(interfaceClass = LoanInfoService.class,version = "1.0.0",timeout = 15000)
public class LoanInfoServiceImpl implements LoanInfoService {

    @Autowired
    LoanInfoMapper loanInfoMapper;
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public double queryHistoryAvgRate() {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        Double rate = (Double) redisTemplate.opsForValue().get(Constants.HISTORY_AVERAGE_RATE);
        if (rate == null){
            synchronized (this){
                rate = (Double) redisTemplate.opsForValue().get(Constants.HISTORY_AVERAGE_RATE);
                if (rate == null){
                    rate = loanInfoMapper.selectHistoryAvgRate();
                    redisTemplate.opsForValue().set(Constants.HISTORY_AVERAGE_RATE,rate);
                }
            }
        }
        return rate;
    }

    @Override
    public List<LoanInfo> queryByProductTypeId(int productTypeId, int startIndex, int pageSize) {

        return loanInfoMapper.selectByProductTypeId(productTypeId,startIndex,pageSize);
    }

    @Override
    public int queryRows(int productTypeId) {
        return loanInfoMapper.selectRows(productTypeId);
    }

    @Override
    public LoanInfo queryByPrimaryKey(Integer id) {
        return loanInfoMapper.selectByPrimaryKey(id);
    }




}
