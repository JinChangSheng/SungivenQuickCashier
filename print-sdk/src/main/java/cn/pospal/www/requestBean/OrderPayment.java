package cn.pospal.www.requestBean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by jinchangsheng on 18/7/27.
 * 销售单付款行
 */

public class OrderPayment implements Serializable{
    String payType;
    BigDecimal total;
    String payId;
    String payStoreOrdNum;
    String accountType;
    String number;
    BigDecimal beforePayBalance;
    BigDecimal afterPayBalance;
    String cardType;
    String payWayCode;
    String payWayName;

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getPayStoreOrdNum() {
        return payStoreOrdNum;
    }

    public void setPayStoreOrdNum(String payStoreOrdNum) {
        this.payStoreOrdNum = payStoreOrdNum;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public BigDecimal getBeforePayBalance() {
        return beforePayBalance;
    }

    public void setBeforePayBalance(BigDecimal beforePayBalance) {
        this.beforePayBalance = beforePayBalance;
    }

    public BigDecimal getAfterPayBalance() {
        return afterPayBalance;
    }

    public void setAfterPayBalance(BigDecimal afterPayBalance) {
        this.afterPayBalance = afterPayBalance;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getPayWayCode() {
        return payWayCode;
    }

    public void setPayWayCode(String payWayCode) {
        this.payWayCode = payWayCode;
    }

    public String getPayWayName() {
        return payWayName;
    }

    public void setPayWayName(String payWayName) {
        this.payWayName = payWayName;
    }
}
