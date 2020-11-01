package com.bjpowernode.p2p.service.loan;

import com.bjpowernode.p2p.model.user.RechargeRecord;

import java.util.List;
import java.util.Map;

/**
 * 张新宇
 * 2020/8/13
 */
public interface RechargeRecordService {
    /**
     * 根据用户标识获取充值记录
     * @param paramMap
     * @return
     */
    List<RechargeRecord> queryRecentlyRechargeRecordListByUid(Map<String, Object> paramMap);


    /**
     * 新增充值记录
     * @param rechargeRecord
     * @return
     */
    int addRechargeRecord(RechargeRecord rechargeRecord);


    /**
     * 根据充值订单号查询充值记录
     * @param out_trade_no
     * @return
     */
    RechargeRecord queryRechargeRecordByRechargeNo(String out_trade_no);

    /**
     *根据充值订单号更新充值记录
     * @param rechargeRecord
     * @return
     */
    int modifyRechargeRecordByRechargeNo(RechargeRecord rechargeRecord);

    void recharge(Map<String, Object> paramMap) throws Exception;
}
