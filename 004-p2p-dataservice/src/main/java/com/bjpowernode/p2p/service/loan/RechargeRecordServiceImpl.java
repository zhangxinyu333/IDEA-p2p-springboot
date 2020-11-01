package com.bjpowernode.p2p.service.loan;

import com.alibaba.dubbo.config.annotation.Service;
import com.bjpowernode.p2p.mapper.loan.RechargeRecordMapper;
import com.bjpowernode.p2p.mapper.user.FinanceAccountMapper;
import com.bjpowernode.p2p.model.user.RechargeRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 张新宇
 * 2020/8/13
 */
@Component
@Service(interfaceClass = RechargeRecordService.class,version = "1.0.0",timeout = 15000)
public class RechargeRecordServiceImpl implements RechargeRecordService {

    @Autowired
    private RechargeRecordMapper rechargeRecordMapper;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Override
    public List<RechargeRecord> queryRecentlyRechargeRecordListByUid(Map<String, Object> paramMap) {


        return rechargeRecordMapper.selectRecentlyRechargeRecordListByUid(paramMap);
    }

    @Override
    public int addRechargeRecord(RechargeRecord rechargeRecord) {
        return rechargeRecordMapper.insertSelective(rechargeRecord);
    }

    @Override
    public RechargeRecord queryRechargeRecordByRechargeNo(String out_trade_no) {
        return rechargeRecordMapper.selectRechargeRecordByRechargeNo(out_trade_no);
    }

    @Override
    public int modifyRechargeRecordByRechargeNo(RechargeRecord rechargeRecord) {
        return rechargeRecordMapper.updateRechargeRecordByRechargeNo(rechargeRecord);
    }

    @Transactional
    @Override
    public void recharge(Map<String, Object> paramMap) throws Exception {

        Integer uid = (Integer) paramMap.get("uid");
        Double recharMoney= (Double) paramMap.get("rechargeMoney");
        String rechargeNo= (String) paramMap.get("rechargeNo");
        //更新账户的可用余额
        int updateFinanceAccountCount=financeAccountMapper.updateFinanceAccountByRecharge(paramMap);
        if (updateFinanceAccountCount<=0){
            throw new Exception();
        }
        //更新充值记录
        RechargeRecord updateRecharge=new RechargeRecord();
        updateRecharge.setRechargeNo(rechargeNo);
        updateRecharge.setRechargeStatus("1");
        int updateRechargeCount=rechargeRecordMapper.updateRechargeRecordByRechargeNo(updateRecharge);
        if (updateRechargeCount<=0){
            throw new Exception();
        }
    }
}
