package cn.pospal.www.manager;

import com.google.gson.Gson;

import cn.pospal.www.app.AppConfig;
import cn.pospal.www.app.Constance;
import cn.pospal.www.debug.D;
import cn.pospal.www.util.GsonUtil;

/**
 * 数据管理器 管理数据存储和读取
 * 
 * @author Near 2012.12.14
 */
public class ManagerData {
	private static final Gson GSON = GsonUtil.getInstance();
	public static final String ENABLE = "1";


	public static boolean getIsNeedPrintBarcode() {
		return ManagerConf.readFromLocal("is_need_print_barcode", "1").equals(ENABLE);
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

	/**
	 * 获取打印Logo
	 * @return
	 */
	public static boolean getPrintLogo() {
		return ManagerConf.readFromLocal("printLogo", "0").equals(ENABLE);
	}

	/**
	 * 获取厨房打印机打印宽度是否58
	 * @return
	 */
	public static boolean getW58Kitchen() {
		return ManagerConf.readFromLocal("w58_kitchen", "0").equals(ENABLE);
	}

	/**
	 * 获取压桌单打印机打印宽度是否58
	 * @return
	 */
	public static boolean getW58Table() {
		return ManagerConf.readFromLocal("w58_table", "0").equals(ENABLE);
	}

    /**
     * 反结账打印厨房小票
     * @return
     */
    public static boolean getReverseKitchenPrint() {
        return ManagerConf.readFromLocal("reverse_kitchen_print", "0").equals(ENABLE);
    }

    /**
     * 打印厨房小票蜂鸣器报警
     * @return
     */
    public static boolean getKitchenBeep() {
        return ManagerConf.readFromLocal("kitchen_beep", "1").equals(ENABLE);
    }


	/**
	 * 获取厨房总控打印机是否一品一单
	 * @return
	 */
	public static boolean getOneByOneKitchenHost() {
		return ManagerConf.readFromLocal("one_by_one_kitchen_0", "0").equals(ENABLE);
	}

	/**
	 * 获取厨房打印机是否一品一单
	 * @return
	 */
	public static boolean getOneByOneKitchen() {
		return ManagerConf.readFromLocal("one_by_one_kitchen", "0").equals(ENABLE);
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
	 * 获取打印机IP信息
	 * 
	 * @return
	 */
	public static String getReceiptPrinterIpInfo() {
		return ManagerConf.readFromLocal("printer_ip_info", "");
	}

	/**
	 * 获取标签打印机IP信息
	 * 
	 * @return
	 */
	public static String getLabelPrinterIpInfo() {
		return ManagerConf.readFromLocal("label_printer_ip_info", "");
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
	 * 获取标签宽度
	 * 
	 * @return
	 */
	public static int getLableHeight() {
		return Integer.parseInt(ManagerConf.readFromLocal("lable_height", "30"));
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
	 * 获取标签上边距
	 *
	 * @return
	 */
	public static int getLableTopMargin() {
		return Integer.parseInt(ManagerConf.readFromLocal("lable_top_margin", "0"));
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
     * 获取标签行距离
     *
     * @return
     */
    public static int getLabelTextSpace() {
        return Integer.parseInt(ManagerConf.readFromLocal("lable_text_space", "28"));
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
     * 获取标签打印时间
     *
     * @return
     */
    public static boolean getLabelPrintDatetime() {
        return ManagerConf.readFromLocal("lable_print_datetime", "0").equals(ENABLE);
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
     * 获取标签打印尾注
     *
     * @return
     */
    public static boolean getLabelPrintDeliveryType() {
        return ManagerConf.readFromLocal("lable_printDeliveryType", "0").equals("1");
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
     * 获取标签协议类型
     *
     * @return
     */
    public static int getLabelPrintType() {
        return Integer.parseInt(ManagerConf.readFromLocal("lable_print_type", "0"));
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
	 * 获取压桌单打印机IP信息
	 * 
	 * @return
	 */
	public static String getTablePrinterIpInfo() {
		return ManagerConf.readFromLocal("table_printer_ip_info3", "");
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
	 * 获取打印机打印次数
	 * 
	 * @return
	 */
	public static int getPrinterNumInfo() {
		return Integer.parseInt(ManagerConf.readFromLocal("printer_num_info", "0"));
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

	public static final String CHECK_YES = "y";
	public static final String CHECK_NO = "n";

	private static final int MAX_BYS_MARKNO = 999;

	public static boolean getBtEnable() {
		return ManagerConf.readFromLocal("bt_enable", "0").equals(ENABLE);
	}

	public static String getBtAddr() {
		return ManagerConf.readFromLocal("bt_addr", "");
	}

	public static boolean getLabelBtEnable() {
		return ManagerConf.readFromLocal("label_bt_enable", "0").equals(ENABLE);
	}

	public static String getLabelBtAddr() {
		return ManagerConf.readFromLocal("label_bt_addr", "");
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

    public static boolean getKitchenPrintPrice() {
        return ManagerConf.readFromLocal("KitchenPrintPrice", "1").equals(ENABLE);
    }

    public static final boolean getCheckNetPrinterByCmd() {
        return Integer.parseInt(ManagerConf.readFromLocal("checkNetPrinterByCmd", "0")) == 1;
    }

    public static final boolean getUseNetKitchenPrinter() {
        return Integer.parseInt(ManagerConf.readFromLocal("useNetKitchenPrinter", "0")) == 1;
    }


    public static final boolean getReceiptFeedback() {
        return Integer.parseInt(ManagerConf.readFromLocal("receiptFeedback", "0")) == 1;
    }
    public static final String DB_INIT_VERSION = "0.00";

	public static boolean getKitchenPrintCustomer() {
		return Integer.parseInt(ManagerConf.readFromLocal("KitchenPrintCustomer", "1")) == 1;
	}


    public static final String getSeialPrinterPort() {
        return ManagerConf.readFromLocal("serialPrinterPort", AppConfig.DEFAULT_PRINTER_PORT);
    }


    public static final String getSerialLedPort() {
        return ManagerConf.readFromLocal("serialLedPort", AppConfig.DEFAULT_LED_PORT);
    }

    public static final String getSerialScalePort() {
        return ManagerConf.readFromLocal("serialScalePort", AppConfig.DEFAULT_SCALE_PORT);
    }


    public static boolean getPrintCheckout() {
        return Integer.parseInt(ManagerConf.readFromLocal("printCheckout", "1")) == Constance.ENABLE;
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


	public static String getLabelPrintTail() {
        return ManagerConf.readFromLocal("labelPrintTail", "");
	}

	/**
	 * 获取标签打印派单序号
	 *
	 * @return
	 */
	public static boolean getLabelPrintDaySeq() {
		return ManagerConf.readFromLocal("lable_print_day_seq", "0").equals(ENABLE);
	}

	public static String getUserName() {
		return ManagerConf.readFromLocal("UserName");
	}

	public static void saveUserName(String userName) {
		ManagerConf.saveToLocal("UserName", userName);
	}

	public static String getUserPassW() {
		return ManagerConf.readFromLocal("UserPassW");
	}

	public static void saveUserPassW(String userPassW) {
		ManagerConf.saveToLocal("UserPassW", userPassW);
	}

	public static String getIp() {
		return ManagerConf.readFromLocal("localIp");
	}

	public static void saveIp(String localIp) {
		ManagerConf.saveToLocal("localIp", localIp);
	}

    public static String getPort() {
		return ManagerConf.readFromLocal("localPort");
	}

	public static void savePort(String localIp) {
		ManagerConf.saveToLocal("localPort", localIp);
	}


}
