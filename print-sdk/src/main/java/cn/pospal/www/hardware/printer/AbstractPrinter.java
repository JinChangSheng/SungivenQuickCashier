package cn.pospal.www.hardware.printer;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import cn.pospal.www.app.AppConfig;
import cn.pospal.www.debug.D;
import cn.pospal.www.hardware.printer.oject.PrintJob;
import cn.pospal.www.otto.BusProvider;
import cn.pospal.www.otto.DeviceEvent;
import cn.pospal.www.otto.PrintEvent;

public abstract class AbstractPrinter {
	public static final int BRAND_JIABO = 0;        // 佳博打印机打印图片指令比较不一样
	public static final int BRAND_OTHER = -1;

	protected int PRINTTYPE_RECEIPT = 1111;
	protected int PRINTTYPE_NORMAL = 2222;
	protected int INIT_DELAY_TIME = 300;	//300ms
	public static final int PRINT_SUCCESS = 0;
	public static final int PRINT_FAIL = 1;
	
	public static final int TYPE_RECEIPT = 0;
	public static final int TYPE_TABLE = 1;

	public static final int TYPE_EAN = 0;
	public static final int TYPE_QR = 1;

	// 打印机连接类型（网络、串口、USB和蓝牙）
	public static final int CONNECT_TYPE_NONE = -1;
	public static final int CONNECT_TYPE_NET = 0;
	public static final int CONNECT_TYPE_COM = 1;
	public static final int CONNECT_TYPE_USB = 2;
	public static final int CONNECT_TYPE_BT = 3;

	public static int MAX_BUFFER_SIZE = 8 * 1024;
	public static final int MAX_LINE_LENGTH = 42;
	public static final int MAX_LINE_LENGTH_W58 = 32;
	public static int MAX_LINE = AppConfig.isW58 ?
			MAX_BUFFER_SIZE / MAX_LINE_LENGTH_W58 : MAX_BUFFER_SIZE / MAX_LINE_LENGTH;

	// 汉字倍高倍宽+英文倍高倍宽
	public byte[] DHDW_BYTES = new byte[]{28, 33, 12, 27, 33, 48};
	public String DHDW_STR = new String(DHDW_BYTES);
	// 汉字倍高+英文倍高
	public byte[] DH_BYTES = new byte[]{28, 33, 8, 27, 33, 16};
	public String DH_STR = new String(DH_BYTES);
	// 汉字常高+英文常高+清除格式
	public byte[] CLR_HW_BYTES = new byte[]{28, 33, 0, 27, 33, 0};
	public String CLR_HW_STR = new String(CLR_HW_BYTES);
	// 复位标记
	public byte[] CLEAR_CMD = new byte[] { 27, 64 };
	public byte[] CLEAR_LINE_SPACE = new byte[] { 27, 50 };

	public static byte[] OPEN_DRAWER_CMD = new byte[]{27, 112, 0, 127, -1};		// 0x1B, 'p', 0x00, 0x3c, -1
	public byte[] LF_CMD = new byte[]{13, 10};
	public String LF = new String(LF_CMD);
	// 切纸命令
	public byte[] CUT_RECEIPT_CMD = new byte[]{29, 86, 0};
	// 滴滴
	public byte[] BEEP_CMD = new byte[]{27, 66, 4, 1};
	public String BEEP = new String(BEEP_CMD);

	// 佳博检测指令
	public static final byte[] JIABO_STATUS_CMD = new byte[]{27, 118};
	// 通用检测指令（适合网口和串口）
	public static final byte[] SERIAL_STATUS_CMD = new byte[]{16, 4, 1};        // 状态
	public static final byte[] SERIAL_OFFLINE_CMD = new byte[]{16, 4, 2};       // 脱机
	public static final byte[] SERIAL_ERROR_CMD = new byte[]{16, 4, 3};         // 错误
	public static final byte[] SERIAL_SENSOR_CMD = new byte[]{16, 4, 4};        // 传感器

    protected int lineWidth = 42;       // 打印最大英文字符数
    protected long index;
	protected int connectType = CONNECT_TYPE_NONE;
	protected long lastCheckTime = 0;       //最后检测时间

	public long getLastCheckTime() {
		return lastCheckTime;
	}

	public void setLastCheckTime(long lastCheckTime) {
		this.lastCheckTime = lastCheckTime;
	}

//	protected Queue<PrintJob> printJobs = new LinkedBlockingDeque<>(8);
	protected Queue<PrintJob> printJobs = new ArrayDeque<>(8);
	protected PrintJob currentPrintJob;

	public Queue<PrintJob> getPrintJobQueue() {
		return printJobs;
	}

	public PrintJob getCurrentPrintJob() {
		return currentPrintJob;
	}

	public void setCurrentPrintJob(PrintJob currentPrintJob) {
		this.currentPrintJob = currentPrintJob;
	}
	
