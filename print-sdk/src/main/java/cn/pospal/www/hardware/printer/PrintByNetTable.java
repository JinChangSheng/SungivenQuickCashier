package cn.pospal.www.hardware.printer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import cn.pospal.www.app.ManagerApp;
import cn.pospal.www.debug.D;
import cn.pospal.www.hardware.printer.oject.PrintJob;
import cn.pospal.www.manager.ManagerData;
import cn.pospal.www.posbase.R;
import cn.pospal.www.util.SystemUtil;

/**
 * 通过网络打印（WIFI或者直连）
 * @author NearSOC
 * 2013.02.18
 */
public class PrintByNetTable extends PrintByNet {
	public PrintByNetTable(int type) {
		super(type);
	}

	private static final String NAME = ManagerApp.getInstance().getString(R.string.printer_name_table);
	
	@Override
	public boolean initPrinter() {
		printerIpStr = ManagerData.getTablePrinterIpInfo();
		D.out("DDDDD printerIpStr = " + printerIpStr);
		if(!printerIpStr.equals("") && SystemUtil.isIp(printerIpStr)) {
			testIp(printerIpStr);
		}
		
		try {
			Thread.sleep(INIT_DELAY_TIME);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	@Override
	public boolean isInitedOK() {
		return isInitedOK;
	}

	@Override
	public boolean hasPrinter() {
		return SystemUtil.isIp(printerIpStr);
	}

	@Override
	public boolean isConnected() {
		return socket == null ? false : ((socket.isClosed() ? false : socket.isConnected()));
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void closePrinter() {
		if(outStream != null) {
			try {
				outStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				outStream = null;
			}
		}
		
		if(inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				inputStream = null;
			}
		}
		
		if(socket != null && !socket.isClosed()) {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				socket = null;
			}
		}
	}

	@Override
	protected InputStream getPrinterInputStream() {
		return null;
	}

	@Override
	protected OutputStream getPrinterOutputStream() {
		if(!isInitedOK || outStream == null) {
			try {
				if(socket == null || socket.isClosed()) {
					socket = new Socket(printerIpStr, Constance.NET_PRINTER_PORT_JIABO);
				}
				outStream = socket.getOutputStream();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return outStream;
	}
	
	@Override
	public boolean print(PrintJob printJob) throws Exception {
		boolean result = super.print(printJob);
		closePrinter();
		return result;
	}

	@Override
    public void initSupportPrintTypes() {

	}

	@Override
	public boolean equals(Object o) {
		boolean typeEquals = super.equals(o);
		if (typeEquals) {
			PrintByNetTable other = (PrintByNetTable) o;

			if (other.printerIpStr != null) {
				return other.printerIpStr.equals(printerIpStr);
			}
		}

		return false;
	}

}
