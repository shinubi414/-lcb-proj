package com.powernode.lcb.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.powernode.lcb.common.util.HttpClientUtils;
import com.powernode.lcb.mapper.FinanceAccountMapper;
import com.powernode.lcb.mapper.RechargeRecordMapper;
import com.powernode.lcb.model.FinanceAccount;
import com.powernode.lcb.model.RechargeRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Service(interfaceClass = RechargeRecordService.class,version = "1.0.0",timeout = 15000)
public class RechargeRecordServiceImpl implements RechargeRecordService {

    @Autowired
    RechargeRecordMapper rechargeRecordMapper;
    @Autowired
    FinanceAccountMapper financeAccountMapper;


    @Override
    public List<RechargeRecord> queryByUId(int uId, int startIndex, int pageSize) {
        return rechargeRecordMapper.selectByUId(uId,startIndex,pageSize);
    }

    @Override
    public int queryRows(int uId) {
        return rechargeRecordMapper.selectRows(uId);
    }

    @Override
    public int addRechargeRecord(RechargeRecord rechargeRecord) {
        return rechargeRecordMapper.insert(rechargeRecord);
    }

    @Override
    public int modifByRechargeNo(RechargeRecord rechargeRecord) {
        return rechargeRecordMapper.updateByRechargeNo(rechargeRecord);
    }

    @Override
    public RechargeRecord queryByRechargeNo(String rechargeNo) {
        return rechargeRecordMapper.selectByRechargeNo(rechargeNo);
    }

    @Override
    @Transactional
    public int outOrder() {  //掉单处理
        List<RechargeRecord> rechargeRecordList = rechargeRecordMapper.selectByRechargeStatus("0");
        int count = 0 ;
        for (RechargeRecord rechargeRecord : rechargeRecordList) {
            String queryUrl = "http://localhost:9090/alipay/payQuery";
            Map<String,String> map = new HashMap<>();
            map.put("out_trade_no",rechargeRecord.getRechargeNo());
            try {
                String result = HttpClientUtils.doGet(queryUrl, map);
                JSONObject jsonObject = JSON.parseObject(result);
                JSONObject responseJsonObject = jsonObject.getJSONObject("alipay_trade_query_response");
                String code = responseJsonObject.getString("code");
                if ("10000".equals(code)){
                    String tradeStatus = responseJsonObject.getString("trade_status");
                    if ("TRADE_CLOSED".equals(tradeStatus)){
                        rechargeRecord.setRechargeStatus("3");
                        count += rechargeRecordMapper.updateByPrimaryKey(rechargeRecord);
                    }else if ("RADE_SUCCESS".equals(tradeStatus)){
                        if ("0".equals(rechargeRecord.getRechargeStatus())){
                            FinanceAccount financeAccount = financeAccountMapper.selectByUId(rechargeRecord.getUid());
                            financeAccount.setAvailableMoney(financeAccount.getAvailableMoney() + rechargeRecord.getRechargeMoney());
                            count += financeAccountMapper.updateByPrimaryKey(financeAccount);
                            rechargeRecord.setRechargeStatus("1");
                            count += rechargeRecordMapper.updateByPrimaryKey(rechargeRecord);
                        }
                    }
                }else {
                    System.out.println("查询失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return count;
    }



}
