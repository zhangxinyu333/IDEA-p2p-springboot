package com.bjpowernode.p2p.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bjpowernode.p2p.constant.Constants;
import com.bjpowernode.p2p.model.loan.FinanceAccount;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.model.vo.BidInfoExtUser;
import com.bjpowernode.p2p.model.vo.BidUser;
import com.bjpowernode.p2p.model.vo.PaginationVo;
import com.bjpowernode.p2p.service.loan.BidInfoService;
import com.bjpowernode.p2p.service.loan.LoanInfoService;
import com.bjpowernode.p2p.service.user.FinanceAccountService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 张新宇
 * 2020/7/26
 */
@Controller
public class LoanInfoController {

    @Reference(interfaceClass = LoanInfoService.class,version = "1.0.0",check = false)
    private LoanInfoService loanInfoService;

    @Reference(interfaceClass = BidInfoService.class,version = "1.0.0",check = false)
    private BidInfoService bidInfoService;

    @Reference(interfaceClass = FinanceAccountService.class,version = "1.0.0",check = false)
    private FinanceAccountService financeAccountService;

    @RequestMapping("/loan/loan")
    public String loan(HttpServletRequest request, Model model,
                       @RequestParam(value = "currentPage",defaultValue = "1") Integer currentPage,
                       @RequestParam(value = "ptype",required = false) Integer ptype,
                       @RequestParam(value = "pageSize",defaultValue = "9") Integer pageSize){

        //准备分页查询的参数
        Map<String,Object> paramMap =new HashMap<String, Object>();
        //判断产品类型是否有值
        if (ObjectUtils.allNotNull(ptype)){
        paramMap.put("productType", ptype);
        }
        paramMap.put("currentPage", (currentPage-1)*pageSize);
        paramMap.put("pageSize", pageSize);

        //根据产品类型分页查询产品列表（产品类型，页码，每页显示条数）-》返回每页展示的数据，总记录数
        //返回的数据封装到一个分页模型对象paginationVO, 该对象有两个属性：List、Long
        PaginationVo<LoanInfo> paginationVo = loanInfoService.queryLoanInfoListByPage(paramMap);

        //计算总页数
        int totalPage=paginationVo.getTotal().intValue()/pageSize;
        int mod=paginationVo.getTotal().intValue()%pageSize;
        if (mod>0){
            totalPage=totalPage+1;
        }

        model.addAttribute("loanInfoList", paginationVo.getDataList());
        model.addAttribute("totalRows",paginationVo.getTotal());
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("currentPage", currentPage);
        if (ObjectUtils.allNotNull(ptype)){
            model.addAttribute("ptype", ptype);
        }



        //用户投资排行榜
        List<BidUser> bidUserList=bidInfoService.queryBidUserTop();
        model.addAttribute("bidUserList",bidUserList);

        return "loan";
    }

    @RequestMapping("/loan/loanInfo")
    public String loanInfo(HttpServletRequest request,Model model,
                           @RequestParam(value = "id",required = true) Integer id){

        //根据产品表示获取产品详情
        LoanInfo loanInfo=loanInfoService.queryLoanInfoById(id);
        model.addAttribute("loanInfo", loanInfo);

        //根据产品标识获取最近前10笔投资记录(产品标识，起始下标，截取长度) -》List<>


        Map<String,Object> paramMap=new HashMap<String,Object>();
        paramMap.put("loanId", id);
        paramMap.put("currentPage", 0);
        paramMap.put("pageSize", 10);
        List<BidInfoExtUser> bidInfoExtUserList=bidInfoService.queryRecentlyBidInfoListByLoanId(paramMap);
        model.addAttribute("bidInfoExtUserList", bidInfoExtUserList);

        //从session中获取用户信息
        User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER);

        //判断用户是否存在
        if (ObjectUtils.allNotNull(sessionUser)){
            //根据用户标识获取账户余额
            FinanceAccount financeAccount = financeAccountService.queryFinanceAccountByUid(sessionUser.getId());
            model.addAttribute("financeAccount", financeAccount);
        }

        return "loanInfo";
    }
}
