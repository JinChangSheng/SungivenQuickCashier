
package cn.pospal.www.hardware.printer.oject;

import android.text.TextUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import cn.pospal.www.app.AppConfig;
import cn.pospal.www.debug.D;
import cn.pospal.www.hardware.printer.AbstractPrinter;
import cn.pospal.www.hardware.printer.PrintUtil;
import cn.pospal.www.posbase.R;
import cn.pospal.www.util.StringUtil;

/**
 * 收据打印任务
 * @author NearSOC
 * 2013.02.18
 */
public class ReceiptJob extends PrintJob{
	
	public static final int TYPE_SHOW = 0;		// 销售中列表的商品
	public static final int TYPE_DEL = 1;		// 收据删除的商品
	public static final int TYPE_NET = 2;		// 网络订单
	public static final int TYPE_REPRINT = 3;	// 重新打印
    public static final int TYPE_NET_PRE = 4;	// 网络订单提前打印厨打（已经付款）
    public static final int TYPE_NET_AFTER = 5;	// 网络订单之后打印收据
    public static final int TYPE_REVERSE = 6;	// 反结账
    public static final String catgoryStartStr = ">>";

	// 反馈
	public static final String FEEDBACK_URL = "http://pospal.cn/l?a=##ACCOUNT##&sn=##SN##";
	
	private int productNameLen = 13;
	private int lenMoney = 6;
	private int lenQty = 5;

	private int type = TYPE_SHOW;
	
	private String template;
	private int productNameSpaceLenth = 0;

    private String kitchenName = "";
	private PrintUtil printUtil;
	@SuppressWarnings("unchecked")
	public ReceiptJob(Object data, int type) {
		super();
		this.type = type;
	}
    public ReceiptJob( Object data, int type, String kitchenName) {
		super();
		this.type = type;
		this.kitchenName = kitchenName;
	}

    /**
     * 初始化打印
     */
    private void initPrint() {
        D.out("DDDDDD maxLineLen:" + maxLineLen);
        template = new String(maxLineLen == 32 ?
	        AppConfig.receiptTemplate58 : AppConfig.receiptTemplate80);

        productNameSpaceLenth = 0;

        if(template.contains("#item")) {
            int indexOfProductName = template.indexOf("#{商品名称}");
            int endOfProductName = indexOfProductName + "#{商品名称}".length();
            int length = template.length();
            for (int i = endOfProductName; i < length; i++) {
                char c = template.charAt(i);
                if(c == ' ') {
                    productNameSpaceLenth++;
                } else {
                    break;
                }
            }

            if(maxLineLen == 32) {
                productNameLen = 12;
            } else if(maxLineLen == 42) {
                productNameLen = 13;
            } else if(maxLineLen == 48) {
                productNameLen = 15;
            }
        } else {
            if(maxLineLen == 32) {
                productNameLen = 12;
                productNameSpaceLenth = 2;
            } else if(maxLineLen == 42) {
                productNameLen = 13;
                productNameSpaceLenth = 5;
            } else if(maxLineLen == 48) {
                productNameLen = 15;
                productNameSpaceLenth = 7;
            }
        }
    }

