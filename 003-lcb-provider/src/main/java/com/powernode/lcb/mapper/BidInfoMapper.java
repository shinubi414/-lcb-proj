package com.powernode.lcb.mapper;

import com.powernode.lcb.model.BidInfo;
import com.powernode.lcb.model.InvestTopVO;

import java.util.List;

public interface BidInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BidInfo record);

    int insertSelective(BidInfo record);

    BidInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BidInfo record);

    int updateByPrimaryKey(BidInfo record);

    double selectBidMoney();

    List<InvestTopVO> selectBidMoneyRank(int num);

    List<BidInfo> selectByBidLoanId(int loanId);
}