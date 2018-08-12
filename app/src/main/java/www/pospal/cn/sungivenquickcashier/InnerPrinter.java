package www.pospal.cn.sungivenquickcashier;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.pospal.www.app.AppConfig;
import cn.pospal.www.app.ManagerApp;
import cn.pospal.www.debug.D;
import cn.pospal.www.hardware.printer.AbstractEscPrinter;
import cn.pospal.www.hardware.printer.PrintUtil;
import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

/**
 * 串口打印机行为
 * 
 * @author Near Chan 2012.11.20
 */
public class InnerPrinter extends AbstractEscPrinter {
	public static final String NAME = "商米打印机";

	private PosOutputStream os;
	private boolean isInitedOK = false;
	private boolean enableCallback = false;

	private static InnerPrinter instance;
	public static synchronized InnerPrinter getInstance() {
		if(instance == null) {
			instance = new InnerPrinter();
//            charset = "UTF-8";
		}
		return instance;
	}

	private InnerPrinter() {
		LF_CMD = new byte[]{'\n'};
		LF = new String(LF_CMD);
		OPEN_DRAWER_CMD = new byte[]{27, 112, 0, 0x20, 0x30};
		lineWidth = AppConfig.isW58 ? 32 : 42;
        footSpaceLines = 2;
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
		return isInitedOK;
	}

	/**
	 * 控制命令
	 */
	public boolean controlCMD(byte[] cmds) {
		return true;
	}

	public synchronized void getSerialPort() {
		D.out("getSerialPort");
		Intent intent=new Intent();
		intent.setPackage("woyou.aidlservice.jiuiv5");
		intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
		ManagerApp.getInstance().startService(intent);
		ManagerApp.getInstance().bindService(intent, connService, Context.BIND_AUTO_CREATE);
	}

	public void closeSerialPort() {
		D.out("closeSerialPort");
		if (woyouService != null) {
			try {
				ManagerApp.getInstance().unbindService(connService);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Intent intent = new Intent();
			intent.setPackage("woyou.aidlservice.jiuiv5");
			intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
			ManagerApp.getInstance().stopService(intent);
		}

		isInitedOK = false;
		instance = null;
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
	public void shutdown() {
		super.shutdown();

		instance = null;
	}

    @Override
    protected void cutReceipt() {
        if(woyouService != null) {
            try {
                woyouService.cutPaper(callback);
                woyouService.sendRAWData(LF_CMD, callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void openDrawer() {
        if(woyouService != null) {
            try {
                woyouService.openDrawer(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
	protected InputStream getPrinterInputStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected OutputStream getPrinterOutputStream() {
		D.out("getPrinterOutputStream = " + os);
		return os;
	}

	class PosOutputStream extends OutputStream {
		private boolean openSuccess = false;

		public boolean openOutputStream() {
			openSuccess = true;
			return openSuccess;
		}

		public boolean closeOutputStream() {
			return true;
		}

		@Override
		public void write(int oneByte) throws IOException {
			// TODO Auto-generated method stub

		}

		@Override
		public void write(byte[] buffer) throws IOException {
			D.out("XXXX PosOutputStream write buffer.len = " + buffer.length);
			if(openSuccess) {
				try {
					woyouService.sendRAWData(buffer, callback);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void write(byte[] buffer, int offset, int count) throws IOException {
			if(openSuccess) {
				D.out("XXXX PosOutputStream openSuccess 222");
				if(buffer.length <= offset || buffer.length < count || buffer.length < (offset + count)) {
					throw new IOException("打印长度出错");
				}
				byte[] bs = new byte[count];
				for (int i=offset; i<offset+count; i++) {
					bs[i-offset] = buffer[i];
				}
				try {
					woyouService.sendRAWData(bs, callback);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

    @Override
    public int printBmp(OutputStream os, Bitmap bmp, int brand) {
        if (os != null) {
            try {
                os.write(PrintUtil.ALIGN_CENTER);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            woyouService.printBitmap(bmp, callback);
            woyouService.lineWrap(1, null);
            if (enableCallback) {
                wait();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (os != null) {
            try {
                os.write(PrintUtil.ALIGN_LEFT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return callbackResult ? PRINT_SUCCESS : PRINT_FAIL;
    }

	@Override
	protected void printBarcode(String barcode, int type) {
		D.out("printBarcode");
		if (os != null) {
			try {
				os.write(PrintUtil.ALIGN_CENTER);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (type == TYPE_EAN) {
			try {
				woyouService.printBarCode(barcode, 8, 80, 2, 0, callback);
                woyouService.lineWrap(1, null);
				if (enableCallback) {
					wait();
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else if (type == TYPE_QR) {
			try {
				woyouService.printQRCode(barcode, 5, 2, callback);
                woyouService.lineWrap(1, null);
				if (enableCallback) {
					wait();
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (os != null) {
			try {
				os.write(PrintUtil.ALIGN_LEFT);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

    @Override
    public void initSupportPrintTypes() {
        initGeneralPrintType();
	}

	@Override
	public boolean equals(Object o) {
		if(o != null && o.getClass() == InnerPrinter.class ) {
			return true;
		}

		return false;
	}

	private IWoyouService woyouService;
	private ServiceConnection connService = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			woyouService = null;
			D.out("woyouService disconnected");
			if (os != null) {
				os.closeOutputStream();
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				os = null;
			}
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			woyouService = IWoyouService.Stub.asInterface(service);
//            woyouService.setAlignment(0, callback);
			D.out("woyouService connected " + woyouService);
			os = new PosOutputStream();
			isInitedOK = os.openOutputStream();
			D.out("initPrinter woyouService = " + woyouService);

			if (isInitedOK) {
				initSupportPrintTypes();
			}
		}
	};

	private boolean callbackResult = false;
	final ICallback callback = new ICallback.Stub() {

		@Override
		public void onRunResult(boolean isSuccess, int code, String msg) throws RemoteException {
			callbackResult = isSuccess;
			if (enableCallback) {
				notifyAll();
			}
		}
	};
}
