package cn.pospal.www.hardware.printer;

import cn.pospal.www.hardware.printer.oject.PrintJob;

/**
 * 支持打印的类型
 * @author Near Chan
 *
 */
public class SupportPrintType {
	// 支持的类型
	public Class<? extends PrintJob> clazz;
	// 支持的打印机编号
	public long index;
	
	public SupportPrintType() {
		// TODO Auto-generated constructor stub
	}

	public SupportPrintType(Class<? extends PrintJob> clazz, long index) {
		super();
		this.clazz = clazz;
		this.index = index;
	}

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    @Override
	public boolean equals(Object o) {
		if(o != null && o.getClass() == SupportPrintType.class) {
			SupportPrintType other = (SupportPrintType) o;
			return clazz == other.clazz && index == other.index;
		}
		return false;
	}

    @Override
    public String toString() {
        return "SupportPrintType:" + clazz + "-" + index;
    }
}
