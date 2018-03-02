package cn.pospal.www.hardware.printer.oject;

import java.util.ArrayList;
import java.util.List;

import cn.pospal.www.hardware.printer.AbstractPrinter;
import cn.pospal.www.posbase.R;

/**
 * Created by M4600 on 2015/5/28.
 */
public class TscTestJob extends PrintJob {
    @Override
    public List<String> toPrintStrings(AbstractPrinter printer) {
	    this.printer = printer;
        List<String> strings = new ArrayList<String>();
        String testString = getResourceString(R.string.printer_test);
        strings.add(testString);
        strings.add(testString);
        strings.add(testString);
        strings.add("finish");
        return strings;
    }
}
