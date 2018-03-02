package cn.pospal.www.hardware.printer.oject;

import android.support.annotation.StringRes;

import java.util.List;

import cn.pospal.www.app.ManagerApp;
import cn.pospal.www.hardware.printer.AbstractPrinter;

/**
 * 打印对象抽象类
 * @author Near Chan
 *
 */
public abstract class PrintJob implements Cloneable {
	public static final int MAX_LENGTH = 1024;
	
	public static final int STATUS_WATTING = 0;
	public static final int STATUS_PRINTING = 1;
	public static final int STATUS_PRINTED = 2;
	public static final int STATUS_RESTING = 3;      // 打印完休息状态，有些打印机不能持续打印

	private int index;		// 指定的打印机编号
	private int status = STATUS_WATTING;		// 打印状态
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getStatus() {
		return status;
	}
	public synchronized void setStatus(int status) {
		this.status = status;
	}
    protected int maxLineLen = 32;
    public int getMaxLineLen() {
        return maxLineLen;
    }

    public void setMaxLineLen(int maxLineLen) {
        this.maxLineLen = maxLineLen;
    }
    private boolean retryPrint;

    public boolean isRetryPrint() {
        return retryPrint;
    }

    public void setRetryPrint(boolean retryPrint) {
        this.retryPrint = retryPrint;
    }

    @Override
	public boolean equals(Object o) {
        return o != null && o.getClass() == getClass() && index == ((PrintJob) o).getIndex();
    }
	
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

    protected String getResourceString(@StringRes int strId) {
        return ManagerApp.getInstance().getString(strId);
    }

	protected AbstractPrinter printer;
	public void setPrinter(AbstractPrinter printer) {
		this.printer = printer;
	}
	
	/**
	 * 将打印对象转成打印字符串List
	 * @return
	 */
	public abstract List<String> toPrintStrings(AbstractPrinter printer);

    protected long uid;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    /**
     * 打印次数，默认1
     */
    protected int cnt = 1;

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    /**
     * 是否需要跟踪打印流程
     * 如果需要跟踪整个打印流程整个参数要设置true
     * 默认不跟踪
     */
    protected boolean haveToTrace = false;

    public boolean isHaveToTrace() {
        return haveToTrace;
    }

    public void setHaveToTrace(boolean haveToTrace) {
        this.haveToTrace = haveToTrace;
    }
}
