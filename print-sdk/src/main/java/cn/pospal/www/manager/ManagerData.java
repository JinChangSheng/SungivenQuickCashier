package cn.pospal.www.manager;

import android.hardware.Camera;

import com.google.gson.Gson;

import java.io.IOException;
import java.math.BigDecimal;

import cn.pospal.www.app.AppConfig;
import cn.pospal.www.app.Constance;
import cn.pospal.www.debug.D;
import cn.pospal.www.fun.crypto.PasswordUtil;
import cn.pospal.www.mo.SdkUsbInfo;
import cn.pospal.www.util.DatetimeUtil;
import cn.pospal.www.util.GsonUtil;

/**
 * 数据管理器 管理数据存储和读取
 * 
 * @author Near 2012.12.14
 */
public class ManagerData {
	private static final Gson GSON = GsonUtil.getInstance();
	public static final String ENABLE = "1";
	public static final String DISABLE = "0";
	
	/**
	 * 获取上一次同步时间
	 * 
	 * @return
	 */
	public static String getSyncDateTime() {
		return ManagerConf.readFromLocal("sync_datetime", DatetimeUtil.DEFAULT_DATETIME);
	}

	/**
	 * 保存上一次同步时间
	 * 
	 * @param saveDateTime
	 */
	public static void saveSyncDateTime(String saveDateTime) {
		ManagerConf.saveToLocal("sync_datetime", saveDateTime);
	}

	/**
	 * 获取商店名称
	 * 
	 * @return
	 */
	public static String getStoreName() {
		return ManagerConf.readFromLocal("store_name", "");
	}

	/**
	 * 保存商店名称
	 * 
	 * @param storeName
	 */
	public static void saveStoreName(String storeName) {
		ManagerConf.saveToLocal("store_name", storeName);
	}

	/**
	 * 获取商店地址
	 * 
	 * @return
	 */
	public static String getStoreAddr() {
		return ManagerConf.readFromLocal("store_addr", "");
	}

	/**
	 * 保存商店地址
	 * 
	 * @param storeAddr
	 */
	public static void saveStoreAddr(String storeAddr) {
		ManagerConf.saveToLocal("store_addr", storeAddr);
	}

	/**
	 * 获取商店地址
	 * 
	 * @return
	 */
	public static String getStorePhone() {
		return ManagerConf.readFromLocal("store_phone", "");
	}

	/**
	 * 保存商店地址
	 * 
	 * @param storePhone
	 */
	public static void saveStorePhone(String storePhone) {
		ManagerConf.saveToLocal("store_phone", storePhone);
	}

	/**
	 * 获取商店信息
	 * 
	 * @return
	 */
	public static String getStoreInfo() {
		return ManagerConf.readFromLocal("store_info", "");
	}

	/**
	 * 保存商店信息
	 * 
	 * @param storeInfo
	 */
	public static void saveStoreInfo(String storeInfo) {
		ManagerConf.saveToLocal("store_info", storeInfo);
	}

	public static boolean getSaleListCombine() {
		return ManagerConf.readFromLocal("sale_list_combine", "1").equals(ENABLE);
	}

	public static void saveSaleListCombine(boolean combine) {
		ManagerConf.saveToLocal("sale_list_combine", combine ? ENABLE : DISABLE);
	}

	public static boolean getIsNeedPrintBarcode() {
		return ManagerConf.readFromLocal("is_need_print_barcode", "1").equals(ENABLE);
	}

	public static void saveIsNeedPrintBarcode(boolean isNeed) {
		ManagerConf.saveToLocal("is_need_print_barcode", isNeed ? ENABLE : DISABLE);
	}

	/**
	 * 获取打印宽度是否58
	 * @return
	 */
	public static boolean getW58() {
		String defaultW = "0";
        // OEM的手持设备打印机一般是58的
        if (AppConfig.isPhoneVersion && !AppConfig.company.equals(Constance.COMPANY_POSPAL)) {
            defaultW = "1";
        }
		if (AppConfig.company.equals(Constance.COMPANY_ELC)){//易乐康 默认为80
			defaultW = "0";
		}
		return ManagerConf.readFromLocal("w58", defaultW).equals(ENABLE);
	}

	public static void saveW58(boolean isW58) {
		ManagerConf.saveToLocal("w58", isW58 ? ENABLE : DISABLE);
	}

	public static void resetW58() {
        boolean isW58 = false;
        // OEM的手持设备打印机一般是58的
        if (AppConfig.isPhoneVersion && !AppConfig.company.equals(Constance.COMPANY_POSPAL)) {
            isW58 = true;
        }
		ManagerConf.saveToLocal("w58", isW58 ? ENABLE : DISABLE);
	}

	/**
	 * 获取打印Logo
	 * @return
	 */
	public static boolean getPrintLogo() {
		return ManagerConf.readFromLocal("printLogo", "0").equals(ENABLE);
	}

	public static void savePrintLogo(boolean printLogo) {
		ManagerConf.saveToLocal("printLogo", printLogo ? ENABLE : DISABLE);
	}

	/**
	 * 获取厨房打印机打印宽度是否58
	 * @return
	 */
	public static boolean getW58Kitchen() {
		return ManagerConf.readFromLocal("w58_kitchen", "0").equals(ENABLE);
	}

	public static void saveW58Kitchen(boolean isW58) {
		ManagerConf.saveToLocal("w58_kitchen", isW58 ? ENABLE : DISABLE);
	}

	/**
	 * 获取压桌单打印机打印宽度是否58
	 * @return
	 */
	public static boolean getW58Table() {
		return ManagerConf.readFromLocal("w58_table", "0").equals(ENABLE);
	}

	public static void saveW58Table(boolean isW58) {
		ManagerConf.saveToLocal("w58_table", isW58 ? ENABLE : DISABLE);
	}

	/**
	 * 导购员选项
	 * @return
	 */
	public static boolean getUseGuider() {
		return ManagerConf.readFromLocal("use_guider", "0").equals(ENABLE);
	}

	public static void saveUseGuider(boolean useGuider) {
		ManagerConf.saveToLocal("use_guider", useGuider ? ENABLE : DISABLE);
	}

	
	/**
	 * 是否启用导购员
	 * @return
	 */
	public static boolean isUseGuide(){
		return ManagerConf.readFromLocal("is_use_guide", "0").equals(ENABLE);
	}
	
	/**
	 * 是否使用整单导购不是的话单品导购
	 * @return
	 */
	public static boolean isUseTicketGuide(){
		return ManagerConf.readFromLocal("is_use_ticket_guide", "0").equals(ENABLE);
	}
	
	/**
	 * 设置是否启用导购员
	 * @return
	 */
	public static void setUseGuide(boolean isUse){
		ManagerConf.saveToLocal("is_use_guide", isUse ? "1":"0");
	}
	
	/**
	 * 设置是否使用整单导购不是的话单品导购
	 * @return
	 */
	public static void setUseTicketGuide(boolean isUse){
		ManagerConf.saveToLocal("is_use_ticket_guide", isUse ? "1":"0");
	}

    /**
     * 反结账打印厨房小票
     * @return
     */
    public static boolean getReverseKitchenPrint() {
        return ManagerConf.readFromLocal("reverse_kitchen_print", "0").equals(ENABLE);
    }

    public static void saveReverseKitchenPrint(boolean reverseKitchenPrint) {
        ManagerConf.saveToLocal("reverse_kitchen_print", reverseKitchenPrint ? ENABLE : DISABLE);
    }

    /**
     * 打印厨房小票蜂鸣器报警
     * @return
     */
    public static boolean getKitchenBeep() {
        return ManagerConf.readFromLocal("kitchen_beep", "1").equals(ENABLE);
    }

    public static void saveKitchenBeep(boolean beep) {
        ManagerConf.saveToLocal("kitchen_beep", beep ? ENABLE : DISABLE);
    }

	/**
	 * 获取厨房总控打印机是否一品一单
	 * @return
	 */
	public static boolean getOneByOneKitchenHost() {
		return ManagerConf.readFromLocal("one_by_one_kitchen_0", "0").equals(ENABLE);
	}

	public static void saveOneByOneKitchenHost(boolean oneByone) {
		ManagerConf.saveToLocal("one_by_one_kitchen_0", oneByone ? ENABLE : DISABLE);
	}
	/**
	 * 获取厨房打印机是否一品一单
	 * @return
	 */
	public static boolean getOneByOneKitchen() {
		return ManagerConf.readFromLocal("one_by_one_kitchen", "0").equals(ENABLE);
	}

	public static void saveOneByOneKitchen(boolean oneByone) {
		ManagerConf.saveToLocal("one_by_one_kitchen", oneByone ? ENABLE : DISABLE);
	}

	/**
	 * 获取扫描枪输入模式 默认为销售
	 * 
	 * @return
	 */
	public static final int SCAN_TYPE_SALE = 0;
	public static final int SCAN_TYPE_DETAIL = 1;

	public static int getScanType() {
		return Integer.parseInt(ManagerConf.readFromLocal("scan_type", SCAN_TYPE_SALE + ""));
	}

	/**
	 * 保存扫描枪模式
	 */
	public static void saveScanType(int scanType) {
		ManagerConf.saveToLocal("scan_type", scanType + "");
	}

	/**
	 * 获取打印机IP信息
	 * 
	 * @return
	 */
	public static String getReceiptPrinterIpInfo() {
		return ManagerConf.readFromLocal("printer_ip_info", "");
	}

	/**
	 * 保存打印机IP信息
	 * 
	 * @param printerIpInfo
	 */
	public static void saveReceiptPrinterIpInfo(String printerIpInfo) {
		ManagerConf.saveToLocal("printer_ip_info", printerIpInfo);
	}

