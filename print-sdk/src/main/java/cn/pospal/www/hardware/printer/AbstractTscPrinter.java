package cn.pospal.www.hardware.printer;

import android.graphics.Point;

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
 * TSC指令打印机抽象类
 * 一般为标签打印机
 * @author Near Chan
 *
 */
public abstract class AbstractTscPrinter extends AbstractPrinter {

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

		outStream.write(("SIZE " + AppConfig.labelWidth
				+ " mm," + AppConfig.labelHeight + " mm").getBytes());
        outStream.write(LF_CMD);
		outStream.write(("GAP " + AppConfig.labelGap + " mm, 0 mm").getBytes());
        outStream.write(LF_CMD);
		outStream.write("DIRECTION 1".getBytes());
		outStream.write(LF_CMD);
		outStream.write("CODE PAGE 437".getBytes());
        outStream.write(LF_CMD);
		outStream.write("CLS".getBytes());
        outStream.write(LF_CMD);
		int y = AppConfig.labelTopMargin;		// 垂直偏移
		int size = printStrings.size();
		for (int i = 0; i < size; i++) {
			String string = printStrings.get(i);
			// 结束标记不打印
			if(string.equals("finish")) {
				outStream.write("PRINT 1".getBytes());
                outStream.write(LF_CMD);
				y = AppConfig.labelTopMargin;		// 垂直偏移
				outStream.write("CLS".getBytes());
                outStream.write(LF_CMD);
				continue;
			}

            if (string.startsWith("BR###") && string.endsWith("###")) {
                String barcode = string.replace("BR###", "").replace("###", "");
                Point position = new Point(10, y);
                printTscCode(outStream, barcode, position, TYPE_EAN, BRAND_JIABO);
                outStream.write(LF_CMD);

                y += 50;
                continue;
            }

			D.out("TTTTT string = " + string);
            // 繁体使用繁体字体
            if (AppConfig.language.equalsIgnoreCase("zh_TW")
                    || AppConfig.language.equalsIgnoreCase("zh_HK")) {
                outStream.write(("TEXT " + AppConfig.labelLeftMargin + "," + y + ",\"TST24.BF2\",0,1,1,\"" + string + "\"").getBytes("BIG5"));
            } else {
                outStream.write(("TEXT " + AppConfig.labelLeftMargin + "," + y + ",\"TSS24.BF2\",0,1,1,\"" + string + "\"").getBytes("GBK"));
            }
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

	/**
	 * 打印标签机条码或者二维码
	 * @param code
	 */
	public static final void printTscCode(OutputStream os, String code, Point position, int type, int brand)
		throws IOException {
		if (brand == BRAND_JIABO) {
			if (type == TYPE_EAN) {
				String cmdStr = "BARCODE "+ position.x + "," + position.y + ",\"128\",48,0,0,2,2,\"" + code + "\"";
				D.out("XXXXXX printTscCode cmdStr = " + cmdStr);
				os.write(cmdStr.getBytes());
			} else {
				String cmdStr = "QRCODE "+ position.x + "," + position.y + ",H,2,A,0,\""+ code + "\" ";
				os.write(cmdStr.getBytes());
			}
		}
	}

	private static final Point TSC_CODE_POSITION = new Point(50, 20);
	public static final void printTscCode(OutputStream os, String code, int type) throws IOException {
		printTscCode(os, code, TSC_CODE_POSITION, type, BRAND_JIABO);
	}
}
