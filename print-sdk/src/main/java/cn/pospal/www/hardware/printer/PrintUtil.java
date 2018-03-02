package cn.pospal.www.hardware.printer;

import java.util.ArrayList;

import cn.pospal.www.app.ManagerApp;
import cn.pospal.www.debug.D;
import cn.pospal.www.posbase.R;
import cn.pospal.www.util.CharUtil;
import cn.pospal.www.util.StringUtil;

/**
 * 打印工具类
 * 
 * @author pospal
 * 
 */
public class PrintUtil {
	public static final byte[] ALIGN_LEFT = new byte[]{0x1B, 'a', 0x00, 0x48};          // 靠左对齐
	public static final byte[] ALIGN_CENTER = new byte[]{0x1B, 'a', 0x01, 0x49};        // 居中对齐

	private AbstractPrinter printer;
	private int maxLineLength = 32; // 默认最大行宽32英文字符

	public PrintUtil(AbstractPrinter printer) {
		this.printer = printer;
		maxLineLength = printer.lineWidth;
	}

	/**
	 * 打印小票头
	 */
	public ArrayList<String> printHeader() {
		return printCenter("");
	}

	/**
	 * 打印小票尾
	 */
	public ArrayList<String> printFeet(boolean needAddress) {
		ArrayList<String> printStrings = new ArrayList<String>();

		if (printStrings.size() > 0) {
			printStrings.add(printLine());
		}
		String welcomeMsg = ManagerApp.getInstance().getString(R.string.welcome);
		// String endMsg = "请当面点清所购商品和找零，并保管好小票以做对账及开发票和退换货凭证。";

		printStrings.addAll(printCenter(welcomeMsg));
		return printStrings;
	}

	/**
	 * 打印线
	 */
	public String printLine() {
		D.out("PrintFun printLine maxLineLength = " + maxLineLength);
		StringBuffer buffer = new StringBuffer(maxLineLength);
		for (int i = 0; i < maxLineLength; i++) {
			buffer.append("-");
		}
		buffer.append(printer.LF);

		return buffer.toString();
	}

	/**
	 * 打印间隔线
	 */
	public String printSapceLine() {
		D.out("PrintFun printLine maxLineLength = " + maxLineLength);
		StringBuffer buffer = new StringBuffer(maxLineLength);
		for (int i = 0; i < maxLineLength; i++) {
			buffer.append(i % 2 == 0 ? "-" : " ");
		}
		buffer.append(printer.LF);

		return buffer.toString();
	}

	/**
	 * 居中打印
	 * 
	 * @param buffer
	 */
	public ArrayList<String> printCenter(String buffer) {
		D.out("PrintFun printCenter maxLineLength = " + maxLineLength);

		int length = StringUtil.caculateStrLength(buffer, printer);

		// 过长直接打印
		if (length > maxLineLength) {
			return spitMaxLenStrings(buffer);
		} else {
			StringBuffer printBuffer = new StringBuffer(maxLineLength);
			int spaceLen = maxLineLength - length;
			for (int i = 0; i < spaceLen / 2; i++) {
				printBuffer.append(" ");
			}
			printBuffer.append(buffer);
			int lastSpaceLen = maxLineLength - StringUtil.caculateStrLength(printBuffer.toString(), printer);
			for (int i = 0; i < lastSpaceLen; i++) {
				printBuffer.append(" ");
			}
			printBuffer.append(printer.LF);

			ArrayList<String> printStrings = new ArrayList<String>();
			printStrings.add(printBuffer.toString());

			return printStrings;
		}
	}