	/**
	 * 获取标签打印机IP信息
	 * 
	 * @return
	 */
	public static String getLabelPrinterIpInfo() {
		return ManagerConf.readFromLocal("label_printer_ip_info", "");
	}

	
	public static final int PRINTER_TYPE_LABLE = 0;			// 标签机
	public static final int PRINTER_TYPE_RECEIPT = 1;		// 小票机
	public static void saveInnerPrinterType(int printerType) {
		ManagerConf.saveToLocal("inner_printer_type", printerType + "");
	}
	public static int getInnerPrinterType() {
		return Integer.parseInt(ManagerConf.readFromLocal("inner_printer_type", "0"));
	}

	/**
	 * 保存标签宽度
	 * 
	 * @param width
	 */
	public static void saveLableWidth(int width) {
		ManagerConf.saveToLocal("lable_width", width + "");
	}

	/**
	 * 获取标签宽度
	 * 
	 * @return
	 */
	public static int getLableWidth() {
		return Integer.parseInt(ManagerConf.readFromLocal("lable_width", "40"));
	}

	/**
	 * 保存标签宽度
	 * 
	 * @param height
	 */
	public static void saveLableHeight(int height) {
		ManagerConf.saveToLocal("lable_height", height + "");
	}

	/**
	 * 获取标签宽度
	 * 
	 * @return
	 */
	public static int getLableHeight() {
		return Integer.parseInt(ManagerConf.readFromLocal("lable_height", "30"));
	}

    /**
     * 保存标签间隔
     *
     * @param gap
     */
    public static void saveLabelGap(int gap) {
        ManagerConf.saveToLocal("lable_gap", gap + "");
    }

    /**
     * 获取标签间隔
     *
     * @return
     */
    public static int getLabelGap() {
        return Integer.parseInt(ManagerConf.readFromLocal("lable_gap", "2"));
    }

	/**
	 * 保存标签上边距
	 *
	 * @param top
	 */
	public static void saveLableTopMargin(int top) {
		ManagerConf.saveToLocal("lable_top_margin", top + "");
	}

	/**
	 * 获取标签上边距
	 *
	 * @return
	 */
	public static int getLableTopMargin() {
		return Integer.parseInt(ManagerConf.readFromLocal("lable_top_margin", "0"));
	}

	/**
	 * 保存标签左边距
	 *
	 * @param left
	 */
	public static void saveLableLeftMargin(int left) {
		ManagerConf.saveToLocal("lable_left_margin", left + "");
	}

	/**
	 * 获取标签左边距
	 *
	 * @return
	 */
	public static int getLableLeftMargin() {
		return Integer.parseInt(ManagerConf.readFromLocal("lable_left_margin", "0"));
	}

    /**
     * 保存标签行距离
     *
     * @param space
     */
    public static void saveLabelTextSpace(int space) {
        ManagerConf.saveToLocal("lable_text_space", space + "");
    }

    /**
     * 获取标签行距离
     *
     * @return
     */
    public static int getLabelTextSpace() {
        return Integer.parseInt(ManagerConf.readFromLocal("lable_text_space", "28"));
    }

    /**
     * 保存标签打印条码
     *
     * @param printBarcode
     */
    public static void saveLabelPrintBarcode(boolean printBarcode) {
        ManagerConf.saveToLocal("lable_print_barcode", printBarcode ? ENABLE : DISABLE);
    }

    /**
     * 获取标签打印条码
     *
     * @return
     */
    public static boolean getLabelPrintBarcode() {
        return ManagerConf.readFromLocal("lable_print_barcode", "0").equals(ENABLE);
    }

    /**
     * 保存标签打印时间
     *
     * @param printDatetime
     */
    public static void saveLabelPrintDatetime(boolean printDatetime) {
        ManagerConf.saveToLocal("lable_print_datetime", printDatetime ? ENABLE : DISABLE);
    }

    /**
     * 获取标签打印时间
     *
     * @return
     */
    public static boolean getLabelPrintDatetime() {
        return ManagerConf.readFromLocal("lable_print_datetime", "0").equals(ENABLE);
    }

    /**
     * 保存标签打印时间
     *
     * @param printDatetime
     */
    public static void saveLabelShelfLife(boolean printDatetime) {
        ManagerConf.saveToLocal("lable_print_shelf_life", printDatetime ? "1" : "0");
    }

    /**
     * 获取标签打印时间
     *
     * @return
     */
    public static boolean getLabelPrintShelfLife() {
        return ManagerConf.readFromLocal("lable_print_shelf_life", "0").equals("1");
    }

    /**
     * 保存标签打印尾注
     *
     * @param printDeliveryType
     */
    public static void saveLabelPrintDeliveryType(boolean printDeliveryType) {
        ManagerConf.saveToLocal("lable_printDeliveryType", printDeliveryType ? "1" : "0");
    }

    /**
     * 获取标签打印尾注
     *
     * @return
     */
    public static boolean getLabelPrintDeliveryType() {
        return ManagerConf.readFromLocal("lable_printDeliveryType", "0").equals("1");
    }

    /**
     * 保存标签打印尾注
     *
     * @param printEndMsg
     */
    public static void saveLabelPrintEndMsg(boolean printEndMsg) {
        ManagerConf.saveToLocal("lable_print_end_msg", printEndMsg ? ENABLE : DISABLE);
    }

    /**
     * 获取标签打印尾注
     *
     * @return
     */
    public static boolean getLabelPrintEndMsg() {
        return ManagerConf.readFromLocal("lable_print_end_msg", "1").equals(ENABLE);
    }

    public static final int TYPE_TSC = 0;
    public static final int TYPE_EZPL = 1;
    /**
     * 保存标签协议类型
     *
     * @param type
     */
    public static void saveLabelPrintType(int type) {
        ManagerConf.saveToLocal("lable_print_type", type + "");
    }

    /**
     * 获取标签协议类型
     *
     * @return
     */
    public static int getLabelPrintType() {
        return Integer.parseInt(ManagerConf.readFromLocal("lable_print_type", "0"));
    }

	/**
	 * 保存厨房打印机IP信息
	 * 
	 * @param printerIpInfo
	 */
	public static void saveKitchenPrinterIpInfo(String printerIpInfo) {
		ManagerConf.saveToLocal("kitchen_printer_ip_info", printerIpInfo);
	}

	/**
	 * 获取厨房打印机IP信息
	 * 
	 * @return
	 */
	public static String getKitchenPrinterIpInfo() {
		return ManagerConf.readFromLocal("kitchen_printer_ip_info", "");
	}

	/**
	 * 保存厨房打印机1IP信息
	 * 
	 * @param printerIpInfo
	 */
	public static void saveKitchenPrinterIpInfo1(String printerIpInfo) {
		ManagerConf.saveToLocal("kitchen_printer_ip_info1", printerIpInfo);
	}

	/**
	 * 获取厨房打印机1IP信息
	 * 
	 * @return
	 */
	public static String getKitchenPrinterIpInfo1() {
		return ManagerConf.readFromLocal("kitchen_printer_ip_info1", "");
	}

	/**
	 * 获取厨房打印机2IP信息
	 * 
	 * @return
	 */
	public static String getKitchenPrinterIpInfo2() {
		return ManagerConf.readFromLocal("kitchen_printer_ip_info2", "");
	}

	/**
	 * 保存厨房打印机2IP信息
	 * 
	 * @param printerIpInfo
	 */
	public static void saveKitchenPrinterIpInfo2(String printerIpInfo) {
		ManagerConf.saveToLocal("kitchen_printer_ip_info2", printerIpInfo);
	}

	/**
	 * 获取厨房打印机3IP信息
	 * 
	 * @return
	 */
	public static String getKitchenPrinterIpInfo3() {
		return ManagerConf.readFromLocal("kitchen_printer_ip_info3", "");
	}

	/**
	 * 保存厨房打印机3IP信息
	 * 
	 * @param printerIpInfo
	 */
	public static void saveKitchenPrinterIpInfo3(String printerIpInfo) {
		ManagerConf.saveToLocal("kitchen_printer_ip_info3", printerIpInfo);
	}

	/**
	 * 获取厨打是否使用平板
	 *
     * @param index 编号，可以是uid
	 * @return
	 */
	public static int getKitchenPrinterDeviceType(long index) {
		return Integer.parseInt(ManagerConf.readFromLocal("kitchen_printer_device_type_" + index, "0"));
	}

	/**
	 * 保存厨房打印机3IP信息
	 *
	 * @param index
	 * @param type
	 */
	public static void saveKitchenPrinterDeviceType(long index, int type) {
		ManagerConf.saveToLocal("kitchen_printer_device_type_" + index, type + "");
	}

    public static final int KITCHEN_USE_RECEIPT = 0;
    public static final int KITCHEN_USE_NET = 1;
	/**
	 * 获取厨房打印机使用情况
	 * 
	 * @return		0：收银小票机		1：自己的打印机
	 */
	public static int getKitchenUseType() {
		return Integer.parseInt(ManagerConf.readFromLocal("kitchen_printer_use_type", "1"));
	}

	/**
	 * 保存厨房打印机使用情况
	 * @param type 0：收银小票机		1：自己的打印机
	 */
	public static void saveKitchenUseType(int type) {
		ManagerConf.saveToLocal("kitchen_printer_use_type", type + "");
	}

