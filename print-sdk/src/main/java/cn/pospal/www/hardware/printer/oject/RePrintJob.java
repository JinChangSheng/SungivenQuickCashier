package cn.pospal.www.hardware.printer.oject;

import java.util.List;

import cn.pospal.www.hardware.printer.AbstractPrinter;
import cn.pospal.www.mo.SdkPrinterJob;

/**
 * 重复打印任务
 * Created by Near Chan on 2015/4/21.
 */
public class RePrintJob extends PrintJob {
    private SdkPrinterJob sdkPrinterJob;

    public RePrintJob() {
    }

    public RePrintJob(SdkPrinterJob sdkPrinterJob) {
        this.sdkPrinterJob = sdkPrinterJob;
    }

    public SdkPrinterJob getSdkPrinterJob() {
        return sdkPrinterJob;
    }

	@Override
	public List<String> toPrintStrings(AbstractPrinter printer) {
		this.printer = printer;
		return sdkPrinterJob.getJob();
	}
}
