package cn.pospal.www.responseBean;

import java.io.Serializable;

/**
 * Created by jinchangsheng on 18/7/25.
 */

public class BaseResponse implements Serializable{
    /**
     * "echoCode": 0,
     "echoMessage": null,
     */
    public int echoCode;
    public String echoMessage;

    public int getEchoCode() {
        return echoCode;
    }

    public void setEchoCode(int echoCode) {
        this.echoCode = echoCode;
    }

    public String getEchoMessage() {
        return echoMessage;
    }

    public void setEchoMessage(String echoMessage) {
        this.echoMessage = echoMessage;
    }
}
