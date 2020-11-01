package com.bjpowernode.p2p.service.user;

import com.alibaba.dubbo.config.annotation.Service;
import com.bjpowernode.p2p.mapper.user.FinanceAccountMapper;
import com.bjpowernode.p2p.model.loan.FinanceAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 张新宇
 * 2020/8/7
 */
@Component
@Service(interfaceClass = FinanceAccountService.class,version = "1.0.0",timeout = 15000)
public class FinanceAccountServiceImpl implements FinanceAccountService {

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Override
    public FinanceAccount queryFinanceAccountByUid(Integer uid) {
        return financeAccountMapper.selectFinanceAccountByUid(uid);
    }
}
