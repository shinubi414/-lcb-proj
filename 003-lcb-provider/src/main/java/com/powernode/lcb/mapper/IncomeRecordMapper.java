package com.powernode.lcb.mapper;

import com.powernode.lcb.model.IncomeRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IncomeRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(IncomeRecord record);

    int insertSelective(IncomeRecord record);

    IncomeRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IncomeRecord record);

    int updateByPrimaryKey(IncomeRecord record);

    List<IncomeRecord> selectByUId(@Param("uId") int uId, @Param("startIndex") int startIndex, @Param("pageSize")int pageSize);

    int selectRows(int uId);

    List<IncomeRecord> selectByIncomeStatus(int incomeStatus);
}