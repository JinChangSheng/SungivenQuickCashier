package cn.pospal.www.hardware.printer;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.ParcelUuid;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import cn.pospal.www.app.*;
import cn.pospal.www.debug.D;
import cn.pospal.www.hardware.printer.oject.PrintH5Job;
import cn.pospal.www.hardware.printer.oject.ReceiptJob;
import cn.pospal.www.manager.ManagerData;
import cn.pospal.www.posbase.R;
import cn.pospal.www.service.fun.PrinterFun;

/**
 * 蓝牙打印机
 * @author lin
 *
 */
public class PrintByBluetooth extends AbstractEscPrinter {
	private static final int[] DEVICECLASS_PRINTERS = new int[]{1664, 7936};
	private static final String NAME = ManagerApp.getInstance().getString(R.string.printer_name_bluetooth);
	private BluetoothDevice bondDevice;
	private OutputStream os;
	private int bondDeviceClass = 0;
	
	public PrintByBluetooth() {
		connectType = CONNECT_TYPE_BT;
		lineWidth = AppConfig.isW58 ? 32 : 42;
	}
	
	private boolean isPrinterConnected() {
		
		if(RamStatic.bluetoothSocket != null) {
			os = getPrinterOutputStream();
			if(os != null) {
				return true;
			}
		}
		
		return false;
	}

	@SuppressLint("NewApi")
	@Override
	public boolean initPrinter() {
		super.initPrinter();
		try {
			D.out("KKKKKKKK RamStatic.bluetoothSocket  = " + RamStatic.bluetoothSocket );
			if(ManagerData.getBtEnable()) {
				if(RamStatic.bluetoothSocket == null) {
					String btAddr = ManagerData.getBtAddr();
					D.e("chl", ">>>>>>>>>>>> btAddr  = " + btAddr);
					if(!btAddr.equals("")) {
						bondDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(btAddr);
						if(bondDevice == null) {
							D.out("KKKKKKKK bondDevice  = null");
							return false;
						} else {
							if(bondDevice.getBluetoothClass()!=null){
								bondDeviceClass = bondDevice.getBluetoothClass().getDeviceClass();
							}
							UUID uuid = PrinterFun.KNOWN_PRINTER_UUID;
							
							// 如果是高版本我们可以获取uuid
							if(android.os.Build.VERSION.SDK_INT >= 15
                                    && !cn.pospal.www.app.Constance.HPRT_BLUETOOTH_PRINTER.equals(bondDevice.getName())) {
								ParcelUuid[] uuids = bondDevice.getUuids();
								D.out("KKKKKKKK uuids = " + uuids);
								if(uuids != null && uuids.length > 0) {
									for (ParcelUuid parcelUuid : uuids) {
										D.out("KKKKKKKK parcelUuid = " + parcelUuid);
									}
									
									uuid = uuids[0].getUuid();
								}
							}
							
							D.out("KKKKKKKK bondDevice  = " + bondDevice);
							RamStatic.bluetoothSocket = bondDevice.createRfcommSocketToServiceRecord(uuid);
							if(bondDevice.getBondState() != BluetoothDevice.BOND_BONDED) {
								D.out("KKKKKKKKK bondDevice.getBondState"+bondDevice.getBondState());
								RamStatic.bluetoothSocket = null;
								return false;
							}
							
						}
					} else {
						return false;
					}
				}
				RamStatic.bluetoothSocket.connect();

				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
			try {
				RamStatic.bluetoothSocket =(BluetoothSocket) bondDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(bondDevice,1);
				RamStatic.bluetoothSocket.connect();
				D.e("chl", ">>>>>>>>>>>>>>> createRfcommSocket");
			} catch (Exception e1) {
				RamStatic.bluetoothSocket = null;
				e1.printStackTrace();
			}
		}
		
		return false;
	}

	@Override
	public boolean isInitedOK() {
		return isPrinterConnected();
	}

	@Override
	public boolean hasPrinter() {
		return isPrinterConnected();
	}

	@Override
	public boolean isConnected() {
		return isPrinterConnected();
	}

	@Override
	public String getName() {
		return NAME;
	}

	public void close() {
		D.out("KKKKKKKKKK PrintByBluetooth close");
		try {
			if(os != null) {
				os.close();
			}
		} catch (Exception e) {
			D.out(e);
		}
		try {
			if(RamStatic.bluetoothSocket != null) {
				RamStatic.bluetoothSocket.close();
			}
		} catch (Exception e) {
			D.out(e);
		}
		os = null;
		RamStatic.bluetoothSocket = null;
		D.out("KKKKKKKKKK PrintByBluetooth close end");
	}

	@Override
	protected InputStream getPrinterInputStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected OutputStream getPrinterOutputStream() {
		
		if(RamStatic.bluetoothSocket != null) {
			if(os != null) {
				return os;
			}
			
			try {
				if(RamStatic.bluetoothSocket != null) {
					os = RamStatic.bluetoothSocket.getOutputStream();
					return os;
				}
			} catch (IOException e) {
				e.printStackTrace();
				os = null;
			}

		}
		
		return null;
	}

	@Override
	public void closePrinter() {
		close();
	}

	
	
	@Override
	public void initSupportPrintTypes() {
		supportPrintTypes.add(new SupportPrintType(ReceiptJob.class, 0));
		supportPrintTypes.add(new SupportPrintType(PrintH5Job.class, 0));

	}

    @Override
    protected void openDrawer() {
//        if(bondDeviceClass != 0 && bondDeviceClass!=DEVICECLASS_PRINTERS[0]){
            super.openDrawer();
//        }
    }

    @Override
    protected void cutReceipt() {
//        if(bondDeviceClass != 0 && bondDeviceClass!=DEVICECLASS_PRINTERS[0]){
            super.cutReceipt();
//        }
    }

    @Override
    protected void printBarcode(String barcode, int type) {
    	// TODO Auto-generated method stub
    	super.printBarcode(barcode, type);
    }


}
