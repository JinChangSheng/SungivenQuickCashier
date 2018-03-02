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
 * USB标签打印机
 * 
 * @author Near Chan 2012.11.20
 */
public class PrintTscByUSB extends AbstractTscPrinter {
    public static final String NAME = ManagerApp.getInstance().getString(R.string.printer_name_usb_label);
	private final int MAX_WRITE_TIME = 500;

	private UsbManager mUsbManager;
	private UsbDevice dev;
	private UsbDeviceConnection conn;
	private boolean isInitedOK = false;
	private UsbOutputStream outputStream;

	public PrintTscByUSB(Context context, UsbDevice usbDevice) {
		connectType = CONNECT_TYPE_USB;
		mUsbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
		dev = usbDevice;

        lineWidth = (int)(24 * (AppConfig.labelWidth / 40.0f) + 0.5);
	}

	/**
	 * 判断是否USB打印机
	 *
	 * @return 2012.11.20
	 */
	private boolean hasUsbPrinter() {
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
		isInitedOK = hasUsbPrinter();
		D.out("XXXXX isInitedOK = " + isInitedOK);
		if(isInitedOK) {
			outputStream = new UsbOutputStream();
			D.out("XXXXX outputStream = " + outputStream);
			return true;
		}

		return false;
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
	
	class UsbOutputStream extends OutputStream {
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
				D.out("XXXXXX intf = " + intf);
				
				conn = mUsbManager.openDevice(dev);
				D.out("XXXXXX conn = " + conn);
				if(conn.claimInterface(intf, true)) {
					for (int i = 0; i < intf.getEndpointCount(); i++) {
						UsbEndpoint ep = intf.getEndpoint(i);
						D.out("XXXXXX ep = " + ep);
						if(ep.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK){
							if (ep.getDirection() == UsbConstants.USB_DIR_OUT) {
								D.out("XXXXXX ep = endOut");
								endOut = ep;
								break;
							}
						}
					}
				}
			}

			D.out("XXXXXX endOut = " + endOut);
			return endOut;
		}

		@Override
		public void write(int oneByte) throws IOException {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void write(byte[] buffer) throws IOException {
			D.out("XXXXX outputStream write");
	        int result = -1;
			UsbEndpoint endOut = getEndOut();
			if(endOut != null) {
				result = conn.bulkTransfer(endOut, buffer, buffer.length, MAX_WRITE_TIME);
			}
		}
		
		@Override
		public void write(byte[] buffer, int offset, int count) throws IOException {
			D.out("XXXXX outputStream write 222222");
			if(buffer.length <= offset || buffer.length < count || buffer.length < (offset + count)) {
				throw new IOException("打印长度出错");
			}
			byte[] bs = new byte[count];
	        for (int i=offset; i<offset+count; i++) {
	        	bs[i-offset] = buffer[i];
	        }

	        int result = -1;
	        UsbEndpoint endOut = getEndOut();
	        if(endOut != null) {
	        	result = conn.bulkTransfer(endOut, bs, bs.length, MAX_WRITE_TIME);
	        }
		}
	}

	public UsbDevice getDev() {
		return dev;
	}
}
