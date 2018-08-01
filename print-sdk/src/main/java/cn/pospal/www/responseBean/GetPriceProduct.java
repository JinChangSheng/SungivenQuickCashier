package cn.pospal.www.responseBean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import cn.pospal.www.requestBean.FavType;

/**
 * Created by jinchangsheng on 18/7/26.
 */

public class GetPriceProduct implements Serializable{
    String inputCode;
    BigDecimal amount;
    String brandName;
    int codeType;
    String gdTag;
    String gdWtMarker;
    String gdcode;
    String gdgid;
    String gdname;
    boolean isQtyLimitPromotion;
    String productBarCode;
    BigDecimal qpc;
    BigDecimal qty;
    BigDecimal rtlPrc;
    String sortName;
    String spec;
    String unit;
    BigDecimal unitPrice;
    List<FavType> favors;

    public String getInputCode() {
        return inputCode;
    }

    public void setInputCode(String inputCode) {
        this.inputCode = inputCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public int getCodeType() {
        return codeType;
    }

    public void setCodeType(int codeType) {
        this.codeType = codeType;
    }

    public String getGdTag() {
        return gdTag;
    }

    public void setGdTag(String gdTag) {
        this.gdTag = gdTag;
    }

    public String getGdWtMarker() {
        return gdWtMarker;
    }

    public void setGdWtMarker(String gdWtMarker) {
        this.gdWtMarker = gdWtMarker;
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

    public String getGdname() {
        return gdname;
    }

    public void setGdname(String gdname) {
        this.gdname = gdname;
    }

    public boolean isQtyLimitPromotion() {
        return isQtyLimitPromotion;
    }

    public void setQtyLimitPromotion(boolean qtyLimitPromotion) {
        isQtyLimitPromotion = qtyLimitPromotion;
    }

    public String getProductBarCode() {
        return productBarCode;
    }

    public void setProductBarCode(String productBarCode) {
        this.productBarCode = productBarCode;
    }

    public BigDecimal getQpc() {
        return qpc;
    }

    public void setQpc(BigDecimal qpc) {
        this.qpc = qpc;
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

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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
}