	// 初始化打印机
	public boolean initPrinter() {
		return false;
	}
	protected abstract boolean isInitedOK();				// 初始化是否完成
	protected abstract boolean hasPrinter();				// 是否存在打印机
	protected abstract boolean isConnected();				// 是否连接到打印机
	public abstract String getName();					// 获取打印机名称
	/**
	 * 打印任务送达
	 * 打印机编号可以让我们支持多台同类型打印机
	 * @param printJob		打印任务，需要clone（可能多台打印机支持同一个任务）
	 * @param uid			指定打印机编号或者uid
	 */
	public void jobComing(PrintJob printJob, long uid) {
		SupportPrintType type = new SupportPrintType(printJob.getClass(), uid);
		if(supportPrintTypes.contains(type)) {
            D.out("jobComing add: printer = " + getClass() + ", job = " + printJob);
            PrintJob clonePrintJob = (PrintJob) printJob.clone();
			if(clonePrintJob != null) {
                clonePrintJob.setMaxLineLen(lineWidth);
				printJobs.offer(clonePrintJob);
			}
		}
	}

    /**
     * 恢复打印现场
     * @param printJob
     */
    public void restoreJob(PrintJob printJob) {
        printJobs.offer(printJob);
    }

	public abstract void closePrinter();					// 关闭打印机
	// 打印机关机
	public void shutdown() {
		
	}
	
	protected abstract InputStream getPrinterInputStream();
	protected abstract OutputStream getPrinterOutputStream();

	public abstract boolean print(PrintJob printJob) throws Exception;

	protected List<SupportPrintType> supportPrintTypes = new ArrayList<SupportPrintType>();
	
	/**
	 * 初始化支持打印的类型
	 */
	public abstract void initSupportPrintTypes();

    public List<SupportPrintType> getSupportPrintTypes() {
        return supportPrintTypes;
    }

    /**
	 * 添加支持打印的类型
	 * @param supportPrintType 支持的类型
	 */
	public void addSupportPrintType(SupportPrintType supportPrintType) {
		if(!supportPrintTypes.contains(supportPrintType)) {
			supportPrintTypes.add(supportPrintType);
		}
	}

	/**
	 * 删除支持打印的类型
	 * @param supportPrintType 支持的类型
	 */
	public void removeSupportPrintType(SupportPrintType supportPrintType) {
		if(supportPrintTypes.contains(supportPrintType)) {
			supportPrintTypes.remove(supportPrintType);
		}
	}
	
	/**
	 * 清除支持打印的类型
	 */
	public void clearSupportPrintTypes() {
		supportPrintTypes.clear();
	}

	/**
	 * 测试打印机
	 */
	public void test(){}

    protected int lastStatus = -1;
    /**
     * 检测打印机状态
     * @return
     */
    public boolean checkPrinter(){return true;}

    /**
     * 发送打印机状态
     * @param status
     */
    public void sendPrinterStatus(int status) {
        if (lastStatus == status) {
            return;
        }

        lastStatus = status;
        DeviceEvent event = new DeviceEvent();
        event.setDevice(DeviceEvent.DEVICE_PRINTER);
        event.setDeviceName(getName());
        event.setType(status);
        event.setIndex(index);
        BusProvider.getInstance().post(event);
    }
	
	@Override
	public boolean equals(Object o) {
		if(o != null && o instanceof AbstractPrinter) {
			AbstractPrinter other = (AbstractPrinter) o;
			
			return getName().equals(other.getName());
		}
		
		return false;
	}

	public int getConnectType() {
		return connectType;
	}

    public long getIndex() {
        return index;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    protected String charset = "GBK";

    public static final int STATUS_CONNECTED = 0;
    public static final int STATUS_NO_PAPER = 1;
    public static final int STATUS_DISCONNECTED = 2;
    public static final int STATUS_ERROR = 3;

    /**
     * 打印机可以通过检测写一个字节或者4K的0来检测打印机是否正常连接
     * 这个函数可以在打印前打印后测试（打印后检测可以判断这笔单据是否打印完成）
     * 该函数可以被复写，默认返回连接成功
     * @return
     */
    protected int getStatus() {
        return  STATUS_CONNECTED;
    }

    public static class PrinterStatusException extends Exception {
        private static final long serialVersionUID = 702519727800277016L;

        public PrinterStatusException(String msg) {
            super(msg);
        }

        @Override
        public boolean equals(Object o) {
            if(o != null && o.getClass() == PrinterStatusException.class) {
                return getMessage().equals(((PrinterStatusException)o).getMessage());
            }

            return super.equals(o);
        }
    }

	/**
	 * 新增完成打印
	 */
	protected boolean completePrint() {
        sendJobStatus(currentPrintJob, PrintEvent.STATUS_END);
		return true;
	}

    /**
     * 新增备份联准备阶段
     */
    protected boolean beforeBackupPrint() {
        return true;
    }

    private PrintEvent lastPrintEvent;
    /**
     * 发送任务状态
     * @param status
     */
    public void sendJobStatus(PrintJob job, int status) {
        if (job.isHaveToTrace()) {
            if (lastPrintEvent != null
                    && lastPrintEvent.getUid() == job.getUid()
                    && lastPrintEvent.getStatus() == status) {
                return;
            }

            PrintEvent event = new PrintEvent();
            event.setUid(job.getUid());
            event.setClazz(job.getClass());
            event.setQty(job.getCnt());
            event.setStatus(status);
            lastPrintEvent = event;
            BusProvider.getInstance().post(event);
        }
    }

	/**
	 *  For Landi
	 */
	protected void startPrint() {

	}
}
