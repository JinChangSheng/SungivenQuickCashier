package cn.pospal.www.app;

import android.content.Context;

import cn.pospal.www.debug.D;
import cn.pospal.www.manager.ManagerData;
import cn.pospal.www.util.StringUtil;
import cn.pospal.www.util.SystemUtil;

import static cn.pospal.www.hardware.printer.Constance.POSPAL_LED;
import static cn.pospal.www.hardware.printer.Constance.POSPAL_PRINTER;
import static cn.pospal.www.hardware.printer.Constance.POSPAL_SCALE;

/**
 * 程序配置
 * Created by Near Chan on 2016/3/12 0012.
 * Copyright © Zhundong Network 2010
 */
public class AppConfig {
	public static final String default80Template = "[[                #{店名}]]\r\n\r\n收银：#{收银员姓名}(#{收银员工号})  导购：#{导购员}\r\n单号：#{单据号}\r\n牌号：#{牌号}  点送方式：#{点送方式}\r\n时间：#{下单时间}\r\n-----------------------------------------------\r\n商品名称          原价   折后价  数量  小计\r\n#item\r\n#{商品名称}  #{单价} #{折后价}  #{数量} #{小计}\r\n#item\r\n-----------------------------------------------\r\n原价：#{总计}                 总数：#{总数}\r\n现价：#{应收}                 支付：#{支付方式}\r\n实收：#{实收}                 找零：#{找零}\r\n-----------------------------------------------\r\n#{会员姓名} #{会员号}\r\n#{余额}               #{积分}\r\n[[                  欢迎光临]]\r\n";
	public static final String default58Template = "[[          #{店名}]]\r\n收银：#{收银员姓名}(#{收银员工号})  导购：#{导购员}\r\n牌号：#{牌号} 点送方式：#{点送方式}\r\n单号：#{单据号}\r\n时间：#{下单时间}\r\n--------------------------------\r\n商品名称      折后价  数量  小计\r\n#item\r\n#{商品名称}  #{折后价} #{数量} #{小计}\r\n#item\r\n--------------------------------\r\n原价：#{总计}      总数：#{总数}\r\n现价：#{应收}      支付：#{支付方式}\r\n实收：#{实收}      找零：#{找零}\r\n--------------------------------\r\n#{会员姓名} #{会员号}\r\n#{余额} #{积分}\r\n[[          欢迎光临]]\r\n";
	public static final String defaultCouponTemplate = "--------------------------------\r\n请妥善保管、及时使用，遗失不补\r\n#{优惠券条码}\r\n优惠券码：#{优惠券码}\r\n如有任何问题请联系门店服务台\r\n优惠券使用规则：\r\n该券只能在有效期内购买商品时使用\r\n该券不可分次使用";

	public static final String table80Template = "\r\n[[                #{店名}]]\r\n\r\n收银员：#{收银员姓名}(#{收银员工号}) \r\n牌号：#{牌号}\r\n打印时间：#{打印时间}\r\n-----------------------------------------------\r\n商品名称          原价   折后价  数量  小计\r\n#item\r\n#{商品名称}  #{单价} #{折后价}  #{数量} #{小计}\r\n#item\r\n-----------------------------------------------\r\n总数：#{总数}                             总价：#{应收}\r\n";
	public static final String table58Template = "\r\n[[          #{店名}]]\r\n\r\n收银员：#{收银员姓名}(#{收银员工号})  \r\n牌号：#{牌号} \r\n 打印时间：#{打印时间}\r\n--------------------------------\r\n商品名称      折后价  数量  小计\r\n#item\r\n#{商品名称}  #{折后价} #{数量} #{小计}\r\n#item\r\n--------------------------------\r\n总数：#{总数}                 总计：#{应收}";

	public static String company = "Pospal";            // 合作方
	public static boolean isPhoneVersion = true;	    // 是否手机版
	public static String language = "zh_CN";            // 语言

	public static boolean checkNetPrinterByCmd;                 // 通过指令检测网口打印机
	public static boolean isW58 = false;                        // 小票机宽度是否58
	public static String receiptTemplate80;                     // 小票80模板
	public static String receiptTemplate58;                     // 小票58模板
	public static int receiptPrintCnt = 1;			            // 小票打印张数
	public static boolean printTicketLogo;		                // 收据打印logo
	public static boolean isNeedPrintBarcode = true;			// 小票是否打印条码
	public static boolean isW58Table = false;                   // 压桌单宽度是否58
	public static int tableReceiptPrintCnt = 1;		            // 压桌单打印张数
	public static boolean receiptFeedback;                      // 小票打印反馈二维码

	public static boolean isW58Kichen = false;                  // 厨打宽度是否58
	public static boolean useNetKitchenPrinter;                 // 厨打使用小票机
	public static boolean kitchenBeep = true;                   // 厨房打印蜂鸣器开关
	public static boolean kitchenPrintCustomer = true;          // 厨房打印是否打印会员
	public static boolean isKitchenHostOneByOne = true;			// 厨房总控是否分开打印
	public static boolean isKitchenOneByOne = true;			    // 厨房打印是否分开打印
	public static boolean isReverseKitchenPrint = false;         // 反结账时是否打印厨打
	public static boolean kitchenPrintPrice = true;             // 厨房打印是否打印价格

