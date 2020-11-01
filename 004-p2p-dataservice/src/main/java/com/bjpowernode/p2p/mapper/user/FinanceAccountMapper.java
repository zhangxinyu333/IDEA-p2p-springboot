package com.bjpowernode.p2p.mapper.user;

import com.bjpowernode.p2p.model.loan.FinanceAccount;

import java.util.Map;

public interface FinanceAccountMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FinanceAccount record);

    int insertSelective(FinanceAccount record);

    FinanceAccount selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FinanceAccount record);

    int updateByPrimaryKey(FinanceAccount record);

    /**
     * 根据用户标识获取用户信息
     * @param uid
     * @return
     */
    FinanceAccount selectFinanceAccountByUid(Integer uid);

    /**
     * 更新账户可用余额（账户投资）
     * @param paramMap
     * @return
     */
    int updateFinanceAccountByUid(Map<String, Object> paramMap);

    /**
     * 更新账户可用余额（收益返还）
     * @param paramMap
     * @return
     */
    int updateFinanceAccountByIncomeBack(Map<String, Object> paramMap);

    /**
     * 更新账户可用余额（账户充值）
     * @param paramMap
     * @return
     */
    int updateFinanceAccountByRecharge(Map<String, Object> paramMap);
}