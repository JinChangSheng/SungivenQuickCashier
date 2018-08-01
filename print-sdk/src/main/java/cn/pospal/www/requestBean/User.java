package cn.pospal.www.requestBean;

import java.io.Serializable;

/**
 * Created by jinchangsheng on 18/7/27.
 * UCN
 */

public class User implements Serializable{
    String uuid;
    String code;
    String name;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
