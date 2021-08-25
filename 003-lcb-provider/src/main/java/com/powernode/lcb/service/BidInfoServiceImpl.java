package com.powernode.lcb.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.powernode.lcb.common.constant.Constants;
import com.powernode.lcb.mapper.BidInfoMapper;
import com.powernode.lcb.model.BidInfo;
import com.powernode.lcb.model.InvestTopVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Service(interfaceClass = BidInfoService.class,version = "1.0.0",timeout = 15000)
public class BidInfoServiceImpl implements  BidInfoService {
    @Autowired
    BidInfoMapper bidInfoMapper;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    UserService userService;

    @Override
    public double queryBidMoney() {

        return bidInfoMapper.selectBidMoney();
    }


//    @Override
//    public List<InvestTopVO> queryBidMoneyRank(int num) {
//
//        return bidInfoMapper.selectBidMoneyRank(num);
//    }


    @Override
    public List<BidInfo> queryByBidLoanId(int loanId) {

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
    public List<BidInfo> queryByUId(int uId, int startIndex, int pageSize) {
        return bidInfoMapper.selectByUId(uId,startIndex,pageSize);
    }

    @Override
    public int queryRows(int uId) {
        return bidInfoMapper.selectRows(uId);
    }

    @Override
    public long addInvestRecordRedis() {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        List<InvestTopVO> investTopVOList = bidInfoMapper.selectAllBidMoney();
        Set<ZSetOperations.TypedTuple<Object>> set = new HashSet<>();
        for (InvestTopVO investTopVO : investTopVOList) {
            ZSetOperations.TypedTuple<Object> tuple = new DefaultTypedTuple<>(investTopVO.getPhone(),investTopVO.getMoney());
            set.add(tuple);
        }
        //批量添加
        Long add = redisTemplate.opsForZSet().add(Constants.INVEST_TOP, set);
        System.out.println(add);
        return add;
    }

    @Override
    public List<InvestTopVO> queryBidMoneyRank() {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        Set<ZSetOperations.TypedTuple<Object>> set = redisTemplate.opsForZSet().reverseRangeWithScores(Constants.INVEST_TOP, 0, 4);
        Iterator<ZSetOperations.TypedTuple<Object>> iterator = set.iterator();
        List<InvestTopVO> investTopVOList = new ArrayList<>();
        while (iterator.hasNext()){
            ZSetOperations.TypedTuple<Object> object = iterator.next();
            Double score = object.getScore();
            String phone = (String) object.getValue();
            InvestTopVO investTopVO = new InvestTopVO(phone,score);
            investTopVOList.add(investTopVO);
        }
        return investTopVOList;
    }


}
