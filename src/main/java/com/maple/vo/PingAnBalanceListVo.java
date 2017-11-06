package com.maple.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Maple.Ran on 2017/11/4.
 */
public class PingAnBalanceListVo implements Comparable<PingAnBalanceListVo> {
    private String acctName;//账户姓名
    private String acctNo;//账号
    private String acctOpenBranchName;//开户行
    private String agreementNo;//合约号
    private BigDecimal balance;//余额
    private String updateTime;//更新时间
    private BigDecimal amount;//最近一周扣款成功金额



    public String getAcctName() {
        return acctName;
    }

    public void setAcctName(String acctName) {
        this.acctName = acctName;
    }

    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getAcctOpenBranchName() {
        return acctOpenBranchName;
    }

    public void setAcctOpenBranchName(String acctOpenBranchName) {
        this.acctOpenBranchName = acctOpenBranchName;
    }

    public String getAgreementNo() {
        return agreementNo;
    }

    public void setAgreementNo(String agreementNo) {
        this.agreementNo = agreementNo;
    }


    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public int compareTo(PingAnBalanceListVo o) {
        return this.amount.compareTo(o.amount);
    }
}
