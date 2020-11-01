package com.bjpowernode.p2p.model.vo;

import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.model.loan.BidInfo;

/**
 * 张新宇
 * 2020/7/27
 */
public class BidInfoExtUser extends BidInfo {

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
