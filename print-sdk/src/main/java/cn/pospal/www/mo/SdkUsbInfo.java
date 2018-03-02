package cn.pospal.www.mo;

import android.hardware.usb.UsbDevice;

/**
 * USB信息
 * @author Near Chan
 *
 */
public class SdkUsbInfo {
	public static final transient int PROTOCOL_ESC = 0;		// POS协议
	public static final transient int PROTOCOL_TSC = 1;		// 标签协议
	public static final transient int PROTOCOL_ETSC = 2;	// POS+标签协议（可以切换）

    public static final int TYPE_INNER_SERIAL = 0;      // 内置串口
    public static final int TYPE_INNER_USB = 1;         // 内置USB
    public static final int TYPE_OUT_USB = 2;           // 外接USB

	public static final int EXTRA_TYPE_NONE = 0;
	public static final int EXTRA_TYPE_SERIAL = 1;

	private int vendorId;
	private int productId;
	private String deviceName;
	private int protocolType = PROTOCOL_ESC;		// 协议类型
    private int type = TYPE_INNER_SERIAL;
	private int extraType = EXTRA_TYPE_NONE;		// 额外的类型，一些打印机需要先传送一部分打印控制编码
	
	public int getVendorId() {
		return vendorId;
	}
	public void setVendorId(int vendorId) {
		this.vendorId = vendorId;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o != null && o.getClass() == SdkUsbInfo.class) {
			SdkUsbInfo other = (SdkUsbInfo) o;
			return other.vendorId == vendorId 
					&& other.productId == productId 
					&& other.deviceName.equals(deviceName);
		}
		return false;
	}
	
	/**
	 * 判断是否同类型产品
	 * @param usbDevice
	 * @return
	 */
	public boolean isSameType(UsbDevice usbDevice) {
		if(usbDevice != null) {
			return vendorId == usbDevice.getVendorId() && productId == usbDevice.getProductId();
		}
		
		return false;
	}
	
	/**
	 * 判断是否同一个产品
	 * @param usbDevice
	 * @return
	 */
	public boolean isSameDevice(UsbDevice usbDevice) {
		if(usbDevice != null) {
			return vendorId == usbDevice.getVendorId()
					&& productId == usbDevice.getProductId()
					&& deviceName.equals(usbDevice.getDeviceName());
		}
		
		return false;
	}
	public int getProtocolType() {
		return protocolType;
	}
	public void setProtocolType(int protocolType) {
		this.protocolType = protocolType;
	}

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

	public int getExtraType() {
		return extraType;
	}

	public void setExtraType(int extraType) {
		this.extraType = extraType;
	}
}