	public static int labelPrinterType = ManagerData.TYPE_TSC;  // 标签机类型
	// 标签规格
	public static int labelWidth = 40;
	public static int labelHeight = 30;
	public static int labelGap = 2;
	public static boolean labelPrintBarcode = false;
	public static boolean labelPrintDatetime = false;
	// 标签的上边距、左边距和行距离
	public static int labelTopMargin;
	public static int labelLeftMargin;
	public static int labelTextSpace;
	public static boolean labelPrintShelfLife = false;
	public static boolean labelPrintDeliveryType = false;
	public static boolean labelPrintEndMsg = true;
	public static boolean labelPrintDaySeq = false;

	public static int scanType = ManagerData.SCAN_TYPE_SALE;	// 扫描模式
	public static final int NET_TYPE_WIFI = 0;          // WiFi网络
	public static final int NET_TYPE_3G_MOBILE = 1;     // 3G移动
	public static final int NET_TYPE_3G_UNION_TEL = 2;  // 3G联通/电信
	public static final int NET_TYPE_4G = 3;            // 4G网络
	// 网络类型
	public static int netType = NET_TYPE_WIFI;

    // 打印机、客显、电子秤默认值
    public static String DEFAULT_PRINTER_PORT = POSPAL_PRINTER;
    public static String DEFAULT_LED_PORT = POSPAL_LED;
    public static String DEFAULT_SCALE_PORT = POSPAL_SCALE;

    public static String printerPort = "";      // 打印机串口
    public static String ledPort = "";          // 客显串口
    public static String scalePort = "";        // 电子秤串口

    public static boolean printCheckout;	                    // 打印收据
    public static boolean[] labelPrintContentSettings;	        // 标签打印收据
    public static String labelPrintTail;                        // 标签打印尾注

    /**
     * 修复sp的一些错误
     */
    private static void patch() {
        String hostPortInfo = ManagerData.getHostPortInfo();
        if (StringUtil.isNullOrEmpty(hostPortInfo)) {
            ManagerData.saveHostPortInfo("9315");
        }
    }

	/**
	 * 数据初始化
	 */
	public static void initAllConfig() {
        patch();

		Context context = ManagerApp.getInstance();
		// 加载语言资源
		language = SystemUtil.getLanguage();

		isW58 = ManagerData.getW58();
		D.out("XXXX isW58 = " + isW58);
		if(ManagerData.getKitchenUseType() == ManagerData.KITCHEN_USE_RECEIPT) {
			isW58Kichen = isW58;
		} else {
			isW58Kichen = ManagerData.getW58Kitchen();
		}
        D.out("XXXX isW58Kichen = " + isW58Kichen);
		if(ManagerData.getTableUseType() == ManagerData.TABLE_USE_RECEIPT) {
			isW58Table = isW58;
		} else {
			isW58Table = ManagerData.getW58Table();
		}
        D.out("XXXX isW58Table = " + isW58Table);
		isNeedPrintBarcode = ManagerData.getIsNeedPrintBarcode();
		isKitchenHostOneByOne = ManagerData.getOneByOneKitchenHost();
		isKitchenOneByOne = ManagerData.getOneByOneKitchen();
		receiptPrintCnt = ManagerData.getPrinterNumInfo() + 1;
		isReverseKitchenPrint = ManagerData.getReverseKitchenPrint();
		// 压桌单数量
		tableReceiptPrintCnt = ManagerData.getTablePrinterNumInfo() + 1;

		labelWidth = ManagerData.getLableWidth();
		labelHeight = ManagerData.getLableHeight();
		labelGap = ManagerData.getLabelGap();
		labelPrintBarcode = ManagerData.getLabelPrintBarcode();
		labelPrintDatetime = ManagerData.getLabelPrintDatetime();
		labelPrintShelfLife = ManagerData.getLabelPrintShelfLife();
		labelPrintDeliveryType = ManagerData.getLabelPrintDeliveryType();
		labelPrintEndMsg = ManagerData.getLabelPrintEndMsg();
		labelPrintDaySeq = ManagerData.getLabelPrintDaySeq();

		kitchenBeep = ManagerData.getKitchenBeep();
		// 加载串口打印机、客显和电子秤端口
		AppConfig.printerPort = ManagerData.getSeialPrinterPort();
		AppConfig.ledPort = ManagerData.getSerialLedPort();
		AppConfig.scalePort = ManagerData.getSerialScalePort();
		D.out("XXXXXX scalePort = " + AppConfig.scalePort);
		kitchenPrintPrice = ManagerData.getKitchenPrintPrice();
		checkNetPrinterByCmd = ManagerData.getCheckNetPrinterByCmd();
		useNetKitchenPrinter = ManagerData.getUseNetKitchenPrinter();
		receiptFeedback = ManagerData.getReceiptFeedback();

		netType = ManagerData.getNetType();

		labelPrinterType = ManagerData.getLabelPrintType();
		labelTopMargin = ManagerData.getLableTopMargin();
		labelLeftMargin = ManagerData.getLableLeftMargin();
		labelTextSpace = ManagerData.getLabelTextSpace();
		printTicketLogo = ManagerData.getPrintLogo();
		kitchenPrintCustomer = ManagerData.getKitchenPrintCustomer();


        labelPrintContentSettings = ManagerData.getLabelPrintContentSettings();
        labelPrintTail = ManagerData.getLabelPrintTail();

		scanType = ManagerData.getScanType();
		D.out("scanType = " + scanType);
        printCheckout = ManagerData.getPrintCheckout();

	}

}
