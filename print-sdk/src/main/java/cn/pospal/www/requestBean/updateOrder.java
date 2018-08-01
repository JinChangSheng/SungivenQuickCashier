package cn.pospal.www.requestBean;

import java.io.Serializable;

/**
 * Created by jinchangsheng on 18/7/30.
 */

public class UpdateOrder implements Serializable{
    String tranId;
    Order order;
    Integer sentState;
    String prepareTranId;
    String datetime;

    public String getTranId() {
        return tranId;
    }

    public void setTranId(String tranId) {
        this.tranId = tranId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Integer getSentState() {
        return sentState;
    }

    public void setSentState(Integer sentState) {
        this.sentState = sentState;
    }

    public String getPrepareTranId() {
        return prepareTranId;
    }

    public void setPrepareTranId(String prepareTranId) {
        this.prepareTranId = prepareTranId;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
