package cn.pospal.www.responseBean;

import java.io.Serializable;

/**
 * Created by jinchangsheng on 18/7/31.
 * 销售小票打印内容
 */

public class GetTally extends BaseResponse implements Serializable{
    String eleinvoiceInfo;
    String tally;

    public String getEleinvoiceInfo() {
        return eleinvoiceInfo;
    }

    public void setEleinvoiceInfo(String eleinvoiceInfo) {
        this.eleinvoiceInfo = eleinvoiceInfo;
    }

    public String getTally() {
        return tally;
    }

    public void setTally(String tally) {
        this.tally = tally;
    }
}
