package com.powernode.lcb.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.powernode.lcb.common.constant.Constants;
import com.powernode.lcb.common.constant.ResponseResult;
import com.powernode.lcb.common.util.HttpClientUtils;
import com.powernode.lcb.config.AlipayConfig;
import com.powernode.lcb.model.FinanceAccount;
import com.powernode.lcb.model.RechargeRecord;
import com.powernode.lcb.model.User;
import com.powernode.lcb.service.DealService;
import com.powernode.lcb.service.FinanceAccountService;
import com.powernode.lcb.service.RechargeRecordService;
import com.powernode.lcb.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
public class DealController {

    @Reference(interfaceClass = DealService.class,version = "1.0.0",timeout = 15000)
    private DealService dealService;
    @Reference(interfaceClass = UserService.class,version = "1.0.0",timeout = 15000)
    private UserService userService;
    @Reference(interfaceClass = RechargeRecordService.class,version = "1.0.0",timeout = 15000)
    private RechargeRecordService rechargeRecordService;
    @Reference(interfaceClass = FinanceAccountService.class,version = "1.0.0",timeout = 15000)
    private FinanceAccountService financeAccountService;

    @RequestMapping("/loan/toRecharge")
    public String toRecharge(String rechargeMoney,Model model,HttpSession session){
        User user = (User) session.getAttribute(Constants.SESSION_USER);

        String phone = user.getPhone();
        String out_trade_no = System.currentTimeMillis() + phone;
        String total_amount = rechargeMoney;
        String subject = "??????????????????";

        //??????????????????
        RechargeRecord rechargeRecord = new RechargeRecord();
        rechargeRecord.setRechargeNo(out_trade_no);
        rechargeRecord.setUid(user.getId());
        rechargeRecord.setRechargeStatus("0");
        rechargeRecord.setRechargeMoney(Double.parseDouble(rechargeMoney));
        rechargeRecord.setRechargeTime(new Date());
        rechargeRecord.setRechargeDesc(subject);
        rechargeRecordService.addRechargeRecord(rechargeRecord);

        Map<String,Object> map = new HashMap<>();
        map.put("out_trade_no",out_trade_no);
        map.put("total_amount",total_amount);
        map.put("subject",subject);
        String url = "http://localhost:9090/alipay/index";
        String result = "";
        try {
            result = HttpClientUtils.doPost(url, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("result",result);
        return "pay";
    }

    @RequestMapping("/loan/page/invest")
    @ResponseBody
    public ResponseResult invest(int loanInfoId, double bidMoney, HttpSession session){
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        ResponseResult responseResult = new ResponseResult();
        int result = dealService.invest(loanInfoId, bidMoney, user);
        if (result > 0){
            user = userService.queryByPhone(user.getPhone());
            session.setAttribute(Constants.SESSION_USER,user);
            responseResult.setCode(Constants.STATUS_OK);
            responseResult.setMsg("????????????");
        }else {
            responseResult.setCode(Constants.STATUS_ERROR);
            responseResult.setMsg("??????????????????????????????");
        }
        return responseResult;
    }

    @RequestMapping("/loan/page/toRecharge")
    public String toRecharge(){
        return "toRecharge";
    }

    @RequestMapping("/returnPayStatus")
    public String returnPayStatus(Model model, HttpServletRequest request, String out_trade_no, String trade_no, String total_amount) throws UnsupportedEncodingException {
        User user = (User) request.getSession().getAttribute(Constants.SESSION_USER);
        System.out.println(out_trade_no+","+trade_no+","+total_amount);
        //???????????????GET??????????????????
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //???????????????????????????????????????????????????
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        boolean signVerified = false; //??????SDK????????????
        try {
            signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type);
            //????????????????????????????????????????????????????????????????????????
            if(signVerified) {
                //????????????????????????????????????????????????
                String queryUrl = "http://localhost:9090/alipay/payQuery";
                Map<String,String> map = new HashMap<>();
                map.put("out_trade_no",out_trade_no);
                try {
                    String result = HttpClientUtils.doGet(queryUrl, map);
                    JSONObject jsonObject = JSON.parseObject(result);
                    JSONObject responseJsonObject = jsonObject.getJSONObject("alipay_trade_query_response");
                    String code = responseJsonObject.getString("code");
                    if("10000".equals(code)){  //????????????
                        System.out.println("????????????");
                        String tradeStatus = responseJsonObject.getString("trade_status");
                        if ("TRADE_SUCCESS".equals(tradeStatus)){//????????????
                            //????????????????????????
                            RechargeRecord rechargeRecord = rechargeRecordService.queryByRechargeNo(out_trade_no);
                            rechargeRecord.setRechargeStatus("1");
                            rechargeRecordService.modifByRechargeNo(rechargeRecord);
                            //????????????????????????
                            FinanceAccount financeAccount = financeAccountService.queryByUId(user.getId());
                            financeAccount.setAvailableMoney(financeAccount.getAvailableMoney() + Double.parseDouble(total_amount));
                            financeAccountService.updateById(financeAccount);
                            //??????session
                            request.getSession().setAttribute(Constants.SESSION_USER,userService.queryByPhone(user.getPhone()));

                            model.addAttribute("trade_msg","????????????");
                            model.addAttribute("out_trade_no",out_trade_no);
                            model.addAttribute("trade_no",trade_no);
                            model.addAttribute("total_amount",total_amount);
                        }else if ("TRADE_CLOSED".equals(tradeStatus)){
                            //????????????????????????
                            RechargeRecord rechargeRecord = rechargeRecordService.queryByRechargeNo(out_trade_no);
                            rechargeRecord.setRechargeStatus("2");
                            rechargeRecordService.modifByRechargeNo(rechargeRecord);

                            System.out.println("?????????????????????????????????");
                            model.addAttribute("trade_msg","????????????");
                        }
                    }else {
                        model.addAttribute("trade_msg","????????????");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                System.out.println("????????????");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        return "toRechargeBack";
        
    }
}