    /**
     * 生成商品信息
     * @param orignalPriceStr
     * @param itemAmountStr
     * @param qtyStr
     * @param productName
     * @param barcode
     * @param dstPriceStr
     * @param remarkStr
     */
    private void createProductInfo(StringBuilder itemStrBuffs,
                                   String orignalPriceStr, String itemAmountStr, String qtyStr,
                                   String productName, String barcode, String dstPriceStr,
                                   String remarkStr, String guiderName, String taxFeeStr,
                                   String discountStr, String priceWithoutTaxStr, String productUnit,
                                   String originalItemAmountStr, String catgoryStr) {
        if (barcode == null) {
            barcode = "";
        }
        String itemStr = itemTemplateStr
                .replace("#{备注}\n", "")
                .replace("#{备注}", "")
                .replace("#{条码}", barcode)
                .replace("#{单价}", StringUtil.flushRight(' ', lenMoney, orignalPriceStr, printer))
                .replace("#{数量}", StringUtil.flushRight(' ', lenQty, qtyStr, printer))
                .replace("#{小计}", StringUtil.flushRight(' ', lenMoney, itemAmountStr, printer))
                .replace("#{折后价}", StringUtil.flushRight(' ', lenMoney, dstPriceStr, printer))
		        .replace("#{导购员}", guiderName == null ? getResourceString(R.string.null_str) : guiderName)
                .replace("#{单位}", productUnit)
                .replace("#{税费}", StringUtil.flushRight(' ', lenMoney, taxFeeStr, printer))
                .replace("#{折扣}", StringUtil.flushRight(' ', lenMoney, discountStr, printer))
                .replace("#{税后单价}", StringUtil.flushRight(' ', lenMoney, priceWithoutTaxStr, printer))
                .replace("#{商品原价小计}", StringUtil.flushRight(' ', lenMoney, originalItemAmountStr, printer));
        if (TextUtils.isEmpty(catgoryStr)) {
            itemStr = itemStr.trim().replace("#{品类}\n", "") + "\n";
        } else {
            itemStr = itemStr.replace("#{品类}", catgoryStr);
        }
        int nameLen = StringUtil.caculateStrLength(productName, printer);
        // 商品名称可以起新行
        boolean needNewLine = itemStr.contains("#{商品名称}\n");
        if(nameLen > productNameLen) {
            if(nameLen > productNameLen + productNameSpaceLenth) {
                if(itemStr.contains(printer.DHDW_STR)
                        && itemStr.contains(printer.CLR_HW_STR)) {
                    itemStr = itemStr.replace("#{商品名称}", productName);
                } else {
                    D.out("KKKKKKK needNewLine = " + needNewLine);
                    if(needNewLine) {
                        itemStrBuffs.append(productName + printer.LF);
                        itemStr = itemStr.replace("#{商品名称}\n", "");
                    } else {
                        // 重新计算剩余空间（名称后面如果跟随金额或者数量很可能前面还有空间）
                        int indexOfProductName = itemStr.indexOf("#{商品名称}");
                        int endOfProductName = indexOfProductName + "#{商品名称}".length();
                        int length = itemStr.length();
                        int useSpace = 0;
                        for (int i = endOfProductName; i < length; i++) {
                            char c = itemStr.charAt(i);
                            D.out("cccccc = " + c);
                            if(c == ' ') {
                                useSpace++;
                            } else {
                                break;
                            }
                        }
                        D.out("XXXXXX useSpace = " + useSpace);
                        if(nameLen > productNameLen + useSpace) {
                            StringBuffer spaceBuf = new StringBuffer(16);
                            for (int i = 1; i < useSpace; i++) {
                                spaceBuf.append(' ');
                            }

                            // 如果商品详情里面还有换行则先打印完商品内容再打印换行内容
                            String[] strings = itemStr.split("\n");
                            for (String string : strings) {
                                if(string.contains("#{商品名称}")) {
                                    List<String> nameStrs = printUtil.cutString(productName, productNameLen + useSpace -1);
                                    for (int i = 0; i < nameStrs.size(); i++) {
                                        if(i == 0) {
                                            string = string.replace("#{商品名称}" + spaceBuf, StringUtil.flushLeft(' ', productNameLen + useSpace - 1, nameStrs.get(i), printer));
                                            itemStrBuffs.append(string + printer.LF);
                                            itemStr = "";
                                        } else {
                                            itemStrBuffs.append(
                                                    StringUtil.flushLeft(' ', productNameLen + useSpace - 1, nameStrs.get(i), printer)
                                                            + printer.LF);
                                        }
                                    }
                                } else {
                                    itemStrBuffs.append(string + printer.LF);
                                }
                            }
                        } else {
                            int needSpace = nameLen - productNameLen;
                            StringBuffer spaceBuf = new StringBuffer(16);
                            for (int i = 0; i < needSpace; i++) {
                                spaceBuf.append(' ');
                            }
                            D.out("PPPPPP spaceBuf = " + spaceBuf);
                            itemStr = itemStr.replace("#{商品名称}" + spaceBuf, productName);
                        }
                    }
                }
            } else {
                int needSpace = nameLen - productNameLen;
                StringBuffer spaceBuf = new StringBuffer(16);
                for (int i = 0; i < needSpace; i++) {
                    spaceBuf.append(' ');
                }
                D.out("PPPPPP spaceBuf = " + spaceBuf);
                itemStr = itemStr.replace("#{商品名称}" + spaceBuf, productName);
            }
        } else {
            String replaceNameStr = "#{商品名称}";
            if (needNewLine) {
                replaceNameStr += "\n";
            }
            itemStr = itemStr.replace(replaceNameStr, StringUtil.flushLeft(' ', productNameLen, productName, printer));
        }

        itemStrBuffs.append(itemStr);

        if(remarkStr != null && !remarkStr.equals("")) {
            ArrayList<String> strings = printUtil.cutString(remarkStr, maxLineLen);
            for (String string : strings) {
                itemStrBuffs.append(string + printer.LF);
            }
        }

    }


	private BigDecimal allNum = BigDecimal.ZERO;
	private BigDecimal originalAmount = BigDecimal.ZERO;
	private BigDecimal tagAmount = BigDecimal.ZERO;
	private String itemTemplateStr = null;
    private boolean containCategory = false;  //小票模板包含"#{品类}"
    String lastCtgName = "";
	@Override
	public synchronized ArrayList<String> toPrintStrings(AbstractPrinter printer) {
		D.out("FFFFFF template = " + template);

		return null;
	}

}

