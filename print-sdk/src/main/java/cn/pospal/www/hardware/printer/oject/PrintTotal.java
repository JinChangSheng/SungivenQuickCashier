package cn.pospal.www.hardware.printer.oject;

import java.util.ArrayList;
import java.util.List;

import cn.pospal.www.hardware.printer.AbstractPrinter;
import cn.pospal.www.hardware.printer.PrintUtil;

/**
 * Created by jinchangsheng on 18/2/8.
 */
public class PrintTotal extends PrintJob {
    private PrintUtil printUtil;
    private String totalStr;

    public PrintTotal(String totalStr) {
        this.totalStr = totalStr;
    }

    @Override
    public synchronized List<String> toPrintStrings(AbstractPrinter printer) {

        this.printer = printer;
        printUtil = new PrintUtil(this.printer);

        ArrayList<String> printStrings = new ArrayList<String>();
        return printH5Super(printStrings);

    }

    private ArrayList<String> printH5Super(ArrayList<String> printStrings) {
            printStrings.add(totalStr);
        return printStrings;
    }
}