    public static final int TABLE_USE_RECEIPT = 0;
    public static final int TABLE_USE_NET = 1;
	/**
	 * 获取压桌单打印机使用情况
	 *
	 * @return		0：收银小票机		1：新的打印机
	 */
	public static int getTableUseType() {
		return Integer.parseInt(ManagerConf.readFromLocal("table_printer_use_type", "0"));
	}

	/**
	 * 保存压桌单打印机使用情况
	 * @param type 0：收银小票机		1：新的打印机
	 */
	public static void saveTableUseType(int type) {
		ManagerConf.saveToLocal("table_printer_use_type", type + "");
	}

	/**
	 * 获取压桌单打印机IP信息
	 * 
	 * @return
	 */
	public static String getTablePrinterIpInfo() {
		return ManagerConf.readFromLocal("table_printer_ip_info3", "");
	}

	/**
	 * 保存压桌单打印机IP信息
	 * 
	 * @param printerIpInfo
	 */
	public static void saveTablePrinterIpInfo3(String printerIpInfo) {
		ManagerConf.saveToLocal("table_printer_ip_info3", printerIpInfo);
	}

	/**
	 * 获取压桌单打印次数
	 * 
	 * @return
	 */
	public static int getTablePrinterNumInfo() {
		return Integer.parseInt(ManagerConf.readFromLocal("table_printer_num_info", "0"));
	}

	/**
	 * 保存压桌单打印次数
	 * 
	 * @param position
	 */
	public static void saveTablePrinterNumInfo(int position) {
		ManagerConf.saveToLocal("table_printer_num_info", position + "");
	}
	
	/**
	 * 保存标签打印机IP信息
	 * 
	 * @param printerIpInfo
	 */
	public static void saveLabelPrinterIpInfo(String printerIpInfo) {
		ManagerConf.saveToLocal("label_printer_ip_info", printerIpInfo);
	}

	/**
	 * 获取打印机打印次数
	 * 
	 * @return
	 */
	public static int getPrinterNumInfo() {
		return Integer.parseInt(ManagerConf.readFromLocal("printer_num_info", "0"));
	}

	/**
	 * 保存打印机打印次数
	 * 
	 * @param printerNumInfo
	 */
	public static void savePrinterNumInfo(int printerNumInfo) {
		ManagerConf.saveToLocal("printer_num_info", printerNumInfo + "");
	}

	/**
	 * 获取功能设置
	 * 
	 * @return
	 */
	public static int getFunInfo() {
		return Integer.parseInt(ManagerConf.readFromLocal("fun_info", "0"));
	}

	/**
	 * 保存功能设置
	 * 
	 * @param fun
	 */
	public static void saveFunInfo(int fun) {
		ManagerConf.saveToLocal("fun_info", fun + "");
	}

	/**
	 * 获取主机IP信息
	 * 
	 * @return
	 */
	public static String getServerIpInfo() {
		return ManagerConf.readFromLocal("server_ip_info", "");
	}

	/**
	 * 保存主机IP信息
	 * 
	 * @param serverIpInfo
	 */
	public static void saveServerIpInfo(String serverIpInfo) {
		ManagerConf.saveToLocal("server_ip_info", serverIpInfo);
	}

	/**
	 * 获取主机端口信息
	 * 
	 * @return
	 */
	public static String getServerPortInfo() {
		return ManagerConf.readFromLocal("server_port_info", "9315");
	}

	/**
	 * 保存主机端口信息
	 * 
	 * @param serverPortInfo
	 */
	public static void saveServerPortInfo(String serverPortInfo) {
		ManagerConf.saveToLocal("server_port_info", serverPortInfo);
	}

	/**
	 * 获取本地端口信息（作为主机）
	 * 
	 * @return
	 */
	public static String getLocalPortInfo() {
		return ManagerConf.readFromLocal("local_port_info", "9601");
	}

	/**
	 * 保存本地端口信息（作为主机）
	 * 
	 * @param localPortInfo
	 */
	public static void saveLocalPortInfo(String localPortInfo) {
		ManagerConf.saveToLocal("local_port_info", localPortInfo);
	}

	/**
	 * 获取本地端口信息（作为主机）
	 * 
	 * @return
	 */
	public static String getHostPortInfo() {
		return ManagerConf.readFromLocal("host_port_info", "9315");
	}

	/**
	 * 保存本地端口信息（作为主机）
	 * 
	 * @param localPortInfo
	 */
	public static void saveHostPortInfo(String localPortInfo) {
		ManagerConf.saveToLocal("host_port_info", localPortInfo);
	}

	/**
	 * 获取客显端口信息（作为主机）
	 * 
	 * @return
	 */
	public static String getDisplayerIpInfo() {
		return ManagerConf.readFromLocal("displayer_ip_info", "");
	}

	/**
	 * 保存客显IP信息（作为主机）
	 * 
	 * @param localIpInfo
	 */
	public static void saveDisplayerIpInfo(String localIpInfo) {
		ManagerConf.saveToLocal("displayer_ip_info", localIpInfo);
	}

	/**
	 * 获取客显端口信息（作为主机）
	 * 
	 * @return
	 */
	public static String getDisplayerPortInfo() {
		return ManagerConf.readFromLocal("displayer_port_info", "9602");
	}

	/**
	 * 保存客显端口信息（作为主机）
	 * 
	 * @param localPortInfo
	 */
	public static void saveDisplayerPortInfo(String localPortInfo) {
		ManagerConf.saveToLocal("displayer_port_info", localPortInfo);
	}

	
	public static boolean getUseNum() {
		return ManagerConf.readFromLocal("use_num", "0").equals(ENABLE);
	}

	public static void saveUseNum(boolean useNum) {
		ManagerConf.saveToLocal("use_num", useNum ? ENABLE : DISABLE);
	}

	public static boolean getUseDelivery() {
		return ManagerConf.readFromLocal("useDelivery", "0").equals(ENABLE);
	}

	public static void saveUseDelivery(boolean useDelivery) {
		ManagerConf.saveToLocal("useDelivery", useDelivery ? ENABLE : DISABLE);
	}

	public static boolean getUseCurrent() {
		return ManagerConf.readFromLocal("use_current", "0").equals(ENABLE);
	}

	public static void saveUseCurrent(boolean useCurrent) {
		ManagerConf.saveToLocal("use_current", useCurrent ? ENABLE : DISABLE);
	}

	public static boolean getUseTake() {
		return ManagerConf.readFromLocal("use_take", "0").equals(ENABLE);
	}

	public static void saveUseTake(boolean useTake) {
		ManagerConf.saveToLocal("use_take", useTake ? ENABLE : DISABLE);
	}

	public static boolean getUseSend() {
		return ManagerConf.readFromLocal("use_send", "0").equals(ENABLE);
	}

	public static void saveUseSend(boolean useSend) {
		ManagerConf.saveToLocal("use_send", useSend ? ENABLE : DISABLE);
	}

    public static void savePaymentNeedMarkNoPop(boolean need) {
        ManagerConf.saveToLocal("paymentNeedMarkNoPop", need ? "1" : "0");
    }

    public static boolean getPaymentNeedMarkNoPop() {
        return Integer.parseInt(ManagerConf.readFromLocal("paymentNeedMarkNoPop", "0")) == 1;
    }

	public static int getFrushTime() {
		return Integer.parseInt(ManagerConf.readFromLocal("frush_time", "0"));
	}

	public static void saveFrushTime(int frushTime) {
		ManagerConf.saveToLocal("frush_time", frushTime + "");
	}

	public static String getVideoPath() {
		return ManagerConf.readFromLocal("video_path", ManagerFile.POSPAL_AD_VIDEO_ROOT);
	}

	public static void saveVideoPath(String PicturePath) {
		ManagerConf.saveToLocal("video_path", PicturePath);
	}

	public static String getPicturePath() {
		return ManagerConf.readFromLocal("picture_path", ManagerFile.POSPAL_AD_PICTURE_ROOT);
	}

	public static void savePicturePath(String PicturePath) {
		ManagerConf.saveToLocal("picture_path", PicturePath);
	}

	public static String getSoundPath() {
		return ManagerConf.readFromLocal("sound_path", ManagerFile.POSPAL_AD_SOUND_ROOT);
	}

	public static void saveSoundPath(String soundPath) {
		ManagerConf.saveToLocal("sound_path", soundPath);
	}

	/**
	 * 获取使用版本
	 * @return
	 */
	public static String getUseVersion() {
		return ManagerConf.readFromLocal("use_version", "-1");
	}

	public static void saveUseVersion(String useVersion) {
		ManagerConf.saveToLocal("use_version", useVersion);
	}

