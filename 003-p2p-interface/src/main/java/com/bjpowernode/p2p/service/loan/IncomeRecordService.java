package com.bjpowernode.p2p.service.loan;

import com.bjpowernode.p2p.model.vo.IncomeRecordExtLoanInfo;

import java.util.List;
import java.util.Map;

/**
 * 张新宇
 * 2020/8/13
 */
public interface IncomeRecordService {

    /**
     * 根据用户标识获取最近收益记录（包含产品信息）
     * @param paramMap
     * @return
     */
    List<IncomeRecordExtLoanInfo> queryRecentlyIncomeRecordListByUid(Map<String, Object> paramMap);

    /**
     * 生成收益计划
     */
    void generateIncomePlan() throws Exception;

    /**
     * 收益返还
     */
    void generateIncomeBack() throws Exception;

}
