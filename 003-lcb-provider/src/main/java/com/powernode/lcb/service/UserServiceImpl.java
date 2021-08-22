package com.powernode.lcb.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.powernode.lcb.common.constant.Constants;
import com.powernode.lcb.common.util.HttpClientUtils;
import com.powernode.lcb.mapper.UserMapper;
import com.powernode.lcb.model.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.Date;
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
        System.out.println("你的验证码是：" + code);
        String content = "【创信】你的验证码是：" + code;
        Map<String,String> map = new HashMap<>();
        map.put("content",content);
        map.put("appkey",appkey);
        map.put("mobile",phone);
        String result = "";
        try {
//            result = HttpClientUtils.doGet("https://way.jd.com/chuangxin/VerCodesms",map);

            result = "{\n" +
                    "    \"code\": \"10000\",\n" +
                    "    \"charge\": false,\n" +
                    "    \"remain\": 1305,\n" +
                    "    \"msg\": \"查询成功\",\n" +
                    "    \"result\": {\n" +
                    "        \"ReturnStatus\": \"Success\",\n" +
                    "        \"Message\": \"ok\",\n" +
                    "        \"RemainPoint\": 420842,\n" +
                    "        \"TaskID\": 18424321,\n" +
                    "        \"SuccessCounts\": 1\n" +
                    "    }\n" +
                    "}";
            JSONObject jsonObject = JSONObject.parseObject(result);
            String returnStatus = jsonObject.getJSONObject("result").getString("ReturnStatus");
            System.out.println(returnStatus);
            if ("Success".equals(returnStatus)){
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

    @Override
    public boolean checkSmsCode(String phone,String code) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        String smsCode = (String) redisTemplate.opsForValue().get(phone);
        if (smsCode == null || !code.equals(smsCode)){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public boolean addUser(User user) {
        user.setAddTime(new Date());
        //md5加密
        String password = DigestUtils.md5Hex(user.getLoginPassword());
        user.setLoginPassword(password);
        int result = userMapper.insert(user);
        if (result > 0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean queryByPhone(String phone) {
        int result = userMapper.selectByPhone(phone);
        if (result > 0) {
            return false;
        }else {
            return true;
        }
    }
}
