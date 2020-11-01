package com.bjpowernode.p2p.model.vo;

import com.bjpowernode.p2p.model.loan.IncomeRecord;
import com.bjpowernode.p2p.model.loan.LoanInfo;

/**
 * 张新宇
 * 2020/8/13
 */
public class IncomeRecordExtLoanInfo extends IncomeRecord {

    private LoanInfo loanInfo;

    public LoanInfo getLoanInfo() {
        return loanInfo;
    }

    public void setLoanInfo(LoanInfo loanInfo) {
        this.loanInfo = loanInfo;
    }
}
