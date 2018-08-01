package cn.pospal.www.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.pospal.www.app.Constance;
import cn.pospal.www.app.ManagerApp;
import cn.pospal.www.app.RamStatic;
import cn.pospal.www.debug.D;
import cn.pospal.www.hardware.printer.AbstractPrinter;
import cn.pospal.www.hardware.printer.PrintEscByUSB;
import cn.pospal.www.hardware.printer.PrintEscByUsb2Serial;
import cn.pospal.www.hardware.printer.PrintTscByUSB;
import cn.pospal.www.hardware.printer.PrintTscByUsb2Serial;
import cn.pospal.www.mo.SdkUsbInfo;
import cn.pospal.www.otto.BusProvider;
import cn.pospal.www.otto.DeviceEvent;
import cn.pospal.www.posbase.R;
import cn.pospal.www.service.fun.DataUploaderFun;
import cn.pospal.www.service.fun.IServiceFun;
import cn.pospal.www.service.fun.NetStatusFun;
import cn.pospal.www.service.fun.PrinterFun;

public class SystemService extends Service {
	private List<IServiceFun> serviceFuns = new ArrayList<IServiceFun>();
	private boolean isRunning = true;
	private UsbManager usbManager;
	private UsbReceiver usbReceiver;
	// 最后保存的usb设备
	private Map<String, UsbDevice> lastDeviceMap;
	private static SystemService this_;

	public static final SystemService getInstance() {
		return this_;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_NOT_STICKY;
	}
	