	/**
	 * 保存收银员信息
	 */
    /*
	public static void saveClerkData() {
		if (RamStatic.cashierData != null && RamStatic.cashierData.getLoginCashier() != null) {
			ManagerConf.saveToLocal("clerk_id", RamStatic.cashierData.getLoginCashier().getUid() + "");
			ManagerConf.saveToLocal("clerk_amount", NumUtil.dcm2String(RamStatic.cashierData.getSaleAmount()));
			ManagerConf.saveToLocal("clerk_koolpay", NumUtil.dcm2String(RamStatic.cashierData.getThirdPartyPaymentAmount()));
			ManagerConf.saveToLocal("clerk_charge", NumUtil.dcm2String(RamStatic.cashierData.getChargeAmount()));
            ManagerConf.saveToLocal("clerk_charge_cash", NumUtil.dcm2String(RamStatic.cashierData.getChargeCashAmount()));
            ManagerConf.saveToLocal("clerk_charge_union", NumUtil.dcm2String(RamStatic.cashierData.getChargeUnionAmount()));
            ManagerConf.saveToLocal("clerk_charge_alipay", NumUtil.dcm2String(RamStatic.cashierData.getChargeAlipayAmount()));
			ManagerConf.saveToLocal("clerk_revolving", NumUtil.dcm2String(RamStatic.cashierData.getRevolvingAmount()));
			ManagerConf.saveToLocal("clerk_back", NumUtil.dcm2String(RamStatic.cashierData.getBackAmount()));
			ManagerConf.saveToLocal("clerk_num", RamStatic.cashierData.getOddNum() + "");
			ManagerConf.saveToLocal("clerk_login_datetime", RamStatic.cashierData.getLoginDatetime());
			ManagerConf.saveToLocal("clerk_serial", RamStatic.receiptSn + "");
			ManagerConf.saveToLocal("clerk_last", RamStatic.lastReceipDate);
			ManagerConf.saveToLocal("clerk_gift", NumUtil.dcm2String(RamStatic.cashierData.getGiftAmount()));
			ManagerConf.saveToLocal("clerk_online", NumUtil.dcm2String(RamStatic.cashierData.getOnlinePayAmount()));
			List<CashierTicketPayment> cashierTicketPayments = RamStatic.cashierData.getCashierTicketPayments();
			for (CashierTicketPayment payment : cashierTicketPayments) {
				int code = payment.getSdkCustomerPayMethod().getCode();
				D.out("clerk_customer_pay_" + code + " = " + payment.getAmount());
				ManagerConf.saveToLocal("clerk_customer_pay_" + code,
					NumUtil.dcm2String(payment.getAmount()));
			}
			ManagerConf.saveToLocal("clerk_buy_pass_product_cash", NumUtil.dcm2String(RamStatic.cashierData.getBuyPassProductCashAmount()));
			ManagerConf.saveToLocal("clerk_buy_pass_product_union", NumUtil.dcm2String(RamStatic.cashierData.getBuyPassProductUnionAmount()));
			ManagerConf.saveToLocal("clerk_credit", NumUtil.dcm2String(RamStatic.cashierData.getCreditAmount()));
		} else {
			ManagerConf.saveToLocal("clerk_id", "");
			ManagerConf.saveToLocal("clerk_amount", "0.00");
			ManagerConf.saveToLocal("clerk_money", "0.00");
			ManagerConf.saveToLocal("clerk_bank", "0.00");
			ManagerConf.saveToLocal("clerk_koolpay", "0.00");
			ManagerConf.saveToLocal("clerk_member", "0.00");
			ManagerConf.saveToLocal("clerk_charge", "0.00");
			ManagerConf.saveToLocal("clerk_charge_cash", "0.00");
			ManagerConf.saveToLocal("clerk_charge_union", "0.00");
			ManagerConf.saveToLocal("clerk_charge_alipay", "0.00");
			ManagerConf.saveToLocal("clerk_revolving", "-1");
			ManagerConf.saveToLocal("clerk_back", "0.00");
			ManagerConf.saveToLocal("clerk_num", "");
			ManagerConf.saveToLocal("clerk_login_datetime", RamStatic.cashierData.getLoginDatetime());
			ManagerConf.saveToLocal("clerk_gift", "0.00");
			ManagerConf.saveToLocal("clerk_online", "0.00");
			List<CashierTicketPayment> cashierTicketPayments = RamStatic.cashierData.getCashierTicketPayments();
			for (CashierTicketPayment payment : cashierTicketPayments) {
				int code = payment.getSdkCustomerPayMethod().getCode();
				D.out("clerk_customer_pay_" + code + " = " + payment.getAmount());
				ManagerConf.saveToLocal("clerk_customer_pay_" + code,
					NumUtil.dcm2String(payment.getAmount()));
			}
			ManagerConf.saveToLocal("clerk_use_pass_product", "0.00");
			ManagerConf.saveToLocal("clerk_buy_pass_product_cash", "0.00");
			ManagerConf.saveToLocal("clerk_buy_pass_product_union", "0.00");
			ManagerConf.saveToLocal("clerk_credit", "0.00");
		}
	}
*/
	/**
	 * 读取收银员信息
	 */
    /*
	public static void loadClerkData() {
		long clerkId = Long.parseLong(ManagerConf.readFromLocal("clerk_id", "0"));
		TableSdkCashier table_Clerk = TableSdkCashier.getInstance();

		ArrayList<SdkCashier> getClerks = table_Clerk.searchDatas("uid=? AND enable=?", new String[] { clerkId + "", "1" });

		if (getClerks.size() == 1) {
			if(RamStatic.cashierData == null) {
				RamStatic.cashierData = new CashierData(getClerks.get(0));
			} else {
				RamStatic.cashierData.setLoginCashier(getClerks.get(0));
				List<CashierTicketPayment> cashierTicketPayments
						= new ArrayList<>(RamStatic.sdkCustomerPayMethods.size());
				for (SdkCustomerPayMethod customerPayMethod : RamStatic.sdkCustomerPayMethods) {
					CashierTicketPayment payment = new CashierTicketPayment();
					int code =  customerPayMethod.getCode();
					BigDecimal amount = new BigDecimal(ManagerConf.readFromLocal("clerk_customer_pay_" + code, "0.00"));
					payment.setAmount(amount);
					payment.setSdkCustomerPayMethod(customerPayMethod);
					cashierTicketPayments.add(payment);
				}

				RamStatic.cashierData.setCashierTicketPayments(cashierTicketPayments);
			}
		} else {
			return;
		}
		RamStatic.cashierData.setSaleAmount(new BigDecimal(ManagerConf.readFromLocal("clerk_amount", "0.00")));
		RamStatic.cashierData.setThirdPartyPaymentAmount(new BigDecimal(ManagerConf.readFromLocal("clerk_koolpay", "0.00")));
		RamStatic.cashierData.setChargeAmount(new BigDecimal(ManagerConf.readFromLocal("clerk_charge", "0.00")));
		RamStatic.cashierData.setChargeCashAmount(new BigDecimal(ManagerConf.readFromLocal("clerk_charge_cash", "0.00")));
		RamStatic.cashierData.setChargeUnionAmount(new BigDecimal(ManagerConf.readFromLocal("clerk_charge_union", "0.00")));
		RamStatic.cashierData.setChargeAlipayAmount(new BigDecimal(ManagerConf.readFromLocal("clerk_charge_alipay", "0.00")));
		RamStatic.cashierData.setRevolvingAmount(new BigDecimal(ManagerConf.readFromLocal("clerk_revolving", "0.00")));
		RamStatic.cashierData.setBackAmount(new BigDecimal(ManagerConf.readFromLocal("clerk_back", "0.00")));
		RamStatic.cashierData.setOddNum(Integer.parseInt(ManagerConf.readFromLocal("clerk_num", "0")));
		RamStatic.receiptSn = Integer.parseInt(ManagerConf.readFromLocal("clerk_serial", "0"));
		RamStatic.lastReceipDate = ManagerConf.readFromLocal("clerk_last", "");
		RamStatic.bysMarkNo = ManagerData.getBysMarkNo();
		RamStatic.cashierData.setGiftAmount(new BigDecimal(ManagerConf.readFromLocal("clerk_gift", "0.00")));
		RamStatic.cashierData.setBuyPassProductCashAmount(new BigDecimal(ManagerConf.readFromLocal("clerk_buy_pass_product_cash", "0.00")));
		RamStatic.cashierData.setBuyPassProductUnionAmount(new BigDecimal(ManagerConf.readFromLocal("clerk_buy_pass_product_union", "0.00")));
		RamStatic.cashierData.setCreditAmount(new BigDecimal(ManagerConf.readFromLocal("clerk_credit", "0.00")));
        RamStatic.cashierData.setLoginDatetime(ManagerConf.readFromLocal("clerk_login_datetime", ""));
	}
	*/
	public static String getLastReceiptDate() {
		return ManagerConf.readFromLocal("clerk_last", "");
	}
	
	public static String getLastReceiptSn() {
		return ManagerConf.readFromLocal("clerk_serial", "0");
	}

	public static BigDecimal getLastRevolvingAmount() {
		return new BigDecimal(ManagerConf.readFromLocal("clerk_revolving", "-1"));
	}

	public static final String CHECK_YES = "y";
	public static final String CHECK_NO = "n";

	/**
	 * 获取录入时是否检查库存 默认为不检测
	 *
	 * @return
	 */
	public static String getCheckStock() {
		return ManagerConf.readFromLocal("check_stock", CHECK_NO);
	}

	/**
	 * 保存录入时是否检查库存
	 */
	public static void saveCheckStock(String checked) {
		ManagerConf.saveToLocal("check_stock", checked);
	}

	/**
	 * 获取上一次推送时间
	 * 
	 * @return
	 */
	public static String getLastPushTime() {
		return ManagerConf.readFromLocal("push_datetime", DatetimeUtil.DEFAULT_SYNC_DATETIME);
	}

	/**
	 * 保存上一次推送时间
	 * 
	 * @param date
	 *            推送到达时间
	 */
	public static void saveLastPushTime(String date) {
		ManagerConf.saveToLocal("push_datetime", date);
	}

	/**
	 * 清除上一次推送时间
	 * 
	 */
	public static void clearLastPushTime() {
		ManagerConf.saveToLocal("push_datetime", DatetimeUtil.DEFAULT_DATETIME);
	}
	
	public static String getNeedUpdate() {
		return ManagerConf.readFromLocal("need_update", "1");
	}
	
	public static void saveNeedUpdate(String needUpdate) {
		ManagerConf.saveToLocal("need_update", needUpdate);
	}


