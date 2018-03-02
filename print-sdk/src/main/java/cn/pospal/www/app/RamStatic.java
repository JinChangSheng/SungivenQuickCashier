package cn.pospal.www.app;

import android.bluetooth.BluetoothSocket;
import android.content.Context;

import java.util.List;

import cn.pospal.www.debug.D;
import cn.pospal.www.mo.SdkUsbInfo;

public class RamStatic {
	public static BluetoothSocket bluetoothSocket;
	public static List<SdkUsbInfo> printers;                    // USB打印机信息


	/**
	 * 数据初始化
	 * @param reInit 是否重新初始化
	 */
	public static void intRamData(boolean reInit) {
        D.out("RamStatic intRamData reInit = " + reInit);
		Context context = ManagerApp.getInstance();

	}

	/**
	 * 初始化所有打印机信息
	 */
	public static final void initAllPrinter() {

	}

}
