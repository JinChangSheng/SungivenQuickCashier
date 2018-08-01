package cn.pospal.www.requestBean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by jinchangsheng on 18/7/27.
 * 销售单行
 */

public class OrderLine implements Serializable{
    Integer line;
    String inputCode;
    BigDecimal qty;
    BigDecimal rtlPrc;
    String gdcode;
    String gdgid;
    BigDecimal unitPrice;
    List<FavType> favors;
    String gdname;
    String productBarCode;
    boolean isQtyLimitPromotion;
    Integer codeType;
    String gdTag;
    String discountCode;
    BigDecimal discount;


    public Integer getLine() {
        return line;
    }

    public void setLine(Integer line) {
        this.line = line;
    }

    public String getInputCode() {
        return inputCode;
    }

    public void setInputCode(String inputCode) {
        this.inputCode = inputCode;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getRtlPrc() {
        return rtlPrc;
    }

    public void setRtlPrc(BigDecimal rtlPrc) {
        this.rtlPrc = rtlPrc;
    }

    public String getGdcode() {
        return gdcode;
    }

    public void setGdcode(String gdcode) {
        this.gdcode = gdcode;
    }

    public String getGdgid() {
        return gdgid;
    }

    public void setGdgid(String gdgid) {
        this.gdgid = gdgid;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public List<FavType> getFavors() {
        return favors;
    }

    public void setFavors(List<FavType> favors) {
        this.favors = favors;
    }

    public String getGdname() {
        return gdname;
    }

    public void setGdname(String gdname) {
        this.gdname = gdname;
    }

    public String getProductBarCode() {
        return productBarCode;
    }

    public void setProductBarCode(String productBarCode) {
        this.productBarCode = productBarCode;
    }

    public boolean isQtyLimitPromotion() {
        return isQtyLimitPromotion;
    }

    public void setQtyLimitPromotion(boolean qtyLimitPromotion) {
        isQtyLimitPromotion = qtyLimitPromotion;
    }

    public Integer getCodeType() {
        return codeType;
    }

    public void setCodeType(Integer codeType) {
        this.codeType = codeType;
    }

    public String getGdTag() {
        return gdTag;
    }

    public void setGdTag(String gdTag) {
        this.gdTag = gdTag;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }
}
