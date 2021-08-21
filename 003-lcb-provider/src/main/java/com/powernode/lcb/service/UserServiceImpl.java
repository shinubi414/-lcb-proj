package com.powernode.lcb.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.powernode.lcb.common.constant.Constants;
import com.powernode.lcb.common.util.HttpClientUtils;
import com.powernode.lcb.mapper.UserMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@Service(interfaceClass = UserService.class,version = "1.0.0",timeout = 15000)
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public long queryUserCount() {
        return userMapper.selectUserCount();
    }

    @Override
    public String sendSmsCode(String phone) {
        String appkey = "061a4d7f0f6fc9fba8695111cbb9d38e";
        //生成四位随机数字字符串
        String code = RandomStringUtils.randomNumeric(4);
        System.out.println("验证码是");
        String content = "【创信】你的验证码是：" + code + ",1分钟内有效";
        Map<String,String> map = new HashMap<>();
        map.put("mobile",phone);
        map.put("content",content);
        map.put("appkey",appkey);
        String result = "";
        try {
            result = HttpClientUtils.doGet("https://way.jd.com/chuangxin/dxjk",map);
            System.out.println(result);
            JSONObject jsonObject = JSONObject.parseObject(result);
            String returnStatus = jsonObject.getString("ReturnStatus");
            if ("success".equals(returnStatus)){
                redisTemplate.setKeySerializer(new StringRedisSerializer());
                redisTemplate.opsForValue().set(phone,code,1,TimeUnit.MINUTES);
                result = "success";
            }else {
                result = "error";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