	public static String getCarReaderIpInfo() {
		return ManagerConf.readFromLocal("verifone_ip_info", "");
	}

	public static void saveCarReaderIpInfo(String ipInfo) {
		ManagerConf.saveToLocal("verifone_ip_info", ipInfo);
	}

	public static String getCarReaderPortInfo() {
		return ManagerConf.readFromLocal("verifone_port_info", "8899");
	}

	public static void saveCarReaderPortInfo(String portInfo) {
		ManagerConf.saveToLocal("verifone_port_info", portInfo);
	}

	public static boolean getNeedInputTableCnt() {
		String needStr = ManagerConf.readFromLocal("need_table_cnt", "1");
		
		return needStr.equals(ENABLE);
	}
	
	public static void saveNeedInputTableCnt(boolean isNeed) {
		ManagerConf.saveToLocal("need_table_cnt", isNeed ? ENABLE : DISABLE);
	}

	public static boolean getUseFrontCamera() {
		String needStr = ManagerConf.readFromLocal("use_front_camera", "0");
		
		return needStr.equals(ENABLE);
	}
	
	public static void saveUseFrontCamera(boolean used) {
		ManagerConf.saveToLocal("use_front_camera", used ? ENABLE : DISABLE);
	}

	public static boolean getDefaultMarkNo() {
		String needStr = ManagerConf.readFromLocal("default_markno", "1");
		
		return needStr.equals(ENABLE);
	}
	
	public static void saveDefaultMarkNo(boolean used) {
		ManagerConf.saveToLocal("default_markno", used ? ENABLE : DISABLE);
	}

	public static boolean getUseLakala() {
		String needStr = ManagerConf.readFromLocal("use_lakala", "0");
		
		return needStr.equals(ENABLE);
	}
	
	public static void saveUseLakala(boolean used) {
		ManagerConf.saveToLocal("use_lakala", used ? ENABLE : DISABLE);
	}

	public static boolean getSearchAutoAdd() {
		String needStr = ManagerConf.readFromLocal("search_auto_add", "1");
		
		return needStr.equals(ENABLE);
	}
	
	public static void saveSearchAutoAdd(boolean used) {
		ManagerConf.saveToLocal("search_auto_add", used ? ENABLE : DISABLE);
	}

	public static void saveFirstInit() {
		ManagerConf.saveToLocal("first_init", "1");
	}

	public static String getFirstInit() {
		return ManagerConf.readFromLocal("first_init", "0");
	}

	/**
	 * 是否第一次登陆收银员
	 * @param first
	 */
	public static void saveFirstCashierLogin(boolean first) {
		ManagerConf.saveToLocal("firstCashierLogin", first ? ENABLE : DISABLE);
	}

	public static boolean getFirstCashierLogin() {
		return ManagerConf.readFromLocal("firstCashierLogin", ENABLE).equals(ENABLE);
	}
	
	private static final int MAX_BYS_MARKNO = 999;

	public static int getBysMarkNo() {
		int bysMarkNo = Integer.parseInt(ManagerConf.readFromLocal("bysMarkNo", "1"));
		if(bysMarkNo > MAX_BYS_MARKNO) {
			bysMarkNo = 1;
		}
		
		return bysMarkNo;
	}

	public static void saveSessionId(String sessionId) {
		ManagerConf.saveToLocal("sessionId", sessionId);
	}
	
	public static String getSessionId() {
		return ManagerConf.readFromLocal("sessionId", null);
	}


	public static String getLakalaAccount() {
		String account = ManagerConf.readFromLocal("lakala_account", "");
		if(!account.equals("")) {
			try {
				String deCryoAccount = PasswordUtil.decryptToString(account);
				account = deCryoAccount;
			} catch (IOException e) {
				D.out(e);
			}
		}
		return account;
	}

	public static void saveLakalaAccount(String lakalaAccount) {
		String enCryoAccount = lakalaAccount;
		if(!lakalaAccount.equals("")) {
			try {
				enCryoAccount = PasswordUtil.encryptToString(lakalaAccount);
			} catch (IOException e) {
				D.out(e);
			}
		}
		ManagerConf.saveToLocal("lakala_account", enCryoAccount);
	}

	public static String getLakalaPassword() {
		String password = ManagerConf.readFromLocal("lakala_password", "");
		if(!password.equals("")) {
			try {
				String deCryoPsw = PasswordUtil.decryptToString(password);
				password = deCryoPsw;
			} catch (IOException e) {
				D.out(e);
			}
		}
		return password;
	}

	public static void saveLakalaPassword(String lakalaPassword) {
		String enCryoPsw = lakalaPassword;
		if(!lakalaPassword.equals("")) {
			try {
				enCryoPsw = PasswordUtil.encryptToString(lakalaPassword);
			} catch (IOException e) {
				D.out(e);
			}
		}
		ManagerConf.saveToLocal("lakala_password", enCryoPsw);
	}

	/**
	 * 自助点餐是否需要条形码
	 * @return
	 */
	public static boolean getBysNeedBarcode() {
		return ManagerConf.readFromLocal("bys_need_barcode", "1").equals(ENABLE);
	}

	public static void saveBysNeedBarcode(boolean need) {
		ManagerConf.saveToLocal("bys_need_barcode", need ? ENABLE : DISABLE);
	}
	
	/**
	 * 自助点餐是否需要收据
	 * @return
	 */
	public static boolean getBysNeedReceipt() {
		return ManagerConf.readFromLocal("bys_need_receipt", "0").equals(ENABLE);
	}
	
	public static void saveBysNeedReceipt(boolean need) {
		ManagerConf.saveToLocal("bys_need_receipt", need ? ENABLE : DISABLE);
	}
	
	public static void saveNewUser(boolean isNew) {
		ManagerConf.saveToLocal("new_user", isNew ? ENABLE : DISABLE);
	}
	
	public static boolean getNewUser() {
		return ManagerConf.readFromLocal("new_user", "0").equals(ENABLE);
	}
	
	public static void saveBtEnable(boolean enable) {
		ManagerConf.saveToLocal("bt_enable", enable ? ENABLE : DISABLE);
	}
	
	public static boolean getBtEnable() {
		return ManagerConf.readFromLocal("bt_enable", "0").equals(ENABLE);
	}
	
	public static void saveBtAddr(String addr) {
		ManagerConf.saveToLocal("bt_addr", addr);
	}
	
	public static String getBtAddr() {
		return ManagerConf.readFromLocal("bt_addr", "");
	}

	public static void saveLabelBtEnable(boolean enable) {
		ManagerConf.saveToLocal("label_bt_enable", enable ? ENABLE : DISABLE);
	}

	public static boolean getLabelBtEnable() {
		return ManagerConf.readFromLocal("label_bt_enable", "0").equals(ENABLE);
	}

	public static void saveLabelBtAddr(String addr) {
		ManagerConf.saveToLocal("label_bt_addr", addr);
	}

	public static String getLabelBtAddr() {
		return ManagerConf.readFromLocal("label_bt_addr", "");
	}
	
	public static void saveUsbPrinterInfo(SdkUsbInfo sdkUsbInfo) {
        if(sdkUsbInfo == null) {
            ManagerConf.saveToLocal("sdkUsbInfo", "");
        } else {
            ManagerConf.saveToLocal("sdkUsbInfo", GsonUtil.getInstance().toJson(sdkUsbInfo));
        }
	}
	
	public static SdkUsbInfo getUsbPrinterInfo() {
		String str = ManagerConf.readFromLocal("sdkUsbInfo", "");
		if(str.equals("")) {
			return null;
		} else {
			return GsonUtil.getInstance().fromJson(str, SdkUsbInfo.class);
		}
	}
	
	public static void saveBaudratePosition(int baudrate) {
		ManagerConf.saveToLocal("baudrate", baudrate + "");
	}
	
	public static int getBaudratePosition() {
		String defaultBaudrate = "2";
		if (AppConfig.company.equals(Constance.COMPANY_HAOSHUN2)) {
			defaultBaudrate = "3";
		}
		if (AppConfig.company.equals(Constance.COMPANY_CHINAZBC)) {
			defaultBaudrate = "4";
		}
		if (AppConfig.company.equals(Constance.COMPANY_EJETON)) {
			defaultBaudrate = "4";
		}
        D.out("defaultBaudrate defaultBaudrate = " + defaultBaudrate);
		return Integer.parseInt(ManagerConf.readFromLocal("baudrate", defaultBaudrate));
	}

    public static void saveDspBaudrate(int baudrate) {
        ManagerConf.saveToLocal("dsp_baudrate", baudrate + "");
    }

    public static int getDspBaudrate() {
        return Integer.parseInt(ManagerConf.readFromLocal("dsp_baudrate", "0"));
    }
	
	/**
	 * 保存网络支付（支付宝、京东支付、微支付）扫描方式
	 * @param type		0:摄像头，1：扫描枪
	 */
	public static void saveOnlinePayScanType(int type) {
		ManagerConf.saveToLocal("online_pay_scan_type", type + "");
	}
	
	public static int getOnlinePayType() {
		String defaultType = "0";
	    int numCameras = Camera.getNumberOfCameras();
	    if (numCameras == 0) {
	    	defaultType = "1";
	    }
		return Integer.parseInt(ManagerConf.readFromLocal("online_pay_scan_type", defaultType));
    }

    public static void saveUseRevolving(boolean use) {
        ManagerConf.saveToLocal("revolving", use ? "1" : "");
    }

    public static boolean getUseRevolving() {
        return ManagerConf.readFromLocal("revolving", "0").equals(ENABLE);
    }

