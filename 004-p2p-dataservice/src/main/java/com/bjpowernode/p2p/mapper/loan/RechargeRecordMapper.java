package com.bjpowernode.p2p.mapper.loan;

import com.bjpowernode.p2p.model.user.RechargeRecord;

import java.util.List;
import java.util.Map;

public interface RechargeRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RechargeRecord record);

    int insertSelective(RechargeRecord record);

    RechargeRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RechargeRecord record);

    int updateByPrimaryKey(RechargeRecord record);

    /**
     * 根据用户标识获取用户信息
     * @param paramMap
     * @return
     */
    List<RechargeRecord> selectRecentlyRechargeRecordListByUid(Map<String, Object> paramMap);

    /**
     * 根据充值订单号查询充值记录
     * @param out_trade_no
     * @return
     */
    RechargeRecord selectRechargeRecordByRechargeNo(String out_trade_no);

    /**
     * 根据充值订单号更新充值记录
     * @param rechargeRecord
     * @return
     */
    int updateRechargeRecordByRechargeNo(RechargeRecord rechargeRecord);
}