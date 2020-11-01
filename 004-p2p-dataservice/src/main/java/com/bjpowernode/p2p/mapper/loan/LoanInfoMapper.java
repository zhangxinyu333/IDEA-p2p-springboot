package com.bjpowernode.p2p.mapper.loan;

import com.bjpowernode.p2p.model.loan.LoanInfo;

import java.util.List;
import java.util.Map;

public interface LoanInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LoanInfo record);

    int insertSelective(LoanInfo record);

    LoanInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LoanInfo record);

    int updateByPrimaryKey(LoanInfo record);

    /**
     * 获取历史平均年化收益率
     * @return
     */
    Double selectHistoryAverageRate();


    /**
     * 根据产品类型获取产品列表
     * @return
     */
    List<LoanInfo> selectLoanInfoListByProductType(Map<String, Object> paramMap);

    /**
     * 查询产品总记录数
     * @param paramMap
     * @return
     */
    Long selectTotal(Map<String, Object> paramMap);


    /**
     * 更新产品的剩余可投金额
     * @return
     */
    int updateLeftProductMoneyById(Map<String, Object> paramMap);


    /**
     * 根据产品状态获取产品列表
     * @param productStatus
     * @return
     */
    List<LoanInfo> selectLoanInfoListByProductStatus(Integer productStatus);
}