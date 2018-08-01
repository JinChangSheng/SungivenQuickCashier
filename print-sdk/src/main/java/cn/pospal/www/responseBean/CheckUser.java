package cn.pospal.www.responseBean;

import java.io.Serializable;

import cn.pospal.www.requestBean.StoreInfo;
import cn.pospal.www.requestBean.User;

/**
 * Created by jinchangsheng on 18/7/25.
 */

public class CheckUser extends BaseResponse implements Serializable{
    public User user;
    public StoreInfo storeInfo;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public StoreInfo getStoreInfo() {
        return storeInfo;
    }

    public void setStoreInfo(StoreInfo storeInfo) {
        this.storeInfo = storeInfo;
    }
}
