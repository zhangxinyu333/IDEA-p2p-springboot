package com.bjpowernode.p2p.model.vo;

import java.io.Serializable;

/**
 * 张新宇
 * 2020/8/23
 */
public class BidUser implements Serializable {

    /**
     * 用户手机号码
     */
    private String phone;

    /**
     * 分数：累计投资金额
     */
    private Double score;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
