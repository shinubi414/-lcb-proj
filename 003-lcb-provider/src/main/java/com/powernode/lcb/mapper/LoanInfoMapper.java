package com.powernode.lcb.mapper;

import com.powernode.lcb.model.LoanInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LoanInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LoanInfo record);

    int insertSelective(LoanInfo record);

    LoanInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LoanInfo record);

    int updateByPrimaryKey(LoanInfo record);

    double selectHistoryAvgRate();

    List<LoanInfo> selectByProductTypeId(@Param("productTypeId")int productTypeId,@Param("startIndex")int startIndex,@Param("pageSize")int pageSize);

    int selectRows(int productTypeId);

}