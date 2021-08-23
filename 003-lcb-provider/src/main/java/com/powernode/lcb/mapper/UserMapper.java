package com.powernode.lcb.mapper;

import com.powernode.lcb.model.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    long selectUserCount();

    int selectByPhone(@Param("phone") String phone);

    User selectByPhoneAndPassword(@Param("phone")String phone,@Param("password")String password);
}