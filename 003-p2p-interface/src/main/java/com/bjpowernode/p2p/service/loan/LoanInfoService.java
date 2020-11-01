package com.bjpowernode.p2p.service.loan;

import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.model.vo.PaginationVo;

import java.util.List;
import java.util.Map;

/**
 * 张新宇
 * 2020/7/25
 */
public interface LoanInfoService {

    /**
     *获取平台历史年化收益率
     * @return
     */
    Double queryHistoryAverageRate();

    /**
     * 根据产品类型获取产品列表
     * @param paramMap
     * @return
     */
    List<LoanInfo> queryLoanInfoListByProductType(Map<String, Object> paramMap);

    /**
     * 根据产品类型分页查询产品列表
     * @param paramMap
     * @return
     */
    PaginationVo<LoanInfo> queryLoanInfoListByPage(Map<String, Object> paramMap);

    /**
     * 根据产品标识获取产品详情
     * @param id
     * @return
     */
    LoanInfo queryLoanInfoById(Integer id);
}
