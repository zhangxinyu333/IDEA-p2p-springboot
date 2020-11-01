package com.bjpowernode.p2p.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bjpowernode.p2p.service.loan.BidInfoService;
import com.bjpowernode.p2p.util.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 张新宇
 * 2020/8/22
 */
@Controller
public class BidInfoController {

    @Reference(interfaceClass = BidInfoService.class,version = "1.0.0",timeout = 35000)
    private BidInfoService bidInfoService;

    @RequestMapping("/loan/invest")
    public @ResponseBody Result invest(HttpServletRequest request,
                                       @RequestParam(value = "loanId",required = true)Integer loanId,
                                       @RequestParam(value = "uid",required = true)Integer uid,
                                       @RequestParam(value = "bidMoney",required = true)Double bidMoney){

        try {
            //准备投资参数
            Map<String,Object> paramMap=new HashMap<String, Object>();
            paramMap.put("uid", uid);
            paramMap.put("loanId", loanId);
            paramMap.put("bidMoney", bidMoney);

            //用户投资【1.更新产品剩余可投金额 2.更新账户可用余额 3.新增账户投资记录 4.判断产品是否满标】
            //（用户标识，产品标识，投资金额）
            bidInfoService.invest(paramMap);

            /*ExecutorService executorService = Executors.newFixedThreadPool(100);
            for (int i = 0; i < 1000; i++) {
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        Map<String,Object> paramMap=new HashMap<String, Object>();
                        paramMap.put("uid", 1);
                        paramMap.put("loanId",2);
                        paramMap.put("bidMoney", 1.0);

                        //用户投资【1.更新产品剩余可投金额 2.更新账户可用余额 3.新增账户投资记录 4.判断产品是否满标】
                        //（用户标识，产品标识，投资金额）
                        try {
                            bidInfoService.invest(paramMap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            executorService.shutdown();*/
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("投资失败");
        }

        return Result.success();
    }
}
