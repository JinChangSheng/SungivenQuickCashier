package cn.pospal.www.hardware.printer;

public class Constance {
	// usb转串口芯片pid
	private static final int[] USB_DIRECT_PIDS = new int[]{8963, 9553, 8965, 33054, 30084, 85, 1280};
	// 打印机端口
	public static final int NET_PRINTER_PORT_JIABO = 9100;
	public static final int NET_PRINTER_PORT_STATUS = 4000;

	public static final String POSPAL_PRINTER = "/dev/ttySerialPrinter";
	public static final String POSPAL_LED = "/dev/ttySerialLedDisplay";
	public static final String POSPAL_SCALE = "/dev/ttySerialScale";

	public enum PrinterType {
		PRINTERTYPE_SERIAL("serial"), 
		PRINTERTYPE_NET("net"), 
		PRINTERTYPE_BLUETOOTH("buletooth"), 
		PRINTERTYPE_PARPORT("parport");
		
		private String name;
		private PrinterType(String name) {
			this.name = name;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}

	/**
	 * 是否直接打印内容（无需控制字符）
	 * @param pid
	 * @return
	 */
	public static boolean isUsbDirectPrinter(int pid) {
		for (int comparePid : USB_DIRECT_PIDS) {
			if(pid == comparePid) {
				return true;
			}
		}
		
		return false;
	}
}
