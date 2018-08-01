package cn.pospal.www.otto;

import cn.pospal.www.hardware.printer.oject.PrintJob;

/**
 * 打印事件，可以监听打印任务
 * 目前暂时用于标签打印
 * Created by Administrator on 2017/7/14.
 */
public class PrintEvent {
    public static final int STATUS_WAIT = 0;
    public static final int STATUS_START = 1;
    public static final int STATUS_ING = 2;
    public static final int STATUS_END = 3;
    public static final int STATUS_ERROR = 4;
    // 这个uid可以是ticketUid也可以是productUid，等等
    private long uid;
    // 类型
    private Class<? extends PrintJob> clazz;
    // 打印总数
    private int qty;
    // 目前打印位置
    private int index;
    // 打印状态
    private int status;
    //需要打印的字符串
    private int printStr;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public Class<? extends PrintJob> getClazz() {
        return clazz;
    }

    public void setClazz(Class<? extends PrintJob> clazz) {
        this.clazz = clazz;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPrintStr() {
        return printStr;
    }

    public void setPrintStr(int printStr) {
        this.printStr = printStr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PrintEvent event = (PrintEvent) o;

        return uid == event.uid;

    }

    @Override
    public String toString() {
        return "PrintEvent{" +
                "uid=" + uid +
                ", clazz=" + clazz +
                ", status=" + status +
                '}';
    }
}
