package com.powernode.lcb.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.powernode.lcb.common.util.HttpClientUtils;
import com.powernode.lcb.mapper.FinanceAccountMapper;
import com.powernode.lcb.mapper.UserMapper;
import com.powernode.lcb.model.FinanceAccount;
import com.powernode.lcb.model.User;
import com.sun.deploy.net.HttpRequest;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@Service(interfaceClass = UserService.class, version = "1.0.0", timeout = 15000)
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    FinanceAccountMapper financeAccountMapper;

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
        Map<String, String> map = new HashMap<>();
        map.put("content", content);
        map.put("appkey", appkey);
        map.put("mobile", phone);
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
            if ("Success".equals(returnStatus)) {
               //redisTemplate.setKeySerializer(new StringRedisSerializer());
                redisTemplate.opsForValue().set(phone, code, 1, TimeUnit.MINUTES);
                result = "success";
            } else {
                result = "error";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean checkSmsCode(String phone, String code) {
        //redisTemplate.setKeySerializer(new StringRedisSerializer());
        String smsCode = (String) redisTemplate.opsForValue().get(phone);
        if (smsCode == null || !code.equals(smsCode)) {
            return false;
        } else {
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
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public User queryByPhone(String phone) {
        User user = userMapper.selectByPhone(phone);
        return user;
    }

    @Override
    public User login(String phone, String password) {
        password = DigestUtils.md5Hex(password);
        User user = userMapper.selectByPhoneAndPassword(phone, password);
        if (user != null) {
            user.setLastLoginTime(new Date());
            userMapper.updateByPrimaryKey(user);
        }
        return user;
    }

    @Override
    @Transactional
    public String realName(String phone, String realName, String idCard) {
        User user = userMapper.selectByPhone(phone);
        if (user == null) {
            return "该手机号未注册";
        } else {
            if (user.getName() != null) {
                return "该手机号已实名认证";
            } else {
                //实名验证
                String appkey = "061a4d7f0f6fc9fba8695111cbb9d38e";
                Map<String, Object> map = new HashMap<>();
                map.put("cardNo", idCard);
                map.put("realName", realName);
                map.put("appkey", appkey);
                try {
//                    String result = HttpClientUtils.doPost("https://way.jd.com/hl/idcardcheck", map);
                    String result = "{\"code\":\"10000\",\"charge\":false,\"remain\":0,\"msg\":\"查询成功\",\"result\":{\"error_code\":0,\"reason\":\"成功\",\"result\":{\"realname\":\"罗*\",\"idcard\":\"441781************\",\"isok\":true,\"IdCardInfor\":{\"province\":\"广东省\",\"city\":\"阳江市\",\"district\":\"阳春市\",\"area\":\"广东省阳江市阳春市\",\"sex\":\"男\",\"birthday\":\"1997-5-21\"}}},\"requestId\":\"22948364f3a84b99b717df3f3f03099c\"}";
                    JSONObject jsonObject = JSON.parseObject(result);
                    boolean flag = jsonObject.getJSONObject("result").getJSONObject("result").getBoolean("isok");
                    if (flag == false) {
                        return "认证不通过，请核对身份号或姓名";
                    } else {
                        user.setIdCard(idCard);
                        user.setName(realName);
                        int count = userMapper.updateByPrimaryKey(user);
                        FinanceAccount financeAccount = new FinanceAccount();
                        financeAccount.setUid(user.getId());
                        financeAccount.setAvailableMoney(0.0);
                        count += financeAccountMapper.insert(financeAccount);
                        if (count > 0) {
                            return "ok";
                        }else {
                            return "实名认证失败";
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "系统繁忙，请稍后重试";
    }

}
