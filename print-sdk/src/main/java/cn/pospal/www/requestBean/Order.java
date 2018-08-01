package cn.pospal.www.requestBean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by jinchangsheng on 18/7/27.
 * 销售单
 */

public class Order implements Serializable{
    String flowNo;//服务器返回
    User filler;
    String filDate;
    BigDecimal shouldPayTotal;
    BigDecimal favTotal = BigDecimal.ZERO;//优惠金额
    MemberInfo memberInfo;
    List<OrderLine> products;
    List<OrderPayment> payments;
    String limitPromotionInfo;//限量促销提醒信息
    String limitPromotedProducts;
    String purchasePromotionInfo;//内购限量促销提醒信息
    String purchasePromotedProducts;

    public String getFlowNo() {
        return flowNo;
    }

    public void setFlowNo(String flowNo) {
        this.flowNo = flowNo;
    }

    public User getFiller() {
        return filler;
    }

    public void setFiller(User filler) {
        this.filler = filler;
    }

    public String getFilDate() {
        return filDate;
    }

    public void setFilDate(String filDate) {
        this.filDate = filDate;
    }

    public BigDecimal getShouldPayTotal() {
        return shouldPayTotal;
    }

    public void setShouldPayTotal(BigDecimal shouldPayTotal) {
        this.shouldPayTotal = shouldPayTotal;
    }

    public BigDecimal getFavTotal() {
        return favTotal;
    }

    public void setFavTotal(BigDecimal favTotal) {
        this.favTotal = favTotal;
    }

    public MemberInfo getMemberInfo() {
        return memberInfo;
    }

    public void setMemberInfo(MemberInfo memberInfo) {
        this.memberInfo = memberInfo;
    }

    public List<OrderLine> getProducts() {
        return products;
    }

    public void setProducts(List<OrderLine> products) {
        this.products = products;
    }

    public List<OrderPayment> getPayments() {
        return payments;
    }

    public void setPayments(List<OrderPayment> payments) {
        this.payments = payments;
    }

    public String getLimitPromotionInfo() {
        return limitPromotionInfo;
    }

    public void setLimitPromotionInfo(String limitPromotionInfo) {
        this.limitPromotionInfo = limitPromotionInfo;
    }

    public String getLimitPromotedProducts() {
        return limitPromotedProducts;
    }

    public void setLimitPromotedProducts(String limitPromotedProducts) {
        this.limitPromotedProducts = limitPromotedProducts;
    }

    public String getPurchasePromotionInfo() {
        return purchasePromotionInfo;
    }

    public void setPurchasePromotionInfo(String purchasePromotionInfo) {
        this.purchasePromotionInfo = purchasePromotionInfo;
    }

    public String getPurchasePromotedProducts() {
        return purchasePromotedProducts;
    }

    public void setPurchasePromotedProducts(String purchasePromotedProducts) {
        this.purchasePromotedProducts = purchasePromotedProducts;
    }
}
