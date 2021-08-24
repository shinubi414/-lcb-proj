package com.powernode.lcb.service;

import com.powernode.lcb.model.BidInfo;
import com.powernode.lcb.model.InvestTopVO;

import java.util.List;

public interface BidInfoService {

    double queryBidMoney();

    List<InvestTopVO> queryBidMoneyRank(int num);

    List<BidInfo> queryByBidLoanId(int loanId);

    List<BidInfo> queryByUId(int uId,int startIndex,int pageSize);

    int queryRows(int uId);

}
