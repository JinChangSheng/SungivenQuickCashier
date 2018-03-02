package cn.pospal.www.util.jsonParserUtils;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * 设备信息工具类
 */
public class StringUtil {

    /**
     * 大图地址
     */
    public static String transformBIG(String url) {
        return url.substring(0, url.lastIndexOf(".")) + ".big"
                + url.substring(url.lastIndexOf("."), url.length());
    }

    /**
     * StringUtil.isEmpty(null) = true StringUtil.isEmpty("") = true
     * StringUtil.isEmpty(" ") = false StringUtil.isEmpty("gaohang") = false
     * StringUtil.isEmpty("  gaohang  ") = false
     */
    // 是否为空
    public static boolean isEmpty(String str) {
        return (str == null || str.length() == 0);
    }


    /**
     * 验证身份证号是否符合规则
     *
     * @param text 身份证号
     * @return
     */
    public static boolean personIdValidation(String text) {
        text = text.toUpperCase();
        // String regx = "[0-9]{17}x";
        // String reg1 = "[0-9]{15}";
        // String regex = "[0-9]{18}";
        String re = "(\\d{14}[0-9X])|(\\d{17}[0-9X])";
        return text.matches(re) /*
                                 * || text.matches(regx) || text.matches(reg1)
								 * || text.matches(regex)
								 */;
    }

    /**
     * 验证人名是否符合规则
     *
     * @param text 身份证号
     * @return
     */
    public static boolean personNameValidation(String text) {
        text = text.toUpperCase();
        // String regx = "[0-9]{17}x";
        // String reg1 = "[0-9]{15}";
        // String regex = "[0-9]{18}";
        // String re = "^([\u4e00-\u9fa5]{1,20}|[a-zA-Z\.\s]{1,20})$";
        String re = "[\u4E00-\u9FA5]{2,11}(?:·[\u4E00-\u9FA5]{2,11})*";
        return text.matches(re) /*
                                 * || text.matches(regx) || text.matches(reg1)
								 * || text.matches(regex)
								 */;
    }

    /**
     * StringUtil.isBlank(null) = true StringUtil.isBlank("") = true
     * StringUtil.isBlank(" ") = true StringUtil.isBlank("gaohang") = false
     * StringUtil.isBlank("  gaohang  ") = false
     */
    // 是否为空白
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * StringUtil.isNotEmpty(null) = false StringUtil.isNotEmpty("") = false
     * StringUtil.isNotEmpty(" ") = true StringUtil.isNotEmpty("gaohang") = true
     * StringUtil.isNotEmpty("  gaohang  ") = true
     */
    // 是否不为空
    public static boolean isNotEmpty(String str) {
        return (str != null && str.length() > 0);
    }

