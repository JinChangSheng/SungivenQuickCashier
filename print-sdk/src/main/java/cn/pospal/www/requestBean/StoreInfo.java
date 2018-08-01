package cn.pospal.www.requestBean;

import java.io.Serializable;

/**
 * Created by jinchangsheng on 18/7/27.
 */

public class StoreInfo implements Serializable{
    String storeNo;
    String storeName;
    String enterpriseName;//企业名称
    String enterpriseCode;//企业代码
    String saVersion;//系统版本

    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getEnterpriseCode() {
        return enterpriseCode;
    }

    public void setEnterpriseCode(String enterpriseCode) {
        this.enterpriseCode = enterpriseCode;
    }

    public String getSaVersion() {
        return saVersion;
    }

    public void setSaVersion(String saVersion) {
        this.saVersion = saVersion;
    }
}
