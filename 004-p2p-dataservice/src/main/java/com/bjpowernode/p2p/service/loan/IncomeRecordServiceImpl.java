package com.bjpowernode.p2p.service.loan;

import com.alibaba.dubbo.config.annotation.Service;
import com.bjpowernode.p2p.constant.Constants;
import com.bjpowernode.p2p.mapper.loan.BidInfoMapper;
import com.bjpowernode.p2p.mapper.loan.IncomeRecordMapper;
import com.bjpowernode.p2p.mapper.loan.LoanInfoMapper;
import com.bjpowernode.p2p.mapper.user.FinanceAccountMapper;
import com.bjpowernode.p2p.model.loan.BidInfo;
import com.bjpowernode.p2p.model.loan.IncomeRecord;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.model.vo.IncomeRecordExtLoanInfo;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 张新宇
 * 2020/8/13
 */
@Component
@Service(interfaceClass = IncomeRecordService.class,version = "1.0.0",timeout = 15000)
public class IncomeRecordServiceImpl implements IncomeRecordService {

    @Autowired
    private IncomeRecordMapper incomeRecordMapper;

    @Autowired
    private LoanInfoMapper loanInfoMapper;

    @Autowired
    private BidInfoMapper bidInfoMapper;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Override
    public List<IncomeRecordExtLoanInfo> queryRecentlyIncomeRecordListByUid(Map<String, Object> paramMap) {
        return incomeRecordMapper.queryRecentlyIncomeRecordListByUid(paramMap);
    }

    @Override
    public void generateIncomePlan() throws Exception {
        //查询产品状态为1的已满标产品 -》 返回List《已满标商品》
        List<LoanInfo> loanInfoList=loanInfoMapper.selectLoanInfoListByProductStatus(1);

        //循环遍历List《已满标产品》，获取到每一个已满标产品
        for (LoanInfo loanInfo : loanInfoList) {
            //根据产品标识获取产品的所有投资记录 —》 返回List《投资记录》
            List<BidInfo> bidInfoList=bidInfoMapper.selectBidInfoByLoanId(loanInfo.getId());

            //循环遍历List《投资记录》，获取到每一个投资记录
            for (BidInfo bidInfo : bidInfoList) {
                //将当前的投资记录生成对应的收益计划
                IncomeRecord incomeRecord=new IncomeRecord();
                incomeRecord.setUid(bidInfo.getUid());
                incomeRecord.setBidId(bidInfo.getId());
                incomeRecord.setLoanId(loanInfo.getId());
                incomeRecord.setBidMoney(bidInfo.getBidMoney());
                incomeRecord.setIncomeStatus(0);//0未返还  1已返还

                //收益时间（Date）=产品满标时间（Date）+产品周期（int天|月）
                Date incomeDate=null;

                //收益金额=投资金额*日利率*投资天数
                Double incomeMoney=null;
                //判断产品类型
                if (Constants.PRODUCT_TYPE_X == loanInfo.getProductType()){
                    //新手宝
                    incomeDate= DateUtils.addDays(loanInfo.getProductFullTime(),loanInfo.getCycle());
                    incomeMoney=bidInfo.getBidMoney()*(loanInfo.getRate()/100/365)*loanInfo.getCycle();
                }else {
                    //优选或散标
                    incomeDate=DateUtils.addMonths(loanInfo.getProductFullTime(),loanInfo.getCycle());
                    incomeMoney=bidInfo.getBidMoney()*(loanInfo.getRate()/100/365)*loanInfo.getCycle()*30;
                }

                incomeMoney=Math.round(Math.pow(10,2)*incomeMoney)/Math.pow(10,2);
                incomeRecord.setIncomeDate(incomeDate);
                incomeRecord.setIncomeMoney(incomeMoney);


                int insertIncomeRecord = incomeRecordMapper.insertSelective(incomeRecord);
                if (insertIncomeRecord<=0){
                    throw new Exception();
                }


            }
            //更新产品的状态为2满标且生成收益计划
            LoanInfo updateLoanInfo = new LoanInfo();
            updateLoanInfo.setId(loanInfo.getId());
            updateLoanInfo.setProductStatus(2);//2满标且生成收益计划
            int updateProductStatusCount = loanInfoMapper.updateByPrimaryKeySelective(updateLoanInfo);
            if (updateProductStatusCount<=0){
                throw new Exception();
            }
        }


    }

    @Override
    public void generateIncomeBack() throws Exception {
        //查询收益状态为0且收益时间与当前时间一致的收益计划-》返回List
        List<IncomeRecord> incomeRecordList=incomeRecordMapper.selectIncomeRecordListByIncomeStatusAndCurDate(0);

        Map<String,Object> paramMap=new HashMap<String, Object>();
        //循环遍历List -》获取每一条收益计划
        for (IncomeRecord incomeRecord : incomeRecordList) {

            paramMap.put("uid", incomeRecord.getUid());
            paramMap.put("bidMoney", incomeRecord.getBidMoney());
            paramMap.put("incomeMoney", incomeRecord.getIncomeMoney());
            //将当前收益返还给对应的用户的账户
            int updateFinanceAccountCount=financeAccountMapper.updateFinanceAccountByIncomeBack(paramMap);
            if (updateFinanceAccountCount<=0){
                throw new Exception();
            }

            //更新当前收益的状态为1已返还
            IncomeRecord updateIncome = new IncomeRecord();
            updateIncome.setId(incomeRecord.getId());
            updateIncome.setIncomeStatus(1);//1已返还
            int updateIncomeCount=incomeRecordMapper.updateByPrimaryKeySelective(updateIncome);
            if (updateIncomeCount<=0){
                throw new Exception();
            }

        }

    }
}
