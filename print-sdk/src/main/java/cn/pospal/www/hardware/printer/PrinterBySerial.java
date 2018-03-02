package cn.pospal.www.hardware.printer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android_serialport_api.SerialPort;
import cn.pospal.www.app.AppConfig;
import cn.pospal.www.hardware.printer.oject.PrintH5Job;
import cn.pospal.www.hardware.printer.oject.PrintJob;
import cn.pospal.www.hardware.printer.oject.ReceiptJob;

/**
 * 纯串口打印机
 * 
 * @author Near Chan 2012.11.20
 */
public class PrinterBySerial extends AbstractEscPrinter {
	private static final String NAME = "USB串口打印机";
	
	private SerialPort mSerialPort = null;
	private OutputStream os;
	private boolean isInitedOK = false;
	private String serialDevice = "";

	public PrinterBySerial(String serialDevice) {
		super();
		connectType = CONNECT_TYPE_COM;
		this.serialDevice = serialDevice;

        lineWidth = AppConfig.isW58 ? 32 : 42;
    }

	/**
	 * 判断是否串口打印机
	 * 
	 * @return 2012.11.20
	 */
	public boolean hasSerialPrinter() {
		return isInitedOK;
	}

	/**
	 * 初始化打印机
	 */
	@Override
	public boolean initPrinter() {
		getSerialPort();
		if(mSerialPort != null && os != null) {
			isInitedOK = true;
		} else {
			isInitedOK = false;
		}
		return true;

	}

	/**
	 * 控制命令
	 */
	public boolean controlCMD(byte[] cmds) {
		return true;
	}
	
	public void getSerialPort() {
		if (mSerialPort == null) {
			int baudrate = 9600;

			try {
				mSerialPort = new SerialPort(new File(serialDevice), baudrate, 0);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(os == null && mSerialPort != null) {
			os = mSerialPort.getOutputStream();
		}
	}

	public void closeSerialPort() {
		if (mSerialPort != null) {
			if(os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				os = null;
			}
			mSerialPort.close();
			mSerialPort = null;
		}
	}
	
	@Override
	public boolean isInitedOK() {
		return isInitedOK;
	}

	@Override
	public boolean hasPrinter() {
		return isInitedOK;
	}

	@Override
	public boolean isConnected() {
		return isInitedOK;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void closePrinter() {
		closeSerialPort();
	}

	@Override
	protected InputStream getPrinterInputStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected OutputStream getPrinterOutputStream() {
		return os;
	}

	@Override
    public void initSupportPrintTypes() {
		supportPrintTypes.add(new SupportPrintType(ReceiptJob.class, 0));
        supportPrintTypes.add(new SupportPrintType(PrintH5Job.class, 0));

	}

    @Override
    public boolean print(PrintJob printJob) throws Exception {
        boolean result = super.print(printJob);
        closePrinter();
        Thread.sleep(50);
        return result;
    }

    @Override
    protected int getStatus() {
        return super.getStatus();
    }
}
