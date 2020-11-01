package com.bjpowernode.p2p.service.loan;

import com.bjpowernode.p2p.model.vo.BidInfoExtUser;
import com.bjpowernode.p2p.model.vo.BidInfoLoanVO;
import com.bjpowernode.p2p.model.vo.BidUser;

import java.util.List;
import java.util.Map;

/**
 * 张新宇
 * 2020/7/25
 */
public interface BidInfoService {

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
    List<BidInfoExtUser> queryRecentlyBidInfoListByLoanId(Map<String, Object> paramMap);

    /**
     *根据用户标识获取投资记录（包含产品名称）
     * @param paramMap
     * @return
     */
    List<BidInfoLoanVO> queryRecentlyBidInfoListByUid(Map<String, Object> paramMap);

    /**
     * 用户投资
     * @param paramMap
     */
    void invest(Map<String, Object> paramMap) throws Exception;

    /**
     * 获取用户投资排行榜
     * @return
     */
    List<BidUser> queryBidUserTop();

}
