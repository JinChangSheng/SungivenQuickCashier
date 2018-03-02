package cn.pospal.www.hardware.printer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import cn.pospal.www.app.AppConfig;
import cn.pospal.www.debug.D;
import cn.pospal.www.hardware.printer.oject.PrintJob;
import cn.pospal.www.hardware.printer.oject.TscTestJob;
import cn.pospal.www.otto.DeviceEvent;
import cn.pospal.www.otto.PrintEvent;
import cn.pospal.www.service.fun.PrinterFun;

/**
 * EZPL指令打印机抽象类
 * 一般为标签打印机
 * @author Near Chan
 *
 */
public abstract class AbstractEZPLPrinter extends AbstractPrinter {

	@Override
	public boolean print(PrintJob printJob) throws Exception {
        sendJobStatus(printJob, PrintEvent.STATUS_START);
		if(!isInitedOK()) {
			initPrinter();
			
			if(!isInitedOK()) {
				sendPrinterStatus(DeviceEvent.TYPE_DISABLE);
                sendJobStatus(printJob, PrintEvent.STATUS_ERROR);
				return false;
			}
		}
		
		List<String> printStrings = printJob.toPrintStrings(this);
		
		if(printStrings == null || printStrings.size() == 0) {
			return true;
		}
		
		OutputStream outStream = getPrinterOutputStream();
        // 打印完前检测打印机状态
        if (outStream == null) {
            throw new IOException("无法连接打印机");
        }
        if (getStatus() != STATUS_CONNECTED) {
            throw new PrinterStatusException("打印机错误");
        }

        // 高度和间隔
        outStream.write(("^Q" + AppConfig.labelHeight + "," + AppConfig.labelGap).getBytes());
        outStream.write(LF_CMD);
        // 宽度
		outStream.write(("^W" + AppConfig.labelWidth).getBytes());
        outStream.write(LF_CMD);
        // 明暗度为10
		outStream.write("^H10".getBytes());
        outStream.write(LF_CMD);
        // 打印速度每秒6吋
		outStream.write("^S6".getBytes());
        outStream.write(LF_CMD);
        // 不指定位移值
		outStream.write("^R0".getBytes());
        outStream.write(LF_CMD);
        // 打印一份
        outStream.write("^P1".getBytes());
        outStream.write(LF_CMD);
        // 开始打印
        outStream.write("^L".getBytes());
        outStream.write(LF_CMD);

		int y = AppConfig.labelTopMargin;		// 垂直偏移
		int size = printStrings.size();
		for (int i = 0; i < size; i++) {
			String string = printStrings.get(i);
            D.out("AbstractEZPLPrinter string = " + string);
			// 结束标记不打印
			if(string.equals("finish")) {
                // 结束打印
                outStream.write("E".getBytes());
                outStream.write(LF_CMD);

				y = AppConfig.labelTopMargin;		// 垂直偏移恢复初始值

                // 如果还有下文那重新开始打印
                if (i + 1 < size) {
                    // 开始打印
                    outStream.write("^L".getBytes());
                    outStream.write(LF_CMD);
                }
				continue;
			}

            if (string.startsWith("BR###") && string.endsWith("###")) {
                String barcode = string.replace("BR###", "").replace("###", "");
                outStream.write(("BQ," + AppConfig.labelLeftMargin + "," + y + ",3,3,48,0,1," + barcode).getBytes());
                outStream.write(LF_CMD);

                y += 50;
                continue;
            }

			D.out("TTTTT string = " + string);

            outStream.write(("AZ1," + AppConfig.labelLeftMargin + "," + y + ",1,1,1,0," + string).getBytes("GBK"));
            outStream.write(LF_CMD);
			
			y += AppConfig.labelTextSpace;
		}

        completePrint();
		
		return true;

	}

	@Override
	public void test() {
		PrinterFun.getInstance().registPrinter(this);
		printJobs.add(new TscTestJob());
	}
}
