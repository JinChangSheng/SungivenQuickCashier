package cn.pospal.www.hardware.printer.oject;

import java.util.ArrayList;

import cn.pospal.www.hardware.printer.AbstractPrinter;
import cn.pospal.www.hardware.printer.PrintUtil;

/**
 * 收据打印任务
 * @author NearSOC
 * 2013.02.18
 */
public class ReceiptLabelJob extends PrintJob{

	private PrintUtil printUtil;


	@Override
	public synchronized ArrayList<String> toPrintStrings(AbstractPrinter printer) {
		this.printer = printer;
		printUtil = new PrintUtil(printer);

        ArrayList<String> printStrings = new ArrayList<String>();

        return printStrings;
	}

}
