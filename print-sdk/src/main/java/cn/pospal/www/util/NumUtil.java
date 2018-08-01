package cn.pospal.www.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.UUID;

/**
 * 数字相关功能 生成随机数等等
 * 
 * @author Near Chan 2012.12.27
 */
public class NumUtil {
	public static final BigDecimal BigDecimal_100 = new BigDecimal(100);
	public static final BigDecimal BigDecimal_00001 = new BigDecimal("0.00001");
	public static final BigDecimal BigDecimal_001 = new BigDecimal("0.01");
	public static final BigDecimal BigDecimal_N1 = new BigDecimal(-1);
	public static final BigDecimal BigDecimal_1000 = new BigDecimal(1000);
	public static final BigDecimal BigDecimal_998 = new BigDecimal(998);
	public static final BigDecimal BigDecimal_999 = new BigDecimal(999);

	public static long createUID() {
		Random random = new Random();
		int num = random.nextInt(1000000);
		DecimalFormat format = new DecimalFormat("000000");
		return Long.parseLong(System.currentTimeMillis() + "" + format.format(num));
	}
	public static long generateUid(String string) {
		long uid = 0;

		if (string == null || string.trim().equals("")) {
			return 0;
		}

		String stringMd5 = PasswordUtil.encryptToMd5String(string).substring(0, 15);

		int length = stringMd5.length();
		int j = length - 1;
		for (int i = 0; i < length && j >= 0; i++, j--) {
			long character = stringMd5.toLowerCase().charAt(i);
			if (character >= '0' && character <= '9') {
				character -= '0';
			} else if (character >= 'a' && character <= 'z') {
				character -= ('a' - 10);
			} else {
				return 0;
			}

			uid += character * parse16To10(j);
		}

		uid = Math.abs(uid);

		return uid;
	}
	
	public static long parse16To10(int n) {
		if (n == 0) {
			return 1;
		} else if (n == 1) {
			return 16;
		} else {
			n--;
			return 16 * parse16To10(n);
		}
	}
	
	public static boolean isInteger(BigDecimal val) {
		int intValue = val.intValue();
		
		return val.compareTo(new BigDecimal(intValue)) == 0;
	}
	
	public static BigDecimal getSmallPart(BigDecimal val) {
		int intValue = val.intValue();
		
		return val.subtract(new BigDecimal(intValue));
	}
	public static String generateLaklaNo() {
		String lakalaNo = UUID.randomUUID().toString();
		lakalaNo = lakalaNo.replace("-", "");
		lakalaNo = lakalaNo.substring(0, 20);
		
		return lakalaNo;
	}

	/**
	 * 使用java正则表达式去掉多余的.与0
	 * @param s
	 * @return
	 */
	public static String subZeroAndDot(String s){
		if(s.indexOf(".") > 0){
			s = s.replaceAll("0+?$", "");//去掉多余的0
			s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
		}
		return s;
	}

	public static String dcm2String(BigDecimal decimal, String defaultVale, int scale) {
		if(decimal == null) {
			return defaultVale;
		}

		String dcmStr = decimal.setScale(scale, RoundingMode.HALF_UP).toPlainString();
		return subZeroAndDot(dcmStr);
	}
	public static int scaleDigitType = 2;		                // 小数点
	public static String dcm2String(BigDecimal decimal, String defaultVale) {
		return dcm2String(decimal, defaultVale,scaleDigitType);
	}

	public static String dcm2String(BigDecimal decimal) {
		return dcm2String(decimal, "0");
	}

    /**
     * Decimal转成金融字符串（5位小数）
     * @param decimal
     * @return
     */
	public static String dcm2FinancialString(BigDecimal decimal) {
        if(decimal == null) {
            return "0";
        } else if(isInteger(decimal)) {
            return "" + decimal.intValue();
        }

        return decimal.setScale(5, RoundingMode.HALF_UP).toPlainString();
	}

    /**
     * Decimal转成数据库字符串（2位小数）
     * @param decimal
     * @return
     */
	public static String dcm2DatebaseString(BigDecimal decimal) {
        return dcm2String(decimal, "0", 2);
	}
	
	public static BigDecimal str2Decimal(String str, BigDecimal defaultVale) {
		if(str == null || str.equals("")) {
			return defaultVale;
		} else {
			BigDecimal value = defaultVale;
			try {
				value = new BigDecimal(str);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return value;
		}
	}
	
	public static BigDecimal str2Decimal(String str) {
		return str2Decimal(str, BigDecimal.ZERO);
	}


    private static final String[] NUMBER_STRS = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "00", "."};
    /**
     * 是否是数字输入
     * @param string
     * @return
     */
    public static final boolean isNumStr(String string) {
        if (string != null && !string.equals("")) {
            for (int i = 0; i < NUMBER_STRS.length; i++) {
                if (string.equals(NUMBER_STRS[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 除法
     * @param num1      被除数
     * @param num2      除数
     * @return
     */
    public static final BigDecimal financialDivide(BigDecimal num1, BigDecimal num2) {
        if (num1 == null || num2 == null || num2.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return num1.divide(num2, 9, BigDecimal.ROUND_HALF_UP);
    }

    public static final BigDecimal intPart(BigDecimal number) {
        if (number == null) {
            return BigDecimal.ZERO;
        }

        return number.setScale(0, BigDecimal.ROUND_DOWN);
    }

	public static final boolean isNullOrZero(BigDecimal num) {
		return num == null || num.compareTo(BigDecimal.ZERO) == 0;
	}

	public static final int string2Int(String originalStr) {
		if (originalStr == null) {
			return 0;
		}

		try {
			return Integer.parseInt(originalStr);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		return 0;
	}

    public static boolean isNotNull(BigDecimal num) {
        if (num != null && num.compareTo(BigDecimal.ZERO) != 0) {
            return true;
        }
        return false;
    }

	/**
	 * 返回随机字符串，同时包含数字、大小写字母
	 * @param len 字符串长度，不能小于3
	 * @return String 随机字符串
	 */
	public static String randomStr(int len){
		if(len < 3){
			throw new IllegalArgumentException("字符串长度不能小于3");
		}
		//数组，用于存放随机字符
		char[] chArr = new char[len];
		//为了保证必须包含数字、大小写字母
		chArr[0] = (char)('0' + StdRandom.uniform(0,10));
		chArr[1] = (char)('A' + StdRandom.uniform(0,26));
		chArr[2] = (char)('a' + StdRandom.uniform(0,26));


		char[] codes = { '0','1','2','3','4','5','6','7','8','9',
				'A','B','C','D','E','F','G','H','I','J',
				'K','L','M','N','O','P','Q','R','S','T',
				'U','V','W','X','Y','Z','a','b','c','d',
				'e','f','g','h','i','j','k','l','m','n',
				'o','p','q','r','s','t','u','v','w','x',
				'y','z'};
		//charArr[3..len-1]随机生成codes中的字符
		for(int i = 3; i < len; i++){
			chArr[i] = codes[StdRandom.uniform(0,codes.length)];
		}

		//将数组chArr随机排序
		for(int i = 0; i < len; i++){
			int r = i + StdRandom.uniform(len - i);
			char temp = chArr[i];
			chArr[i] = chArr[r];
			chArr[r] = temp;
		}

		return new String(chArr);
	}

}
