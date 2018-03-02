package cn.pospal.www.hardware.printer;

import android.content.Context;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.pospal.www.app.AppConfig;
import cn.pospal.www.app.ManagerApp;
import cn.pospal.www.debug.D;
import cn.pospal.www.hardware.printer.oject.PrintJob;
import cn.pospal.www.hardware.printer.oject.ReceiptLabelJob;
import cn.pospal.www.posbase.R;

/**
 * USB转串口标签打印机
 * 
 * @author Near Chan 2012.11.20
 */
public class PrintTscByUsb2Serial extends AbstractTscPrinter {
    public static final String NAME = ManagerApp.getInstance().getString(R.string.printer_name_usb_label);
	private final int MAX_WRITE_TIME = 1500;

	private UsbManager mUsbManager;
	private UsbDevice dev;
	private UsbDeviceConnection conn;
	private boolean isInitedOK = false;
	private Usb2SerialOutputStream outputStream;

	public PrintTscByUsb2Serial(Context context, UsbDevice usbDevice) {
		connectType = CONNECT_TYPE_USB;
		mUsbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
		dev = usbDevice;

        lineWidth = (int)(24 * (AppConfig.labelWidth / 40.0f) + 0.5);
	}

	/**
	 * 判断是否串口打印机
	 * 
	 * @return 2012.11.20
	 */
	public boolean hasSerialPrinter() {
		if (dev == null) {
			return false;
		}

		return true;

	}
	
	/**
	 * 初始化打印机
	 */
	@Override
	public boolean initPrinter() {
		if(!hasSerialPrinter()) {
			return false;
		}
		conn = mUsbManager.openDevice(dev);
		if(conn == null) {
			return false;
		}

        if (dev.getInterfaceCount() == 0) {
            return false;
        }

		if (!conn.claimInterface(dev.getInterface(0), true)) {
			return false;
		}

		int retryTime = 10;
		while (retryTime > 0) {
			int result = conn.controlTransfer(0x21, 34, 0, 0, null, 0, 200);
			if (result == 0) {
				break;
			}
			retryTime--;
		}
		if (retryTime < 0) {
			return false;
		}
		byte[] ctrlBytes = new byte[] { (byte) 0x80, 0x25, 0x00, 0x00, 0x00, 0x00, 0x08 };
		retryTime = 10;
		while (retryTime > 0) {
			int result = conn.controlTransfer(0x21, 32, 0, 0, ctrlBytes, 7, 200);
			if (result == 0) {
				break;
			}
			retryTime--;
		}
		if (retryTime < 0) {
			return false;
		}

		outputStream = new Usb2SerialOutputStream();
		isInitedOK = true;
		D.out("XXXXX initPrinter end");
		return true;

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
		try {
			if(outputStream != null) {
				outputStream.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected InputStream getPrinterInputStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected OutputStream getPrinterOutputStream() {
		return outputStream;
	}

	@Override
    public void initSupportPrintTypes() {
        supportPrintTypes.add(new SupportPrintType(ReceiptLabelJob.class, 0));
	}

    @Override
    public boolean print(PrintJob printJob) throws Exception {
        boolean result = super.print(printJob);
        closePrinter();
        Thread.sleep(50);
        return result;
    }
	
	class Usb2SerialOutputStream extends OutputStream {
		UsbEndpoint endOut = null;// 输出端口 = intf.getEndpoint(1);
		/**
		 * 获取USB输出端点
		 * @return
		 */
		private UsbEndpoint getEndOut() {
			if(endOut == null) {
                if (dev.getInterfaceCount() == 0) {
                    return null;
                }

                UsbInterface intf = dev.getInterface(0);
				
				if(conn.claimInterface(intf, true)) {
					for (int i = 0; i < intf.getEndpointCount(); i++) {
						UsbEndpoint ep = intf.getEndpoint(i);
						if(ep.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK){
							if (ep.getDirection() == UsbConstants.USB_DIR_OUT) {
								endOut = ep;
								break;
							}
						}
					}
				}
			}
			
			return endOut;
		}

		@Override
		public void write(int oneByte) throws IOException {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void write(byte[] buffer) throws IOException {
			D.out("XXXXX PrintEscByUsb2Serial write byte[]");
	        int result = -1;
			UsbEndpoint endOut = getEndOut();
			D.out("XXXXX PrintEscByUsb2Serial endOut = " + endOut);
			if(endOut != null) {
				result = conn.bulkTransfer(endOut, buffer, buffer.length, MAX_WRITE_TIME);
			}
		}
		
		@Override
		public void write(byte[] buffer, int offset, int count) throws IOException {
			D.out("XXXXX PrintEscByUsb2Serial write byte[] count");
			if(buffer.length <= offset || buffer.length < count || buffer.length < (offset + count)) {
				throw new IOException("打印长度出错");
			}
			byte[] bs = new byte[count];
	        for (int i=offset; i<offset+count; i++) {
	        	bs[i-offset] = buffer[i];
	        }

	        int result = -1;
			UsbEndpoint endOut = getEndOut();
			D.out("XXXXX PrintEscByUsb2Serial endOut = " + endOut);
			if(endOut != null) {
				result = conn.bulkTransfer(endOut, bs, bs.length, MAX_WRITE_TIME);
			}
		}
	}

	public UsbDevice getDev() {
		return dev;
	}
}
