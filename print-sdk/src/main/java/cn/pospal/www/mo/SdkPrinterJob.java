package cn.pospal.www.mo;

import java.io.Serializable;
import java.util.List;

/**
 * 打印机任务
 * 用于打印机缺纸或者连不上恢复性打印
 * Created by Near Chan on 2015/4/17.
 */
public class SdkPrinterJob implements Serializable {
    private static final long serialVersionUID = -5318025846119618918L;

    public transient static final int STATUS_FAIL = 0;
    public transient static final int STATUS_SUCCESS = 1;

    private long uid;
    private String datetime;
    private List<String> job;
    private String printerName;
    private String clazz;
    private long index;
    private int status;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public List<String> getJob() {
        return job;
    }

    public void setJob(List<String> job) {
        this.job = job;
    }

    public String getPrinterName() {
        return printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