    /**
     * StringUtil.isNotBlank(null) = false StringUtil.isNotBlank("") = false
     * StringUtil.isNotBlank(" ") = false StringUtil.isNotBlank("gaohang") =
     * true StringUtil.isNotBlank("  gaohang  ") = true
     */
    // 是否不为空白
    public static boolean isNotBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return false;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return true;
            }
        }
        return false;
    }

    /**
     * StringUtil.isNumeric(null) = false StringUtil.isNumeric("") = false
     * StringUtil.isNumeric("  ") = false StringUtil.isNumeric("1234567890") =
     * true StringUtil.isNumeric("12 3") = false StringUtil.isNumeric("ab2c") =
     * false StringUtil.isNumeric("12-3") = false StringUtil.isNumeric("12.3") =
     * false
     */
    // 是否为数字
    public static boolean isNumeric(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isDigit(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    // 分割字符串
    public static String[] split(String src, String separator) {
        if (src == null || separator == null) {
            return null;
        }
        if (src.length() == 0 || separator.length() == 0) {
            String[] s = {src};
            return s;
        }
        java.util.Vector<String> v = new java.util.Vector<String>();
        int start = 0, end = 0;
        while ((end = src.indexOf(separator, start)) != -1) {
            v.addElement(src.substring(start, end));
            start = end + separator.length();
        }
        v.addElement(src.substring(start));
        int ilen = v.size();
        if (ilen < 1) {
            return null;
        }
        String[] arr = new String[ilen];
        for (int i = 0; i < ilen; i++) {
            arr[i] = v.elementAt(i).toString();
        }
        return arr;
    }

    // 获取url最后一节
    public static String getLastPathComponent(String url) {
        if (url == null)
            return "";
        return url.split("/")[url.split("/").length - 1];
    }

    // 是否是子字符串
    public static boolean isSubString(String str, String subStr) {
        return str.indexOf(subStr) != -1;
    }

    public static void appendString(StringBuffer buff, String str) {
        if (isBlank(str))
            str = "";
        buff.append(str);
    }

    // 字符串拼接
    public static String appendString(String str1, String str2) {
        if (isBlank(str1))
            str1 = "";
        StringBuffer strBuf = new StringBuffer(str1);
        appendString(strBuf, str2);
        return strBuf.toString();
    }

    public static boolean isNotBlank(Object object) {
        return !isBlank(object);
    }

    public static boolean isNull(Object object) {
        return object == null;
    }

    public static boolean isNotNull(Object object) {
        return !isNull(object);
    }

    public static boolean isBlank(Object object) {
        if (isNull(object))
            return true;
        if (object instanceof JSONArray) {
            JSONArray obj = (JSONArray) object;
            if (obj.length() == 0)
                return true;
        }
        if (object instanceof JSONObject) {
            JSONObject obj = (JSONObject) object;
            if (obj.length() == 0)
                return true;
        }
        if (object instanceof List) {
            List<?> obj = (List<?>) object;
            if (obj.size() == 0)
                return true;
        }
        if (object instanceof Map) {
            Map<?, ?> obj = (Map<?, ?>) object;
            if (obj.size() == 0)
                return true;
        }
        return isNull(object);
    }

    public static SpannableString formatText(int left, int right) {
        String num = left + "/" + right;
        SpannableString ssb = new SpannableString(num);
        int index = num.indexOf("/");
        ssb.setSpan(new AbsoluteSizeSpan(40, true), 0, index,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#ff7800")), 0,
                index, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new AbsoluteSizeSpan(18, true), index, num.length(),
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#fefefe")),
                index, num.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return ssb;
    }

    /**
     * int 数组转换成 每个之间加","
     *
     * @param uids
     * @return
     */
    public static String getIntArrayString(int[] uids) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < uids.length; i++) {
            if (i < uids.length - 1) {
                sb.append(uids[i]).append(",");
            } else {
                sb.append(uids[i]);
            }
        }
        return sb.toString();
    }


    //String QJstr = "全角转半角ＤＡＯ";
    public static final String SBCchange(String QJstr) {
        String outStr = "";
        String Tstr = "";
        byte[] b = null;
        for (int i = 0; i < QJstr.length(); i++) {
            try {
                Tstr = QJstr.substring(i, i + 1);
                b = Tstr.getBytes("unicode");
            } catch (java.io.UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (b[3] == -1) {
                b[2] = (byte) (b[2] + 32);
                b[3] = 0;
                try {
                    outStr = outStr + new String(b, "unicode");
                } catch (java.io.UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else outStr = outStr + Tstr;
        }
        return outStr;
    }

    // 半角转全角
    //String QJstr = "hello";
    public static final String BQchange(String QJstr) {
        String outStr = "";
        String Tstr = "";
        byte[] b = null;
        for (int i = 0; i < QJstr.length(); i++) {
            try {
                Tstr = QJstr.substring(i, i + 1);
                b = Tstr.getBytes("unicode");
            } catch (java.io.UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (b[3] != -1) {
                b[2] = (byte) (b[2] - 32);
                b[3] = -1;
                try {
                    outStr = outStr + new String(b, "unicode");
                } catch (java.io.UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else
                outStr = outStr + Tstr;
        }
        return outStr;
    }

    // 全角转半角
    //String QJstr1 = "ｈｅｈｅ"
    public static String QBchange(String QJstr) {
        String outStr = "";
        String Tstr = "";
        byte[] b = null;
        for (int i = 0; i < QJstr.length(); i++) {
            try {
                Tstr = QJstr.substring(i, i + 1);
                b = Tstr.getBytes("unicode");
            } catch (java.io.UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (b[3] == -1) {
                b[2] = (byte) (b[2] + 32);
                b[3] = 0;
                try {
                    outStr = outStr + new String(b, "unicode");
                } catch (java.io.UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else
                outStr = outStr + Tstr;
        }
        return outStr;
    }


    // / 转全角的函数(SBC case)
    // /任意字符串
    // /全角字符串
    // /全角空格为12288，半角空格为32
    // /其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
    public static String ToSBC(String input) {
        // 半角转全角：
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 32) {
                c[i] = (char) 12288;
                continue;
            }
            if (c[i] < 127)
                c[i] = (char) (c[i] + 65248);
        }
        return new String(c);
    }

    // / 转半角的函数(DBC case)
    // /任意字符串
    // /半角字符串
    // /全角空格为12288，半角空格为32
    // /其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    //去除标点
    public static String format(String s) {
        String str = s.replaceAll("[·~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|]", "");
        return str.substring(0, 1);
    }

    /**
     * 检查手机中是否存在某个app
     *
     * @param context
     * @param pName
     * @return
     */
    public static boolean checkPackageInstall(Context context, String pName) {
        List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            if (pName.equals(packs.get(i).packageName)) return true;
        }
        return false;
    }

    /**
     * window.location.href="localcall::_0_::    action   ::_2_::   flushHome    ::_1_::   action   ::_2_::   flushAuthorInfo   ::_1_::   action   ::_2_::    closeWebView  ";
     *
     * @param url
     * @return
     */
    public static String[][] urlLocalToStringArray(String url) {
        String[][] retStr = null;
        if (null != url) {
            String[] str = url.split("::_0_::");
            String lastStr = str[str.length - 1];
            String[] param = lastStr.split("::_1_::");
            if (null != param && param.length > 0) {
                int arrayLen = param.length;
                retStr = new String[arrayLen][arrayLen == 1 ? arrayLen + 1 : arrayLen];
                for (int i = 0; i < arrayLen; i++) {
                    String a = param[i];
                    String[] resultPara = a.split("::_2_::");
                    // System.out.println("resultPara[0]"+resultPara[0]+"  resultPara[1]"+resultPara[1]);
                    retStr[i][0] = resultPara[0];
                    retStr[i][1] = resultPara[1];
                }
            }
        }
        return retStr;
    }

    public static String changeFristChar(String strAll, String desChar) {
        strAll = strAll.replace(desChar, String.valueOf(strAll.charAt(0)));
        return strAll;
    }

}
