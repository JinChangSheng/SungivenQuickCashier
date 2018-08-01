package cn.pospal.www.requestBean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by jinchangsheng on 18/7/27.
 * 商品优惠信息
 */

public class FavType implements Serializable{
    String favType;
    BigDecimal favTotal;//优惠金额
    String promCls;//促销单类型
    String promNum;//促销单单号
    String promLine;//促销单行号
    String cause;//促销原因

    public String getFavType() {
        return favType;
    }

    public void setFavType(String favType) {
        this.favType = favType;
    }

    public BigDecimal getFavTotal() {
        return favTotal;
    }

    public void setFavTotal(BigDecimal favTotal) {
        this.favTotal = favTotal;
    }

    public String getPromCls() {
        return promCls;
    }

    public void setPromCls(String promCls) {
        this.promCls = promCls;
    }

    public String getPromNum() {
        return promNum;
    }

    public void setPromNum(String promNum) {
        this.promNum = promNum;
    }

    public String getPromLine() {
        return promLine;
    }

    public void setPromLine(String promLine) {
        this.promLine = promLine;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }
}
