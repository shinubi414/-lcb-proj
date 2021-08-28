package com.powernode.lcb.mapper;

import com.powernode.lcb.model.RechargeRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RechargeRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RechargeRecord record);

    int insertSelective(RechargeRecord record);

    RechargeRecord selectByPrimaryKey(Integer id);

    RechargeRecord selectByRechargeNo(@Param("rechargeNo") String rechargeNo);

    int updateByPrimaryKeySelective(RechargeRecord record);

    int updateByPrimaryKey(RechargeRecord record);

    int updateByRechargeNo(RechargeRecord rechargeRecord);

    List<RechargeRecord> selectByUId(@Param("uId") int uId, @Param("startIndex") int startIndex, @Param("pageSize")int pageSize);

    int selectRows(int uId);

}