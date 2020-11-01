package com.bjpowernode.p2p.mapper.loan;

import com.bjpowernode.p2p.model.loan.BidInfo;
import com.bjpowernode.p2p.model.vo.BidInfoExtUser;
import com.bjpowernode.p2p.model.vo.BidInfoLoanVO;

import java.util.List;
import java.util.Map;

public interface BidInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BidInfo record);

    int insertSelective(BidInfo record);

    BidInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BidInfo record);

    int updateByPrimaryKey(BidInfo record);

    /**
     * 获取用户总投资金额
     * @return
     */
    Double selectAllBidMoney();

    /**
     * 根据产品标识获取最近10笔投资记录（包含用户信息）
     * @param paramMap
     * @return
     */
    List<BidInfoExtUser> selectRecentlyBidInfoByLoanId(Map<String, Object> paramMap);

    /**
     * 根据用户标识获取最近投资记录（包含产品名称）
     * @param paramMap
     * @return
     */
    List<BidInfoLoanVO> selectRecentlyBidInfoByUid(Map<String, Object> paramMap);

    /**
     * 根据产品标识获取产品的所有投资记录
     * @param loanId
     * @return
     */
    List<BidInfo> selectBidInfoByLoanId(Integer loanId);
}