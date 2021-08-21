package com.powernode.lcb.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.powernode.lcb.common.constant.Constants;
import com.powernode.lcb.model.BidInfo;
import com.powernode.lcb.model.InvestTopVO;
import com.powernode.lcb.model.LoanInfo;
import com.powernode.lcb.service.BidInfoService;
import com.powernode.lcb.service.LoanInfoService;
import com.powernode.lcb.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class IndexController {

    @Reference(interfaceClass = LoanInfoService.class,version = "1.0.0",timeout = 15000)
    LoanInfoService loanInfoService;
    @Reference(interfaceClass = UserService.class,version = "1.0.0",timeout = 15000)
    UserService userService;
    @Reference(interfaceClass = BidInfoService.class,version = "1.0.0",timeout = 15000)
    BidInfoService bidInfoService;

    @RequestMapping("/index")
    public String index(Model model){
        //总用户数
        long count = userService.queryUserCount();
        //全品类历史平均年化利率
        double rate = loanInfoService.queryHistoryAvgRate();
        //累计成交额
        double bidMoney = bidInfoService.queryBidMoney();
        //新手宝
        LoanInfo noviceProduct  = loanInfoService.queryByProductTypeId(Constants.PRODUCT_TYPE_X,0,1).get(0);
        //优选类产品
        List<LoanInfo> excellentProductList  = loanInfoService.queryByProductTypeId(Constants.PRODUCT_TYPE_U,0,4);
        //散标类产品
        List<LoanInfo> looseMarkProductList = loanInfoService.queryByProductTypeId(Constants.PRODUCT_TYPE_S,0,8);

        model.addAttribute("userCount",count);
        model.addAttribute("rate",rate);
        model.addAttribute("bidMoney",bidMoney);
        model.addAttribute("noviceProduct",noviceProduct);
        model.addAttribute("excellentProductList",excellentProductList);
        model.addAttribute("looseMarkProductList",looseMarkProductList);

        return "index";
    }

    @RequestMapping("/loan/loan")
    public String loan(Integer productTypeId,Integer page,Model model){
        page = page == null?1:page;
        int pageSize = 9;
        int startIndex = (page - 1) * pageSize;
        List<LoanInfo> loanInfoList = loanInfoService.queryByProductTypeId(productTypeId,startIndex,pageSize);
        int rows = loanInfoService.queryRows(productTypeId);
        int pageCount = (int) Math.ceil((double)rows / pageSize);
        List<InvestTopVO> investList = bidInfoService.queryBidMoneyRank(5);

        model.addAttribute("loanInfoList",loanInfoList);
        model.addAttribute("rows",rows);
        model.addAttribute("pageCount",pageCount);
        model.addAttribute("page",page);
        model.addAttribute("productTypeId",productTypeId);
        model.addAttribute("investList",investList);
        return "loan";
    }

    @GetMapping("/loan/loanInfo/{id}")
    public String loanInfo(Model model,@PathVariable Integer id){
        LoanInfo loanInfo = loanInfoService.queryByPrimaryKey(id);
        List<BidInfo> bidInfoList = bidInfoService.queryByBidLoanId(id);
        model.addAttribute("loanInfo",loanInfo);
        model.addAttribute("bidInfoList",bidInfoList);
        return "loanInfo";
    }
}
