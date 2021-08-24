package com.powernode.lcb.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.powernode.lcb.common.constant.Constants;
import com.powernode.lcb.model.BidInfo;
import com.powernode.lcb.model.IncomeRecord;
import com.powernode.lcb.model.RechargeRecord;
import com.powernode.lcb.model.User;
import com.powernode.lcb.service.BidInfoService;
import com.powernode.lcb.service.IncomeRecordService;
import com.powernode.lcb.service.RechargeRecordService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class BillController {

    @Reference(interfaceClass = IncomeRecordService.class,version = "1.0.0",timeout = 15000)
    IncomeRecordService incomeRecordService;
    @Reference(interfaceClass = RechargeRecordService.class,version = "1.0.0",timeout = 15000)
    RechargeRecordService rechargeRecordService;
    @Reference(interfaceClass = BidInfoService.class,version = "1.0.0",timeout = 15000)
    BidInfoService bidInfoService;


    @RequestMapping("/loan/myIncome")
    public String ShowIncome(Model model, Integer page, HttpSession session){
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        page = page == null?1:page;
        int pageSize = 6;
        int startIndex = (page - 1) * pageSize;
        int rows = incomeRecordService.queryRows(user.getId());
        int pageCount = (int) Math.ceil((double) rows / pageSize);
        List<IncomeRecord> incomeRecordList = incomeRecordService.queryByUId(user.getId(),startIndex,pageSize);

        model.addAttribute("rows",rows);
        model.addAttribute("pageCount",pageCount);
        model.addAttribute("incomeRecordList",incomeRecordList);
        model.addAttribute("page",page);

        return "myIncome";
    }

    @RequestMapping("/loan/myRecharge")
    public String ShowRecharge(Model model, Integer page, HttpSession session){
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        page = page == null?1:page;
        int pageSize = 6;
        int startIndex = (page - 1) * pageSize;
        int rows = rechargeRecordService.queryRows(user.getId());
        int pageCount = (int) Math.ceil((double) rows / pageSize);
        List<RechargeRecord> rechargeRecordList = rechargeRecordService.queryByUId(user.getId(), startIndex, pageSize);

        model.addAttribute("rows",rows);
        model.addAttribute("pageCount",pageCount);
        model.addAttribute("rechargeRecordList",rechargeRecordList);
        model.addAttribute("page",page);

        return "myRecharge";
    }

    @RequestMapping("/loan/myInvest")
    public String ShowInvest(Model model, Integer page, HttpSession session){
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        page = page == null?1:page;
        int pageSize = 6;
        int startIndex = (page - 1) * pageSize;
        int rows = bidInfoService.queryRows(user.getId());
        int pageCount = (int) Math.ceil((double) rows / pageSize);
        List<BidInfo> bidInfoList = bidInfoService.queryByUId(user.getId(), startIndex, pageSize);

        model.addAttribute("rows",rows);
        model.addAttribute("pageCount",pageCount);
        model.addAttribute("bidInfoList",bidInfoList);
        model.addAttribute("page",page);

        return "myInvest";
    }
}
