package cn.pospal.www.hardware.printer.oject;

import java.util.List;

import cn.pospal.www.hardware.printer.AbstractPrinter;

/**
 * 打印机测试任务
 * 测试打印机是否联通
 * Created by Near Chan on 2015/6/29.
 */
public class PrinterTestJob extends PrintJob {
    @Override
    public List<String> toPrintStrings(AbstractPrinter printer) {
        return null;
    }
}
