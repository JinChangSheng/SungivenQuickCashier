package cn.pospal.www.hardware.printer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.pospal.www.app.AppConfig;
import cn.pospal.www.app.Constance;
import cn.pospal.www.app.ManagerApp;
import cn.pospal.www.debug.D;
import cn.pospal.www.hardware.printer.oject.*;
import cn.pospal.www.manager.ManagerData;

import android_serialport_api.SerialPort;
import cn.pospal.www.otto.DeviceEvent;
import cn.pospal.www.posbase.R;

/**
 * 串口打印机行为
 * 
 * @author Near Chan 2012.11.20
 */
public class SerialPrinter extends AbstractEscPrinter {
	protected String NAME = ManagerApp.getInstance().getString(R.string.printer_name_serial);
    public static final String DEFAULT_SERIAL_PRINTER = "/dev/ttySerialPrinter";
	
	private SerialPort mSerialPort = null;
	private OutputStream os;
	private InputStream is;
	protected boolean isInitedOK = false;
    protected String serialPath = DEFAULT_SERIAL_PRINTER;
	
	protected SerialPrinter() {
		connectType = CONNECT_TYPE_COM;
        lineWidth = AppConfig.isW58 ? 32 : 42;
        lastCheckTime = System.currentTimeMillis();
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

	public synchronized void getSerialPort() {
		D.out("SerialPrinter getSerialPort = " + serialPath);
		if (mSerialPort == null) {
			File pathFile = new File(serialPath);
			if (pathFile.exists() && pathFile.canWrite()) {
				int baudratePosition = ManagerData.getBaudratePosition();
                String[] stringArray = ManagerApp.getInstance().getResources().getStringArray(R.array.baudrate_values);
                int baudrate = Integer.parseInt(stringArray[baudratePosition]);
                D.out("SerialPrinter baudrate = " + baudratePosition);

				try {
					mSerialPort = new SerialPort(new File(serialPath), baudrate, 0);
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		if(os == null && mSerialPort != null) {
			os = mSerialPort.getOutputStream();
		}
        if(is == null && mSerialPort != null) {
            is = mSerialPort.getInputStream();
        }

        D.out("mSerialPort = " + mSerialPort);
		if (mSerialPort == null) {
			isInitedOK = false;
			sendPrinterStatus(DeviceEvent.TYPE_DISABLE);
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
            if(is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                is = null;
            }
			mSerialPort.close();
			mSerialPort = null;
		}

		isInitedOK = false;
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
        if (serialPath.equals(DEFAULT_SERIAL_PRINTER)) {
            File pathFile = new File(serialPath);
            if (!pathFile.exists()) {
                return null;
            }
        }

		return NAME;
	}

	@Override
	public void closePrinter() {
		closeSerialPort();
	}
	
	@Override
	public void shutdown() {
		super.shutdown();
	}

	@Override
	protected InputStream getPrinterInputStream() {
		return is;
	}

	@Override
	protected OutputStream getPrinterOutputStream() {
		return os;
	}

	@Override
    public void initSupportPrintTypes() {
        initGeneralPrintType();
	}
	
	@Override
	public boolean equals(Object o) {
		if(o != null && o.getClass() == SerialPrinter.class ) {
			return true;
		}
		
		return false;
	}

    @Override
    protected int getStatus() {
	    D.out("SerialPrinter getStatus");
        if (mSerialPort != null && os != null && is != null) {
			// 商通不进行检测返回连接成功（否则打印机会变慢）
			if (AppConfig.company.equals(Constance.COMPANY_SEMTOM)) {
				return STATUS_CONNECTED;
			}

            try {
                os.write(SERIAL_STATUS_CMD);
                os.flush();
                // 检测指令最多500ms就返回
	            try {
		            Thread.sleep(500);
	            } catch (InterruptedException e) {
		            e.printStackTrace();
	            }
	            if (is.available() > 0) {
		            byte[] status = new byte[1];
		            int len = is.read(status);
		            D.out("111 getStatus len = " + len);
		            for (int i = 0; i < len; i++) {
			            D.out("status[" + i + "]=" + status[i]);
		            }
		            if (len == 1) {
			            // 0000 1000 = 0x08 // 屏蔽其他干扰只获取联机状态（出现错误为脱机状态）
			            if ((status[0] & 0x08) == 8) {
				            isInitedOK = false;
				            sendPrinterStatus(DeviceEvent.TYPE_DISABLE);
				            return STATUS_ERROR;
			            } else {
				            sendPrinterStatus(DeviceEvent.TYPE_CONNECT);
				            return STATUS_CONNECTED;
			            }
		            } else {
			            isInitedOK = false;
			            sendPrinterStatus(DeviceEvent.TYPE_DISABLE);
			            return STATUS_ERROR;
		            }
	            }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

	    D.out("SerialPrinter getStatus 2222");
	    isInitedOK = false;
	    sendPrinterStatus(DeviceEvent.TYPE_DISABLE);
        return STATUS_ERROR;

    }

    public String getSerialPath() {
        return serialPath;
    }
}
