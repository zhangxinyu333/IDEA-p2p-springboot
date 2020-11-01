package com.bjpowernode.p2p.mapper.loan;

import com.bjpowernode.p2p.model.loan.IncomeRecord;
import com.bjpowernode.p2p.model.vo.IncomeRecordExtLoanInfo;

import java.util.List;
import java.util.Map;

public interface IncomeRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(IncomeRecord record);

    int insertSelective(IncomeRecord record);

    IncomeRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IncomeRecord record);

    int updateByPrimaryKey(IncomeRecord record);

    /**
     * 根据用户标识获取最近的收益记录（包含产品的信息）
     * @param paramMap
     * @return
     */
    List<IncomeRecordExtLoanInfo> queryRecentlyIncomeRecordListByUid(Map<String, Object> paramMap);

    /**
     * 查询收益状态为0且收益时间与当前时间一致的收益计划
     * @param incomeStatus
     * @return
     */
    List<IncomeRecord> selectIncomeRecordListByIncomeStatusAndCurDate(Integer incomeStatus);
}