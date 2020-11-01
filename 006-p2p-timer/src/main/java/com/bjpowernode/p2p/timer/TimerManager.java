package com.bjpowernode.p2p.timer;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bjpowernode.p2p.service.loan.IncomeRecordService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 张新宇
 * 2020/8/23
 */
@Component
public class TimerManager {

    @Reference(interfaceClass = IncomeRecordService.class,version = "1.0.0",check = false)
    private IncomeRecordService incomeRecordService;

//    @Scheduled(cron = "0/5 * * * * ?")
    public void generateIncomePlan() throws Exception {

        incomeRecordService.generateIncomePlan();
    }

    @Scheduled(cron = "0/5 * * * * ?")
    public void generateIncomeBack() throws Exception {
        incomeRecordService.generateIncomeBack();
    }
}
