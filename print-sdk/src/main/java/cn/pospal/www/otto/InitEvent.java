package cn.pospal.www.otto;

/**
 * 初始化事件
 * @author Near Chan
 *
 */
public class InitEvent {
	public static final int TYPE_SYSTEM_SERVICE = 0;	// 服务初始化
    public static final int TYPE_HARDWARE_PRINTER = 1;
    public static final int TYPE_HARDWARE_SCANNER = 2;
    public static final int TYPE_HARDWARE_OEM = 3;      // 商家硬件
    public static final int TYPE_CLOSE_PRINTER = 4;      // 商家硬件

	private int type = TYPE_SYSTEM_SERVICE;		// 初始化类型

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

    public static final int STATUS_SUCCESS = 0;
    public static final int STATUS_FAIL = 1;
    private int status = STATUS_SUCCESS;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