	/**
	 * 倍高倍宽居中打印
	 * 
	 * @param buffer
	 */
	public ArrayList<String> printHWCenter(String buffer) {
		int length = StringUtil.caculateStrLength(buffer, true, printer);
		ArrayList<String> printStrs = new ArrayList<String>();

		// 过长直接打印
		if (length > maxLineLength) {
			printStrs.add(printer.DHDW_STR);
			printStrs.addAll(spitMaxLenStrings(buffer, true));
			printStrs.add(printer.CLR_HW_STR);
			return printStrs;
		} else {
			StringBuffer printBuffer = new StringBuffer(maxLineLength);
			int spaceLen = maxLineLength - length;
			for (int i = 0; i < spaceLen / 2; i++) {
				printBuffer.append(" ");
			}
			printBuffer.append(printer.DHDW_STR);
			printBuffer.append(buffer);
			printBuffer.append(printer.CLR_HW_STR);
			int lastSpaceLen = maxLineLength - (spaceLen / 2 + length);
			for (int i = 0; i < lastSpaceLen; i++) {
				printBuffer.append(" ");
			}
			printBuffer.append(printer.LF);

			ArrayList<String> printStrings = new ArrayList<String>();
			printStrings.add(printBuffer.toString());

			return printStrings;
		}
	}

	/**
	 * 直接打印
	 * 
	 * @param num
	 */
	public ArrayList<String> printHWDirect(int num) {
		ArrayList<String> printStrs = new ArrayList<String>();

		printStrs.add(printer.DHDW_STR);
		printStrs.add(num + "");
		printStrs.add(printer.CLR_HW_STR);
		return printStrs;
	}

	/**
	 * 倍高倍宽
	 * 
	 * @param buffer
	 */
	public ArrayList<String> printHWDirect(String buffer) {
		ArrayList<String> printStrs = new ArrayList<String>();

		printStrs.add(printer.DHDW_STR);
		printStrs.addAll(spitMaxLenStrings(buffer, true));
		printStrs.add(printer.CLR_HW_STR);
		return printStrs;
	}

    /**
     * 倍高
     *
     * @param buffer
     */
    public ArrayList<String> printHDirect(String buffer) {
        ArrayList<String> printStrs = new ArrayList<String>();

        printStrs.add(printer.DH_STR);
        printStrs.addAll(spitMaxLenStrings(buffer, true));
        printStrs.add(printer.CLR_HW_STR);
        return printStrs;
    }

	/**
	 * 居中打印（空白填线）
	 * 
	 * @param buffer
	 */
	public ArrayList<String> printCenterLine(String buffer) {
		ArrayList<String> printStrings = new ArrayList<String>(maxLineLength);
		int length = StringUtil.caculateStrLength(buffer, printer);

		// 过长直接打印
		if (length > maxLineLength) {
			return spitMaxLenStrings(buffer);
		} else {
			StringBuffer printBuffer = new StringBuffer(maxLineLength);
			int spaceLen = maxLineLength - length;
			for (int i = 0; i < spaceLen / 2; i++) {
				printBuffer.append("-");
			}
			printBuffer.append(buffer);
			int lastSpaceLen = maxLineLength - StringUtil.caculateStrLength(printBuffer.toString(), printer);
			for (int i = 0; i < lastSpaceLen; i++) {
				printBuffer.append("-");
			}
			printBuffer.append(printer.LF);

			printStrings.add(printBuffer.toString());
			return printStrings;
		}
	}

    public ArrayList<String> printRight(String str) {
        ArrayList<String> printStrings = new ArrayList<String>();

        int spaceLen = maxLineLength - StringUtil.caculateStrLength(str, printer);
        if (spaceLen > 0) {
            StringBuffer printBuffer = new StringBuffer(maxLineLength);
            for (int i = 0; i < spaceLen; i++) {
                printBuffer.append(" ");
            }
            printBuffer.append(str).append(printer.LF);
            printStrings.add(printBuffer.toString());
        } else {
            printStrings.addAll(spitMaxLenStrings(str));
        }

        return  printStrings;
    }

