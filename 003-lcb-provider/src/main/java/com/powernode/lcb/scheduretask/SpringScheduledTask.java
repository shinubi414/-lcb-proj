package com.powernode.lcb.scheduretask;

import com.powernode.lcb.service.FinanceAccountService;
import com.powernode.lcb.service.IncomeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class SpringScheduledTask {

    @Autowired
    IncomeRecordService incomeRecordService;
    @Autowired
    FinanceAccountService financeAccountService;

    //@Scheduled(cron = "0 42 * * * ?")
    public void generateIncomePlan(){
        int count = incomeRecordService.incomePlan();
        if(count > 0){
            System.out.println("成功生成收益计划");
        }else {
            System.out.println("生成收益计划失败");
        }
    }

    //@Scheduled(cron = "0 43 * * * ?")
    public void generateReturnPayment(){
        int count = financeAccountService.returnPayment();
        if(count > 0){
            System.out.println("成功回款");
        }else {
            System.out.println("回款失败");
        }
    }
}
