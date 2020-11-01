package com.bjpowernode.p2p.service.user;

import com.bjpowernode.p2p.model.loan.FinanceAccount;

/**
 * 张新宇
 * 2020/8/7
 */
public interface FinanceAccountService {
    /**
     * 根据用户标识获取用户信息
     * @param uid
     * @return
     */
    FinanceAccount queryFinanceAccountByUid(Integer uid);
}