    public static void saveScaleType(int scaleType) {
        ManagerConf.saveToLocal("scale_type", scaleType + "");
    }

    public static int getScaleType() {
        return Integer.parseInt(ManagerConf.readFromLocal("scale_type", "0"));
    }

    public static void saveCardReaderType(int cardReaderType) {
        ManagerConf.saveToLocal("card_reader_type", cardReaderType + "");
    }

    public static int getCardReaderType() {
        return Integer.parseInt(ManagerConf.readFromLocal("card_reader_type", "0"));
    }

    public static void saveKitchenPrintPrice(boolean print) {
        ManagerConf.saveToLocal("KitchenPrintPrice", print ? "1" : "0");
    }

    public static boolean getKitchenPrintPrice() {
        return ManagerConf.readFromLocal("KitchenPrintPrice", "1").equals(ENABLE);
    }

    // 副屏在销售时候是否播放广告
    public static void saveADWhenSelling(boolean play) {
        ManagerConf.saveToLocal("ad_at_selling", play ? ENABLE : DISABLE);
    }

    public static boolean getADWhenSelling() {
        return Integer.parseInt(ManagerConf.readFromLocal("ad_at_selling", "0")) == 1;
    }

    // 副屏是否播放视频
    public static void saveUseVideo(boolean use) {
        ManagerConf.saveToLocal("use_video", use ? ENABLE : DISABLE);
    }

    public static boolean getUseVideo() {
        return Integer.parseInt(ManagerConf.readFromLocal("use_video", "0")) == 1;
    }

    // 副屏是否播放图片
    public static void saveUsePicture(boolean use) {
        ManagerConf.saveToLocal("use_picture", use ? ENABLE : DISABLE);
    }

    public static boolean getUsePicture() {
        return Integer.parseInt(ManagerConf.readFromLocal("use_picture", "1")) == 1;
    }

    // 副屏是否播放音乐
    public static void saveUseVoice(boolean use) {
        ManagerConf.saveToLocal("use_voice", use ? ENABLE : DISABLE);
    }

    public static boolean getUseVoice() {
        return Integer.parseInt(ManagerConf.readFromLocal("use_voice", "1")) == 1;
    }

    public static int getWaitTime() {
        return Integer.parseInt(ManagerConf.readFromLocal("wait_time", "5"));
    }

    public static void saveWaitTime(int waitTime) {
        ManagerConf.saveToLocal("wait_time", waitTime + "");
    }
    
    
    /**
	 * 保存app版本号
	 * @param versionName
	 */
	public static void saveVersionName(String versionName){
		ManagerConf.saveToLocal("version_name", versionName);
	}
	
	/**
	 * 获取app版本号
	 * @return
	 */
	public static String getVersionName(){
		return ManagerConf.readFromLocal("version_name", "1.00");
	}

    /**
     * 保持结账是否打印
     * @param needPrint
     */
    public static void saveCheckoutPrint(boolean needPrint) {
        ManagerConf.saveToLocal("checkout_print", needPrint ? ENABLE : DISABLE);
    }

    public static boolean getCheckoutPrint() {
        return ManagerConf.readFromLocal("checkout_print", "1").equals(ENABLE);
    }

    /**
     * 货币符号保存
     * @param symbol
     */
    public static final void saveCurrencySymbolPosition(int symbol) {
        ManagerConf.saveToLocal("currency_symbol_position", symbol + "");
    }

    public static final int getCurrencySymbolPosition() {
        return Integer.parseInt(ManagerConf.readFromLocal("currency_symbol_position", "0"));
    }

    public static final void saveNeedSyncVersion(long needUpdateVersion) {
        ManagerConf.saveToLocal("needSyncVersion", needUpdateVersion + "");
    }

    public static final long getNeedSyncVersion() {
        return Long.parseLong(ManagerConf.readFromLocal("needSyncVersion", "0"));
    }

	/**
	 * 总机是否打印分机压桌单
	 * @param print
	 */
    public static final void saveHostPrintClientHangTableReceipt(boolean print) {
        ManagerConf.saveToLocal("hostPrintClientHangTableReceipt", print ? ENABLE : DISABLE);
    }

    public static final boolean getHostPrintClientHangTableReceipt() {
        return Integer.parseInt(ManagerConf.readFromLocal("hostPrintClientHangTableReceipt", "1")) == 1;
    }

	/**
	 * 网口小票机是否使用指令检测
	 * @param check
	 */
    public static final void saveCheckNetPrinterByCmd(boolean check) {
        ManagerConf.saveToLocal("checkNetPrinterByCmd", check ? ENABLE : DISABLE);
    }

    public static final boolean getCheckNetPrinterByCmd() {
        return Integer.parseInt(ManagerConf.readFromLocal("checkNetPrinterByCmd", "0")) == 1;
    }

	/**
	 * 厨打是否使用云端配置
	 * @param use
	 */
    public static final void saveUseNetKitchenPrinter(boolean use) {
        ManagerConf.saveToLocal("useNetKitchenPrinter", use ? ENABLE : DISABLE);
    }

    public static final boolean getUseNetKitchenPrinter() {
        return Integer.parseInt(ManagerConf.readFromLocal("useNetKitchenPrinter", "0")) == 1;
    }

	/**
	 * 自助点餐是否主扫
	 * @param use
	 */
    public static final void saveHelpYourselfInitiative(boolean use) {
        ManagerConf.saveToLocal("helpYourselfInitiative", use ? ENABLE : DISABLE);
    }

    public static final boolean getHelpYourselfInitiative() {
        return Integer.parseInt(ManagerConf.readFromLocal("helpYourselfInitiative", "1")) == 1;
    }

	/**
	 * 自助点餐起始号码
	 * @param hysStartNum
	 */
    public static final void saveHysStartNum(int hysStartNum) {
        ManagerConf.saveToLocal("hysStartNum", hysStartNum + "");
    }

    public static final int getHysStartNum() {
        return Integer.parseInt(ManagerConf.readFromLocal("hysStartNum", "0"));
    }

	/**
	 * 分机是否支持收银
	 * @param use
	 */
    public static final void saveClientCheckout(boolean use) {
        ManagerConf.saveToLocal("client_checkout", use ? ENABLE : DISABLE);
    }

    public static final boolean getClientCheckout() {
        return Integer.parseInt(ManagerConf.readFromLocal("client_checkout", "0")) == 1;
    }

	/**
	 * 分机是否支持收银
	 * @param use
	 */
    public static final void saveReceiptFeedback(boolean use) {
        ManagerConf.saveToLocal("receiptFeedback", use ? ENABLE : DISABLE);
    }

    public static final boolean getReceiptFeedback() {
        return Integer.parseInt(ManagerConf.readFromLocal("receiptFeedback", "0")) == 1;
    }

	/**
	 * 自助点餐牌号是否倍高倍宽
	 * @param use
	 */
    public static final void saveHysNoDWDH(boolean use) {
        ManagerConf.saveToLocal("HysNoDWDH", use ? ENABLE : DISABLE);
    }

    public static final boolean getHysNoDWDH() {
        return Integer.parseInt(ManagerConf.readFromLocal("HysNoDWDH", "1")) == 1;
    }

	/**
	 * 自助点餐是否使用没有设置过自助下单的商品
	 * @param use
	 */
    public static final void saveHysUseAllProduct(boolean use) {
        ManagerConf.saveToLocal("hysUseAllProduct", use ? ENABLE : DISABLE);
    }

    public static final boolean getHysUseAllProduct() {
        return Integer.parseInt(ManagerConf.readFromLocal("hysUseAllProduct", "1")) == 1;
    }

	/**
	 * 自助点餐是否输入牌号
	 * @param use
	 */
    public static final void saveHysNoInput(boolean use) {
        ManagerConf.saveToLocal("hysNoInput", use ? ENABLE : DISABLE);
    }

    public static final boolean getHysNoInput() {
        return Integer.parseInt(ManagerConf.readFromLocal("hysNoInput", "0")) == 1;
    }

	/**
	 * 自助点餐是否显示商品详情
	 * @param use
	 */
    public static final void saveHysShowDetail(boolean use) {
        ManagerConf.saveToLocal("hysShowDetail", use ? ENABLE : DISABLE);
    }

    public static final boolean getHysShowDetail() {
        return Integer.parseInt(ManagerConf.readFromLocal("hysShowDetail", "1")) == 1;
    }

	/**
	 * 网络类型
	 * @param  type
	 */
	public static final void saveNetType(int type) {
		ManagerConf.saveToLocal("netType", type + "");
	}

	public static final int getNetType() {
		return Integer.parseInt(ManagerConf.readFromLocal("netType", "0"));
	}

	/**
	 * 分机是否支持收银
	 * @param use
	 */
	public static final void saveHysExitStillPlayMusic(boolean use) {
		ManagerConf.saveToLocal("hysExitStillPlayMusic", use ? ENABLE : DISABLE);
	}

	public static final boolean getHysExitStillPlayMusic() {
		return Integer.parseInt(ManagerConf.readFromLocal("hysExitStillPlayMusic", "1")) == 1;
	}

    /**
     * 网络类型
     * @param use
     */
    public static final void saveUseImmersiveMode(boolean use) {
        ManagerConf.saveToLocal("immersive_mode", use ? ENABLE : DISABLE);
    }

    public static final boolean getUseImmersiveMode() {
        return Integer.parseInt(ManagerConf.readFromLocal("immersive_mode", "1")) == 1;
    }

	/**
	 * 小数点位数
	 * @param scaleDigitType
	 */
	public static final void saveScaleDigitType(int scaleDigitType) {
		ManagerConf.saveToLocal("scaleDigitType", scaleDigitType + "");
	}