	@Override
	public void onCreate() {
		D.out("QQQQQQQQQ SystemService onCreate");
		NetStatusFun netStatusFun = new NetStatusFun(this);
		serviceFuns.add(netStatusFun);

        // 添加服务功能
        // 必须在最后添加，因为会发送初始化事件
        PrinterFun printerFun = PrinterFun.getInstance();
        serviceFuns.add(printerFun);
		DataUploaderFun dataUploaderFun = new DataUploaderFun();
		serviceFuns.add(dataUploaderFun);
		
		// 开始服务功能
		for (IServiceFun iServiceFun : serviceFuns) {
			iServiceFun.start();
		}

		// 保存最后usb信息
		usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
		lastDeviceMap = usbManager.getDeviceList();

		//注册Usb状态监听器
		usbReceiver = new UsbReceiver();
		IntentFilter filter = new IntentFilter();
//		filter.addAction(Constance.ACTION_USB_STATE);
		filter.addAction(Constance.ACTION_USB_PERMISSION);
		registerReceiver(usbReceiver, filter);
		D.out("SystemService registerReceiver");

		isRunning = true;
		Thread usbScannerThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (isRunning) {
					// 3s检测一次usb状态
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (!isRunning) {
						break;
					}
					usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
					Map<String, UsbDevice> usbDeviceMap = usbManager.getDeviceList();
                    if (usbDeviceMap != null) {
                        if (usbDeviceMap.keySet().size() > lastDeviceMap.keySet().size()) {     // 插入usb
                            checkInsertUsb();
                        } else if (usbDeviceMap.keySet().size() < lastDeviceMap.keySet().size()) {      // 拔出usb
                            checkPullUsb();
                        }
                    }
				}
			}
		});
		usbScannerThread.start();
		this_ = this;
        D.out("QQQQQQQQQ SystemService onCreate end");

		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		D.out("QQQQQQQQQ SystemService onDestroy");
        if (usbReceiver != null) {
            unregisterReceiver(usbReceiver);
            usbReceiver = null;
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (IServiceFun iServiceFun : serviceFuns) {
                    iServiceFun.stop();
                    iServiceFun = null;
                }
                serviceFuns.clear();
                D.out("QQQQQQQQQ SystemService onDestroy 222");
                isRunning = false;
                this_ = null;
                D.out("QQQQQQQQQ SystemService onDestroy end");
            }
        });
        thread.start();

		super.onDestroy();
	}

	private SdkUsbInfo initPrinter;
	/**
	 * 检测添加的usb设备
	 * @return
	 */
	private boolean checkInsertUsb() {
		D.out("checkInsertUsb");
		// 添加的usb打印机
		HashMap<String, UsbDevice> usbDeviceMap = usbManager.getDeviceList();

		UsbDevice insertDevice = null;
		Set<String> keys = usbDeviceMap.keySet();
		for (String string : keys) {
			UsbDevice usbDevice = usbDeviceMap.get(string);
			// 目前暂时不支持两台一模一样的usb设备插入
			if (insertDevice != null
					&& insertDevice.getProductId() == usbDevice.getProductId()
					&& insertDevice.getVendorId() == usbDevice.getVendorId()
					&& insertDevice.getDeviceClass() == usbDevice.getDeviceClass()
					&& insertDevice.getDeviceName().equals(usbDevice.getDeviceName())) {
				insertDevice = null;
				break;
			}
			// 最后插入的usbDeviceId最大，找出最后插入的usb设备
			if (insertDevice == null || insertDevice.getDeviceId() < usbDevice.getDeviceId()) {
				insertDevice = usbDevice;
			}
		}

		D.out("insertDevice = " + insertDevice);
		if (insertDevice != null) {
			// 如果是系统已经添加的打印机则判断授权，如果没有授权弹出授权窗口
			for (SdkUsbInfo printer : RamStatic.printers) {
				if (printer.getVendorId() == insertDevice.getVendorId()
						&& printer.getProductId() == insertDevice.getProductId()) {
					if (!usbManager.hasPermission(insertDevice)) {
						this.initPrinter = printer;
						//没有权限询问用户是否授予权限
						//该代码执行后，系统弹出一个对话框
						//询问用户是否授予程序操作USB设备的权限
						PendingIntent pi = PendingIntent.getBroadcast(this, 0, new Intent(Constance.ACTION_USB_PERMISSION), 0);
						usbManager.requestPermission(insertDevice, pi);

                        String printerName = null;
                        if (printer.getExtraType() == SdkUsbInfo.EXTRA_TYPE_NONE) {
                            if(printer.getProtocolType() == SdkUsbInfo.PROTOCOL_ESC) {
                                printerName = PrintEscByUSB.NAME;
                            } else {
                                printerName = PrintTscByUSB.NAME;
                            }
                        } else {
                            if (printer.getProtocolType() == SdkUsbInfo.PROTOCOL_ESC) {
                                printerName = PrintEscByUsb2Serial.NAME;
                            } else {
                                printerName = PrintTscByUsb2Serial.NAME;
                            }
                        }
						DeviceEvent event = new DeviceEvent();
						event.setDevice(DeviceEvent.DEVICE_PRINTER);
						event.setType(DeviceEvent.TYPE_DISABLE);
                        event.setDeviceName(printerName);
						BusProvider.getInstance().post(event);

						break;
					}
				}
			}
		}

		lastDeviceMap = usbDeviceMap;

		return true;
	}

	private final int MSG_USB_PUSH = 1001;
	private final int MSG_USB_CONNECT = 1002;
	private final int MSG_USB_PMS_SUCCESS = 1003;
	private final int MSG_USB_PMS_FAIL = 1004;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MSG_USB_PUSH:
					ManagerApp.getInstance().showToast(R.string.usb_printer_disconnect);
					break;
				case MSG_USB_CONNECT:
					break;
				case MSG_USB_PMS_SUCCESS:
					break;
				case MSG_USB_PMS_FAIL:
					break;
			}
			super.handleMessage(msg);
		}
	};

	/**
	 * 检测弹出的usb设备
	 * @return
	 */
	private boolean checkPullUsb() {
		D.out("checkPullUsb");
		UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
		HashMap<String, UsbDevice> usbDeviceMap = usbManager.getDeviceList();
		Set<String> lastKeys = lastDeviceMap.keySet();
		Set<String> keys = usbDeviceMap.keySet();
		lastKeys.removeAll(keys);
		boolean isPrinterDisconnect = false;
		for (String key : lastKeys) {
			if (key != null) {
				UsbDevice usbDevice = lastDeviceMap.get(key);
				for (SdkUsbInfo printer : RamStatic.printers) {
					if (printer.getVendorId() == usbDevice.getVendorId()
							&& printer.getProductId() == usbDevice.getProductId()) {
						handler.sendEmptyMessage(MSG_USB_PUSH);
						D.out("pullDevice = " + printer);
						PrinterFun.getInstance().unregistUsbPrinter(printer);
						isPrinterDisconnect = true;

                        String printerName = null;
                        if (printer.getExtraType() == SdkUsbInfo.EXTRA_TYPE_NONE) {
                            if(printer.getProtocolType() == SdkUsbInfo.PROTOCOL_ESC) {
                                printerName = PrintEscByUSB.NAME;
                            } else {
                                printerName = PrintTscByUSB.NAME;
                            }
                        } else {
                            if (printer.getProtocolType() == SdkUsbInfo.PROTOCOL_ESC) {
                                printerName = PrintEscByUsb2Serial.NAME;
                            } else {
                                printerName = PrintTscByUsb2Serial.NAME;
                            }
                        }
						DeviceEvent event = new DeviceEvent();
						event.setDevice(DeviceEvent.DEVICE_PRINTER);
						event.setType(DeviceEvent.TYPE_DISABLE);
                        event.setDeviceName(printerName);
						BusProvider.getInstance().post(event);
						break;
					}
				}

				if (isPrinterDisconnect) {
					break;
				}
			}
		}

		lastDeviceMap = usbDeviceMap;
		return true;
	}

	/**
	 * 监听Usb授权,授权之后重新添加
	 * @author Near Chan
	 *
	 */
	class UsbReceiver extends BroadcastReceiver {

		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			D.out("UsbReceiver action = " + action);

			if (Constance.ACTION_USB_PERMISSION.equals(action)) {
				synchronized (this) {
					if(initPrinter != null) {
						UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
						// 允许权限申请
						if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
							if (usbDevice != null) {
								if (usbDevice.getVendorId() == initPrinter.getVendorId()
										&& usbDevice.getProductId() == initPrinter.getProductId()) {
									ManagerApp.getInstance().showToast(R.string.printer_permise_success);
									// 属于直接打印或者无需控制字符
                                    AbstractPrinter printer = null;
									if (initPrinter.getExtraType() == SdkUsbInfo.EXTRA_TYPE_NONE) {
										if(initPrinter.getProtocolType() == SdkUsbInfo.PROTOCOL_ESC) {
                                            printer = new PrintEscByUSB(context, usbDevice);
											PrinterFun.getInstance().registPrinter(printer);
										} else {
                                            printer =new PrintTscByUSB(context, usbDevice);
											PrinterFun.getInstance().registPrinter(printer);
										}
									} else {
										if (initPrinter.getProtocolType() == SdkUsbInfo.PROTOCOL_ESC) {
                                            printer = new PrintEscByUsb2Serial(context, usbDevice);
											PrinterFun.getInstance().registPrinter(printer);
										} else {
                                            printer = new PrintTscByUsb2Serial(context, usbDevice);
											PrinterFun.getInstance().registPrinter(printer);
										}
									}

									DeviceEvent event = new DeviceEvent();
									event.setDevice(DeviceEvent.DEVICE_PRINTER);
									event.setType(DeviceEvent.TYPE_CONNECT);
                                    event.setDeviceName(printer.getName());
									BusProvider.getInstance().post(event);
								} else {
									ManagerApp.getInstance().showToast(R.string.printer_permise_fail);
								}
							}
						} else {
							ManagerApp.getInstance().showToast(R.string.printer_not_found);
						}
					}
				}
			}
		}
	}

}