	/**
	 * 首尾打印
	 * 
	 */
	public ArrayList<String> printStartEnd(String startStr, String endStr) {
		ArrayList<String> printStrings = new ArrayList<String>();

		boolean headerHasPrinted = false;
		// 首部字符串超过最大长度直接打印
		if (StringUtil.caculateStrLength(startStr, printer) > maxLineLength) {
			printStrings.addAll(spitMaxLenStrings(startStr));
			// 首部打印置位
			headerHasPrinted = true;
		}

		// 尾部字符串超过最大长度直接打印
		if (StringUtil.caculateStrLength(endStr, printer) > maxLineLength) {
			if (!headerHasPrinted) {
				printStrings.add(startStr + printer.LF);
			}
            printStrings.addAll(printRight(endStr));
		} else {
			int usedLen = StringUtil.caculateStrLength(startStr, printer);

			// 当头部字符串未打印
			if (!headerHasPrinted) {
				// 剩余空间足够打印，拼接成为一行
				if (maxLineLength - usedLen >= StringUtil.caculateStrLength(endStr, printer)) {
					StringBuffer printBuffer = new StringBuffer(maxLineLength);
					printBuffer.append(startStr);
					int spaceLen = maxLineLength - StringUtil.caculateStrLength(startStr, printer) - StringUtil.caculateStrLength(endStr, printer);
					for (int i = 0; i < spaceLen; i++) {
						printBuffer.append(" ");
					}
					printBuffer.append(endStr).append(printer.LF);

					printStrings.add(printBuffer.toString());
				} else { // 不够另起一行
//					StringBuffer printBuffer = new StringBuffer(maxLineLength);
//					printBuffer.append(startStr);
//
//					int spaceLen = maxLineLength - StringUtil.caculateStrLength(endStr);
//					for (int i = 0; i < spaceLen; i++) {
//						printBuffer.append(" ");
//					}
//					printBuffer.append(endStr).append(LF);
//					printStrings.add(printBuffer.toString());
                    printStrings.add(startStr);
                    printStrings.addAll(printRight(endStr));
				}
			} else { // 当头部字符串已经打印，尾部直接靠后打印
				StringBuffer printBuffer = new StringBuffer(maxLineLength);
				int spaceLen = maxLineLength - StringUtil.caculateStrLength(endStr, printer);
				for (int i = 0; i < spaceLen; i++) {
					printBuffer.append(" ");
				}
				printBuffer.append(endStr).append(printer.LF);
				printStrings.add(printBuffer.toString());

			}
		}

		return printStrings;

	}

	/**
	 * 将字符串切割成满行长度的多条字符串
	 * 
	 * @param spiltString
	 * @return
	 */
	public ArrayList<String> spitMaxLenStrings(String spiltString) {
		return spitMaxLenStrings(spiltString, false);
	}

	/**
	 * 将字符串切割成满行长度的多条字符串
	 * 
	 * @param spiltString
	 * @param isDW 倍宽标志
	 * @return
	 */
	private ArrayList<String> spitMaxLenStrings(String spiltString, boolean isDW) {
		ArrayList<String> spiltStrings = cutString(spiltString, maxLineLength);

        // 新增换行
        for (int i = 0; i < spiltStrings.size(); i++) {
            String string = spiltStrings.get(i) + printer.LF;
            spiltStrings.set(i, string);
        }

		return spiltStrings;
	}

	public ArrayList<String> cutString(String origalStr, int maxLineLength, boolean isDW) {
		ArrayList<String> strs = new ArrayList<String>();

		if (StringUtil.caculateStrLength(origalStr, isDW, printer) <= maxLineLength) {
			strs.add(origalStr);
		} else {
			char[] chars = origalStr.toCharArray();

			StringBuffer buffer = new StringBuffer(maxLineLength);
			for (int i = 0; i < chars.length; i++) {
				if (CharUtil.isChinese(chars[i])) {
					if (StringUtil.caculateStrLength(buffer.toString(), isDW, printer) + (isDW ? 4 : 2) > maxLineLength) {
						strs.add(buffer.toString());
						buffer.delete(0, buffer.length() + 1);
						buffer.append(chars[i]);
					} else {
						buffer.append(chars[i]);
					}
				} else {
					if (StringUtil.caculateStrLength(buffer.toString(), isDW, printer) + (isDW ? 2 : 1) > maxLineLength) {
						strs.add(buffer.toString());
						buffer.delete(0, buffer.length() + 1);
						buffer.append(chars[i]);
					} else {
						buffer.append(chars[i]);
					}
				}

				if (i == chars.length - 1) {
					strs.add(buffer.toString());
				}

			}
		}

		return strs;
	}

    public ArrayList<String> cutString(String origalStr, int maxLineLength) {
        return cutString(origalStr, maxLineLength, false);
    }

}