	public static final int getScaleDigitType() {
		return Integer.parseInt(ManagerConf.readFromLocal("scaleDigitType", "0"));
	}

    public static final String DB_INIT_VERSION = "0.00";
	/**
	 * 老版本号
	 * @param oldVersion
	 */
	public static final void saveOldVersion(String oldVersion) {
		ManagerConf.saveToLocal("oldVersion", oldVersion);
	}

	public static final String getOldVersion() {
		return ManagerConf.readFromLocal("oldVersion", DB_INIT_VERSION);
	}

	/**
	 * 单据使用备注
	 * @param useReceiptRemarks
	 */
	public static void saveUseReceiptRemarks(boolean useReceiptRemarks) {
		ManagerConf.saveToLocal("useReceiptRemarks", useReceiptRemarks ? ENABLE : DISABLE);
	}

	public static boolean getUseReceiptRemarks() {
		return Integer.parseInt(ManagerConf.readFromLocal("useReceiptRemarks", "0")) == 1;
	}

	/**
	 * 通知间隔
	 * @param notifyIntervalValue
	 */
	public static void saveNotifyIntervalValue(int notifyIntervalValue) {
		ManagerConf.saveToLocal("notifyIntervalValue", notifyIntervalValue + "");
	}

	public static int getNotifyIntervalValue() {
		return Integer.parseInt(ManagerConf.readFromLocal("notifyIntervalValue", "3"));
	}

	/**
	 * 获取上一次手动同步时间
	 *
	 * @return
	 */
	public static String getQuerySyncDateTime() {
		return ManagerConf.readFromLocal("query_sync_datetime", DatetimeUtil.DEFAULT_DATETIME);
	}

	public static void saveQuerySyncDateTime(String saveDateTime) {
		ManagerConf.saveToLocal("query_sync_datetime", saveDateTime);
	}

	/**
	 * 自助点餐是否支持会员卡支付
	 * @param use
	 */
	public static final void saveSupportCustomerPay(boolean use) {
		ManagerConf.saveToLocal("hysSupportCustomerPay", use ? ENABLE : DISABLE);
	}

	public static final boolean getSupportCustomerPay() {
		return Integer.parseInt(ManagerConf.readFromLocal("hysSupportCustomerPay", "0")) == 1;
	}

	/**
	 * 自助点餐屏幕失灵校正
	 * @param use
	 */
	public static final void saveHysTouchCorrect(boolean use) {
		ManagerConf.saveToLocal("hysTouchCorrect", use ? ENABLE : DISABLE);
	}

	public static final boolean getHysTouchCorrect() {
		String defaultValue = "0";
		if (AppConfig.company.equals(Constance.COMPANY_GENSTAR)) {
			defaultValue = "1";
		}
		return Integer.parseInt(ManagerConf.readFromLocal("hysTouchCorrect", defaultValue)) == 1;
	}

	public static void saveKitchenPrintCustomer(boolean print) {
		ManagerConf.saveToLocal("KitchenPrintCustomer", print ? ENABLE : DISABLE);
	}

	public static boolean getKitchenPrintCustomer() {
		return Integer.parseInt(ManagerConf.readFromLocal("KitchenPrintCustomer", "1")) == 1;
	}

	public static void saveNetWarningTime(int position) {
		ManagerConf.saveToLocal("NetWarningTime", position + "");
	}

	public static int getNetWarningTime() {
		return Integer.parseInt(ManagerConf.readFromLocal("NetWarningTime", "3"));
	}

	public static void saveIndustry(int industryCode) {
		ManagerConf.saveToLocal("industryCode", industryCode + "");
	}

	public static int getIndustry() {
		return Integer.parseInt(ManagerConf.readFromLocal("industryCode", "-1"));
	}

	public static final int IMG_TYPE_NONE = 0;
	public static final int IMG_TYPE_LITTLE = 1;
	public static final int IMG_TYPE_LARGE = 2;
	public static void saveMainProductShowType(int mainProductShowType) {
		ManagerConf.saveToLocal("mainProductShowType", mainProductShowType + "");
	}

	public static int getMainProductShowType() {
		return Integer.parseInt(ManagerConf.readFromLocal("mainProductShowType", "1"));
	}

    public static final String getSeialPrinterPort() {
        return ManagerConf.readFromLocal("serialPrinterPort", AppConfig.DEFAULT_PRINTER_PORT);
    }

    public static final void saveSerialPrinterPort(String port) {
        ManagerConf.saveToLocal("serialPrinterPort", port);
    }

    public static final String getSerialLedPort() {
        return ManagerConf.readFromLocal("serialLedPort", AppConfig.DEFAULT_LED_PORT);
    }

    public static final void saveSerialLedPort(String port) {
        ManagerConf.saveToLocal("serialLedPort", port);
    }

    public static final String getSerialScalePort() {
        return ManagerConf.readFromLocal("serialScalePort", AppConfig.DEFAULT_SCALE_PORT);
    }

    public static final void saveSerialScalePort(String port) {
        ManagerConf.saveToLocal("serialScalePort", port);
    }

    /**
     * 会员使用m1卡
     * @param customerUseM1Card
     */
    public static void saveCustomerUseM1Card(boolean customerUseM1Card) {
        ManagerConf.saveToLocal("customerUseM1Card", customerUseM1Card ? ENABLE : DISABLE);
    }

    public static boolean getCustomerUseM1Card() {
        return Integer.parseInt(ManagerConf.readFromLocal("customerUseM1Card", "0")) == Constance.ENABLE;
    }

    /**
     * 会员使用m1卡
     * @param printCheckout
     */
    public static void savePrintCheckout(boolean printCheckout) {
        ManagerConf.saveToLocal("printCheckout", printCheckout ? ENABLE : DISABLE);
    }

    public static boolean getPrintCheckout() {
        return Integer.parseInt(ManagerConf.readFromLocal("printCheckout", "1")) == Constance.ENABLE;
    }

	/**
	 * 是否显示会员使用M1卡选项
	 * @param isShow
     */
	public static void saveShowCustomerUseM1Card(boolean isShow) {
		ManagerConf.saveToLocal("showCustomerUseM1Card", isShow ? ENABLE : DISABLE);
	}

	public static boolean getShowCustomerUseM1Card() {
		return Integer.parseInt(ManagerConf.readFromLocal("showCustomerUseM1Card", ENABLE)) == Constance.ENABLE;
	}

	/**
	 * 牌号起始值
	 * @param minNum
     */
	public static void saveMinMarkNo(int minNum) {
		ManagerConf.saveToLocal("minMarkNo", minNum + "");
	}

	public static int getMinMarkNo() {
		return Integer.parseInt(ManagerConf.readFromLocal("minMarkNo", "1"));
	}

	/**
	 * 牌号最大值
	 * @param maxNum
	 */
	public static void saveMaxMarkNo(int maxNum) {
		ManagerConf.saveToLocal("maxMarkNo", maxNum + "");
	}

	public static int getMaxMarkNo() {
		return Integer.parseInt(ManagerConf.readFromLocal("maxMarkNo", "9999"));
	}

    public static boolean getUsePayment() {
        return ManagerConf.readFromLocal("usePayment", "0").equals(ENABLE);
    }

    public static void saveUsePayment(boolean usePayment) {
        ManagerConf.saveToLocal("usePayment", usePayment ? ENABLE : DISABLE);
    }

    public static boolean getHysUseDelivery() {
        return ManagerConf.readFromLocal("hysUseDelivery", "0").equals(ENABLE);
    }

    public static void saveHysUseDelivery(boolean useDelivery) {
        ManagerConf.saveToLocal("hysUseDelivery", useDelivery ? ENABLE : DISABLE);
    }

    public static String getHysTakeOutCost() {
        return ManagerConf.readFromLocal("HysTakeOutCost", "0");
    }

    public static void saveHysTakeOutCost(String cost) {
        ManagerConf.saveToLocal("HysTakeOutCost", cost);
    }

    public static boolean getUseMode() {
        return ManagerConf.readFromLocal("useMode", "0").equals(ENABLE);
    }

    public static void saveUseMode(boolean useMode) {
        ManagerConf.saveToLocal("useMode", useMode ? ENABLE : DISABLE);
    }

    /**
     *  外卖订单查询起始时间
     * @param time
     */
    public static void saveNextQueryStartTime(String time) {
        ManagerConf.saveToLocal("nextQueryStartTime", time);
    }

    public static String getNextQueryStartTime() {
        return ManagerConf.readFromLocal("nextQueryStartTime");
    }

    /**
     *	外卖订单一键设置
     * @param isAuto
     */
    public static void saveTakeOutAutoSetting(boolean isAuto) {
        ManagerConf.saveToLocal("autoSetting", isAuto ? ENABLE : DISABLE);
    }

    public static boolean getTakeOutAutoSetting() {
        return Integer.parseInt(ManagerConf.readFromLocal("autoSetting", "0")) == 1;
    }

    /**
     *  一键接单
     * @param isAuto
     */
    public static void saveTakeOutAutoReceive(boolean isAuto) {
        ManagerConf.saveToLocal("autoReceive", isAuto ? ENABLE : DISABLE);

    }

    public static boolean getTakeOutAutoReceive() {
        return Integer.parseInt(ManagerConf.readFromLocal("autoReceive", "0")) == 1;
    }

    /**
     *  一键厨打
     * @param isAuto
     */
    public static void saveTakeOutAutoKds(boolean isAuto) {
        ManagerConf.saveToLocal("autoKDS", isAuto ? ENABLE : DISABLE);

    }

