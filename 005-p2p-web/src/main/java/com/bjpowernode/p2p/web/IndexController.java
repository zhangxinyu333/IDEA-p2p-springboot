package com.bjpowernode.p2p.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bjpowernode.p2p.constant.Constants;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.service.loan.BidInfoService;
import com.bjpowernode.p2p.service.loan.LoanInfoService;
import com.bjpowernode.p2p.service.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 张新宇
 * 2020/7/24
 */
@Controller
public class IndexController {

    @Reference(interfaceClass = LoanInfoService.class,version = "1.0.0",check = false)
    private LoanInfoService loanInfoService;

    @Reference(interfaceClass = UserService.class,version = "1.0.0",check = false)
    private UserService userService;

    @Reference(interfaceClass = BidInfoService.class,version = "1.0.0",check = false)
    private BidInfoService bidInfoService;

    @RequestMapping("/index")
    public String index(Model model){

        //获取平台历史年化收益率
        Double historyAverageRate=loanInfoService.queryHistoryAverageRate();
        model.addAttribute(Constants.HISTORY_AVERAGE_RATE, historyAverageRate);
        //获取平台用户数
        Integer allUserCount=userService.queryAllUserCount();
        model.addAttribute(Constants.ALL_USER_COUNT, allUserCount);
        //获取累计成交额
        Double allBidMoney=bidInfoService.selectAllBidMoney();
        model.addAttribute(Constants.ALL_BID_MONEY,allBidMoney);

        //根据产品类型获取 产品类型，（显示页码-1）*显示条数，显示条数  -》list<产品>
        //准备参数
        Map<String,Object> paramMap=new HashMap<String, Object>();

        paramMap.put("currentPage", 0);//当前页面

        //新手宝 产品类型0 ，显示第1页，显示1个
        paramMap.put("productType", Constants.PRODUCT_TYPE_X);
        paramMap.put("pageSize", 1);//每页显示条数
        List<LoanInfo> xLoanInfoList= loanInfoService.queryLoanInfoListByProductType(paramMap);
        model.addAttribute("xLoanInfoList", xLoanInfoList);

        //优选  产品类型1，显示第1页，显示4个
        paramMap.put("productType", Constants.PRODUCT_TYPE_U);
        paramMap.put("pageSize", 4);
        List<LoanInfo> uLoanInfoList=loanInfoService.queryLoanInfoListByProductType(paramMap);
        model.addAttribute("uLoanInfoList", uLoanInfoList);
        //散标  产品类型2，显示第1页，显示8个
        paramMap.put("productType", Constants.PRODUCT_TYPE_S);
        paramMap.put("pageSize", 8);
        List<LoanInfo> sLoanInfoList=loanInfoService.queryLoanInfoListByProductType(paramMap);
        model.addAttribute("sLoanInfoList", sLoanInfoList);


        return "index";
    }


}
