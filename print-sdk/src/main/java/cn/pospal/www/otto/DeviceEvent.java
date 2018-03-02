package cn.pospal.www.otto;

import android.content.Intent;

/**
 * 外部设备事件
 * 连接/初始化/断开/无法使用等等
 * Created by M4600 on 2015/6/26.
 */
public class DeviceEvent {
    public static final int DEVICE_PRINTER = 0;
    public static final int DEVICE_LED_DISPLAY = 1;
    public static final int DEVICE_PRESENTATION_DISPLAY = 2;
    public static final int DEVICE_SCALE = 3;
    public static final int DEVICE_CARD_READER = 4;
    public static final int DEVICE_NET = 5;
    public static final int DEVICE_NFC = 6;

    public static final int TYPE_CONNECT = 1;
    public static final int TYPE_INITED = 2;
    public static final int TYPE_DISCONNECT = 3;
    public static final int TYPE_DISABLE = 4;
    public static final int TYPE_CONNECT_INNER = 5;     // 只连接内网

    private int type = TYPE_CONNECT;
    private int device = DEVICE_PRINTER;
    private long index;
    private String deviceName;
	private Intent data;
    private String msg;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDevice() {
        return device;
    }

    public void setDevice(int device) {
        this.device = device;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

	public Intent getData() {
		return data;
	}

	public void setData(Intent data) {
		this.data = data;
	}

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o.getClass().equals(DeviceEvent.class)) {
            DeviceEvent other = (DeviceEvent) o;
            if (deviceName == null) {
                deviceName = "";
            }
            if (other.deviceName == null) {
                other.deviceName = "";
            }
            return device == other.device
                    && deviceName.equals(other.deviceName)
                    && index == other.index;
        }

        return false;
    }

    @Override
    public String toString() {
        return "DeviceEvent{" +
                "type=" + type +
                ", device=" + device +
                ", index=" + index +
                ", deviceName='" + deviceName + '\'' +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }
}
