package com.bjpowernode.p2p.model.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 张新宇
 * 2020/8/13
 */
public class BidInfoLoanVO implements Serializable {

    private String productName;

    private Double bidMoney;

    private Date bidTime;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getBidMoney() {
        return bidMoney;
    }

    public void setBidMoney(Double bidMoney) {
        this.bidMoney = bidMoney;
    }

    public Date getBidTime() {
        return bidTime;
    }

    public void setBidTime(Date bidTime) {
        this.bidTime = bidTime;
    }
}