    public static boolean getTakeOutAutoKds() {
        return Integer.parseInt(ManagerConf.readFromLocal("autoKDS", "0")) == 1;
    }

    /**
     *  一键配送
     * @param isAuto
     */
    public static void saveTakeOutAutoDelivery(boolean isAuto) {
        ManagerConf.saveToLocal("autoDelivery", isAuto ? ENABLE : DISABLE);

    }

    public static boolean getTakeOutAutoDelivery() {
        return Integer.parseInt(ManagerConf.readFromLocal("autoDelivery", "0")) == 1;
    }

    /**
     *  一键收银
     * @param isAuto
     */
    public static void saveTakeOutAutoCheckOut(boolean isAuto) {
        ManagerConf.saveToLocal("autoCheckOut", isAuto ? ENABLE : DISABLE);

    }

    public static boolean getTakeOutAutoCheckOut() {
        return Integer.parseInt(ManagerConf.readFromLocal("autoCheckOut", "0")) == 1;
    }

    /**
     *  接受外卖订单开关控制
     * @param receive
     */
    public static void saveReceiveTakeOutSetting(boolean receive) {
        ManagerConf.saveToLocal("receiverTakeOut", receive ? ENABLE : DISABLE);
    }

    public static boolean getReceiveTakeOutSetiing() {
        return Integer.parseInt(ManagerConf.readFromLocal("receiverTakeOut", "1")) == 1;
    }

	/**
	 * 标签打印内容设置
	 */
	public static void saveLabelPrintContentSettings(boolean[] settings) {
		ManagerConf.saveToLocal("labelPrintContentSetting", GSON.toJson(settings));
	}

	public static void resetLabelPrintContentettings() {
		ManagerConf.saveToLocal("labelPrintContentSetting",
                "[true,true,true,true,false,false,false,false,false,false]");
	}

	public static boolean[] getLabelPrintContentSettings() {
        String settings = ManagerConf.readFromLocal("labelPrintContentSetting",
                "[true,true,true,true,false,false,false,false,false,false,false]");
        boolean[] oldSettings = GSON.fromJson(settings, boolean[].class);
        boolean[] newSetting = new boolean[11];
        for (int i = 0; i < oldSettings.length; i++) {
            newSetting[i] = oldSettings[i];
        }

        oldSettings = null;
        return newSetting;
    }

	/**
	 * 标签打印尾注设置
	 */
	public static void saveLabelPrintTail(String tail) {
		ManagerConf.saveToLocal("labelPrintTail", tail);
	}

	public static String getLabelPrintTail() {
        return ManagerConf.readFromLocal("labelPrintTail", "");
	}

	/**
	 * 保存标签打印派单序号
	 *
	 * @param printDaySeq
	 */
	public static void saveLabelPrintDaySeq(boolean printDaySeq) {
		ManagerConf.saveToLocal("lable_print_day_seq", printDaySeq ? ENABLE : DISABLE);
	}

	/**
	 * 获取标签打印派单序号
	 *
	 * @return
	 */
	public static boolean getLabelPrintDaySeq() {
		return ManagerConf.readFromLocal("lable_print_day_seq", "0").equals(ENABLE);
	}


	/**
	 * 自助是否支持会员消费
	 * @return
	 */
	public static boolean getHysUseCustomer() {
		return ManagerConf.readFromLocal("hysUseCustomer", "0").equals(ENABLE);
	}

	public static void saveHysUseCustomer(boolean useDelivery) {
		ManagerConf.saveToLocal("hysUseCustomer", useDelivery ? ENABLE : DISABLE);
	}

     /**
	 * 点餐机根据云端厨打设置出单
	 * @return
	 */
	public static boolean getPrintBasedClound() {
		return ManagerConf.readFromLocal("hysPrintBasedClound", "0").equals(ENABLE);
	}

	public static void savePrintBasedClound(boolean useClound) {
		ManagerConf.saveToLocal("hysPrintBasedClound", useClound ? ENABLE : DISABLE);
	}

    /**
     * 盘点模式
     * @param checkMode
     */
    public static final void saveCheckMode(int checkMode) {
        ManagerConf.saveToLocal("checkMode", checkMode + "");
    }

    /**
     * 非餐饮版本使用厨打
     */
    public static final void saveRetailUseKitchen(boolean retailUseKitchen) {
        ManagerConf.saveToLocal("retailUseKitchen", retailUseKitchen ? ENABLE : DISABLE);
    }

    public static final boolean getRetailUseKitchen() {
        return ManagerConf.readFromLocal("retailUseKitchen", "0").equals(ENABLE);
    }

    /**
     * 是否连锁店
     * @param isChildStore
     */
    public static final void saveIsChildStore(boolean isChildStore) {
        ManagerConf.saveToLocal("isChildStore", isChildStore ? ENABLE : DISABLE);
    }

    public static final boolean getIsChildStore() {
        return ManagerConf.readFromLocal("isChildStore", "0").equals(ENABLE);
    }

	/**
	 * 自助是否支持新加坡Nets支付
	 * @return
	 */
	public static boolean getHysNetsPay() {
		return ManagerConf.readFromLocal("hysNetsPay", "0").equals(ENABLE);
	}

	public static void saveHysNetsPay(boolean useNetsPay) {
		ManagerConf.saveToLocal("hysNetsPay", useNetsPay ? ENABLE : DISABLE);
	}

	/**
	 * 自助是否支持美食卡消费
	 * @return
	 */
	public static boolean getHysUseFoodCard() {
		return ManagerConf.readFromLocal("hysUseFoodCard", "0").equals(ENABLE);
	}

	public static void saveHysUseFoodCard(boolean useFoodCard) {
		ManagerConf.saveToLocal("hysUseFoodCard", useFoodCard ? ENABLE : DISABLE);
	}


    /**
     * 副屏在销售时候是否播放广告
      */
    public static void saveSecondDspPlayAD(boolean play) {
        ManagerConf.saveToLocal("SecondDspPlayAD", play ? ENABLE : DISABLE);
    }

    public static boolean getSecondDspPlayAD() {
        return Integer.parseInt(ManagerConf.readFromLocal("SecondDspPlayAD", "0")) == 1;
    }

    public static boolean getSecondDspUseVideo() {
        return Integer.parseInt(ManagerConf.readFromLocal("SecondDsp_use_video", "0")) == 1;
    }

    // 副屏是否播放视频
    public static void saveSecondDspUseVideo(boolean use) {
        ManagerConf.saveToLocal("SecondDsp_use_video", use ? ENABLE : DISABLE);
    }

    public static boolean getSecondDspUsePicture() {
        return Integer.parseInt(ManagerConf.readFromLocal("SecondDsp_use_picture", "0")) == 1;
    }

    // 副屏是否播放图片
    public static void saveSecondDspUsePicture(boolean use) {
        ManagerConf.saveToLocal("SecondDsp_use_picture", use ? ENABLE : DISABLE);
    }

    public static boolean getSecondDspUseAudio() {
        return Integer.parseInt(ManagerConf.readFromLocal("SecondDsp_use_audio", "0")) == 1;
    }

    // 副屏是否播放音乐
    public static void saveSecondDspUseAudio(boolean use) {
        ManagerConf.saveToLocal("SecondDsp_use_audio", use ? ENABLE : DISABLE);
    }

    /**
     * 副屏设置
     */
    public static int getSecondDspFrushTime() {
        return Integer.parseInt(ManagerConf.readFromLocal("SecondDsp_frush_time", "0"));
    }

    public static void saveSecondDspFrushTime(int frushTime) {
        ManagerConf.saveToLocal("SecondDsp_frush_time", frushTime + "");
    }

    /**
     * 新增商品打印标签
     */
    public static boolean getProductAddPrintLabel() {
        return Integer.parseInt(ManagerConf.readFromLocal("ProductAddPrintLabel", Constance.DISABLE + "")) == Constance.ENABLE;
    }

    public static void saveProductAddPrintLabel(boolean enable) {
        ManagerConf.saveToLocal("ProductAddPrintLabel", enable ? Constance.ENABLE_STR : Constance.DISABLE_STR);
    }

    /**
     * 目标dpi
     */
    public static int getTargetDensityDpi() {
        return Integer.parseInt(ManagerConf.readFromLocal("targetDensityDpi", "-1"));
    }

    public static void saveTargetDensityDpi(int targetDensityDpi) {
        ManagerConf.saveToLocal("targetDensityDpi", targetDensityDpi + "");
    }

	/**
	 * 自助点餐微光扫码默认串口
	 * @param hysStartNum
	 */
	public static final void saveHysStartPort(int hysStartNum) {
		ManagerConf.saveToLocal("hysStartPort", hysStartNum + "");
	}

	public static final int getHysStartPort() {
		return Integer.parseInt(ManagerConf.readFromLocal("hysStartPort", "0"));
	}

	/**
	 * 自助是否支持新加坡Nets信用卡支付
	 * @return
	 */
	public static boolean getHysNetsCreditPay() {
		return ManagerConf.readFromLocal("hysNetsCreditPay", "0").equals(ENABLE);
	}

	public static void saveHysNetsCreditPay(boolean useNetsCreditPay) {
		ManagerConf.saveToLocal("hysNetsCreditPay", useNetsCreditPay ? ENABLE : DISABLE);
	}

	public static String getAutoLoginJobNumber(){
		return ManagerConf.readFromLocal("autoLoginJobNumber");
	}

	public static void saveAutoLoginJobNumber(String jobNumber){
		ManagerConf.saveToLocal("autoLoginJobNumber",jobNumber);
	}


}
