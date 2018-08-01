package cn.pospal.www.responseBean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by jinchangsheng on 18/7/30.
 */

public class PaySubmit extends BaseResponse implements Serializable{
    /**
     * {
     "printInfo": "支付宝支付",
     "payState": 0,
     "amount": 3,
     "storeOrdNum": "201703091716053",
     "payWayCode": "aliPay",
     "payWayName": "支付宝",
     "accountChannelList ": [
     {
     "accountChannel": "支付宝红包",
     "amount": 0.5
     },
     {
     "accountChannel ": "支付宝账户",
     "amount": 1.5
     }
     ],
     "favourAmount": 2,
     "favAmountChannel": 0.5,
     "favAmountMerchant": 1.5,
     "account": "bes***@hotmail.com"
     }
     */
    Integer payState;
    BigDecimal amount;
    String storeOrdNum;
    String printInfo;
    String payWayCode;
    String payWayName;
    List<accountChannel> accountChannelList;
    BigDecimal favourAmount;//支付总优惠金额
    BigDecimal favAmountChannel;//第三方支付系统承担优惠总额
    BigDecimal favAmountMerchant;//商家承担优惠总额
    String account;//顾客的第三方支付账户名称,会员相关支付返回会员号

    class accountChannel implements Serializable{
        String accountChannel;
        BigDecimal amount;

        public String getAccountChannel() {
            return accountChannel;
        }

        public void setAccountChannel(String accountChannel) {
            this.accountChannel = accountChannel;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }
    }

    public Integer getPayState() {
        return payState;
    }

    public void setPayState(Integer payState) {
        this.payState = payState;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStoreOrdNum() {
        return storeOrdNum;
    }

    public void setStoreOrdNum(String storeOrdNum) {
        this.storeOrdNum = storeOrdNum;
    }

    public String getPrintInfo() {
        return printInfo;
    }

    public void setPrintInfo(String printInfo) {
        this.printInfo = printInfo;
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

    public List<accountChannel> getAccountChannelList() {
        return accountChannelList;
    }

    public void setAccountChannelList(List<accountChannel> accountChannelList) {
        this.accountChannelList = accountChannelList;
    }

    public BigDecimal getFavourAmount() {
        return favourAmount;
    }

    public void setFavourAmount(BigDecimal favourAmount) {
        this.favourAmount = favourAmount;
    }

    public BigDecimal getFavAmountChannel() {
        return favAmountChannel;
    }

    public void setFavAmountChannel(BigDecimal favAmountChannel) {
        this.favAmountChannel = favAmountChannel;
    }

    public BigDecimal getFavAmountMerchant() {
        return favAmountMerchant;
    }

    public void setFavAmountMerchant(BigDecimal favAmountMerchant) {
        this.favAmountMerchant = favAmountMerchant;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
