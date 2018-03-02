package cn.pospal.www.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextPaint;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pospal.www.debug.D;
import cn.pospal.www.hardware.printer.AbstractPrinter;
import cn.pospal.www.manager.ManagerFile;

/**
 * 字符串工具类 提供相关字符串处理功能
 * 
 * @author Near Chan 2012.10.29
 */
public class StringUtil {
    public static final String STRING_BLANK = "";
	/**
	 * 将InputStream转成String
	 * 
	 * @param is
	 * @return
	 */
	public static String is2String(InputStream is) {
		if (is == null) {
			return null;
		}

		final int MAX_BUF_SIZE = 32 * 1024; // 预设buffer的大小为32K
		StringBuffer readStringBuffer = new StringBuffer(MAX_BUF_SIZE);

		try {
			InputStreamReader inputStreamReader = new InputStreamReader(is, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String data = "";
			while ((data = bufferedReader.readLine()) != null) {
				readStringBuffer.append(data);
			}
		} catch (IOException e) {
			D.out(e);

			return null;
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				D.out(e);
			}
		}
		D.out("DDDDD readStringBuffer = " + readStringBuffer.length());

		return readStringBuffer.toString();
	}

	public static byte[] isGetBytes(InputStream is) {
		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024]; // 用数据装
		int len = -1;
		try {
			while ((len = is.read(buffer)) != -1) {
				outstream.write(buffer, 0, len);
			}
			outstream.close();
		} catch (IOException e) {
			D.out(e);
		}
		// 关闭流一定要记得。
		return outstream.toByteArray();
	}

	/**
	 * 将String转成InputStream
	 * 
	 * @param string
	 * @return
	 */
	public static InputStream string2Is(String string) {
		if (string != null) {
			return new ByteArrayInputStream(string.getBytes());
		}

		return null;
	}

	/**
	 * 判断字符串不为空
	 * 
	 * @param string
	 * @return
	 */
	public static boolean isStringNotNull(String string) {
		if (string != null && !string.equals("") && !string.equalsIgnoreCase("null")) {
			return true;
		}

		return false;
	}

	/**
	 * 从文件中读取流
	 * 
	 * @param fullName
	 *            完整的文件名（包括路径）
	 * @return
	 */
	public static InputStream readIsFromFile(String fullName) {
		File readFile = new File(fullName);
		if (readFile.exists()) {
			try {
				return new FileInputStream(readFile);
			} catch (FileNotFoundException e) {
				D.out(e);
			}
		}
		return null;
	}

	/**
	 * 从文件中读取字符串
	 * 
	 * @param fullName
	 *            完整的文件名（包括路径）
	 * @return
	 */
	public static String readStringFromFile(String fullName) {

		return is2String(readIsFromFile(fullName));
	}

	/**
	 * 将字符串写到SD卡
	 * 
	 * @param string
	 *            字符串
	 * @param fileName
	 *            文件名
	 * @param saveType
	 *            保存类型
	 * @return 写入是否成功
	 */
	public static final int SAVE_TYPE_BAK = 0; // 备份类型，下次可能需要使用
	public static final int SAVE_TYPE_TMP = 1; // 临时文件，退出程序必须清除
	public static final int SAVE_TYPE_CACHE = 2; // 缓存类型
	public static final int SAVE_TYPE_CONF = 3; // 配置类型
	public static final int SAVE_TYPE_DOWNLOAD = 4; // 下载类型

	public static boolean saveString2SDCard(String string, final String fileName, int saveType) {
		String writePath = ManagerFile.POSPAL_ROOT;
		switch (saveType) {
		case SAVE_TYPE_BAK:
			writePath = ManagerFile.POSPAL_BAK_ROOT;
			break;
		case SAVE_TYPE_TMP:
			writePath = ManagerFile.POSPAL_TMP_ROOT;
			break;
		case SAVE_TYPE_CACHE:
			writePath = ManagerFile.POSPAL_CACHE_ROOT;
			break;
		case SAVE_TYPE_CONF:
			writePath = ManagerFile.POSPAL_CONF_ROOT;
			break;

		case SAVE_TYPE_DOWNLOAD:
			writePath = ManagerFile.POSPAL_DOWNLOAD_ROOT;
			break;

		default:
			break;
		}

		String fullFileName = fileName;
		if (!fileName.startsWith(writePath)) {
			fullFileName = writePath + fileName;
		}

		FileOutputStream fos = null;
		try {
			File dir = new File(writePath);
			if (!dir.exists()) {
				if (!dir.mkdirs()) {
					throw new FileNotFoundException("创建文件夹" + writePath + "失败！");
				}
			}
			File writeFile = new File(fullFileName);
			if (!writeFile.exists()) {
				if (!writeFile.createNewFile()) {
					throw new FileNotFoundException("创建文件" + fullFileName + "失败！");
				}
			}
			fos = new FileOutputStream(writeFile);
			fos.write(string.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			D.out(e);
		} catch (IOException e) {
			D.out(e);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					D.out(e);
				}
			}
		}

		File checkFile = new File(fileName);
		if (checkFile.exists()) {
			long lastModifiedTime = checkFile.lastModified();

			if (System.currentTimeMillis() - lastModifiedTime < 100) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 字符串的右对齐输出
	 *
	 * @param c
	 *            填充字符
	 * @param length
	 *            填充后字符串的总长度
	 * @param string
	 *            要格式化的字符串
	 */
	public static String flushRight(char c, int length, String string, AbstractPrinter printer) {
		D.out("DDDDDD flushRight string = " + string + ", length = " + length);
		String str = "";
		String temp = "";
		int printLen = caculateStrLength(string, printer);
		if (printLen > length) {
			str = string;
		} else {
			for (int i = 0; i < length - printLen; i++) {
				temp = temp + c;
			}
		}
		str = temp + string;
		return str;
	}

	/**
	 * 字符串的左对齐输出
	 *
	 * @param c
	 *            填充字符
	 * @param length
	 *            填充后字符串的总长度
	 * @param string
	 *            要格式化的字符串
	 */
	public static String flushLeft(char c, int length, String string, AbstractPrinter printer) {
		D.out("DDDDDD flushLeft string = " + string + ", length = " + length);
		String str = "";
		String temp = "";
		int printLen = caculateStrLength(string, printer);
		if (printLen > length) {
			str = string;
		} else {
			for (int i = 0; i < length - printLen; i++) {
				temp = temp + c;
			}
		}
		str = string + temp;
		return str;
	}

	public static int caculateStrLength(String str, boolean isBig, AbstractPrinter printer) {
		int len = 0;
		int nouseLen = 0;

		if (str != null && str.length() > 0) {
			int startBig = -1;
			int endBig = -1;
			if(!printer.CLR_HW_STR.equals("")
				&&str.contains(printer.DHDW_STR)
				&& str.contains(printer.CLR_HW_STR)) {
				startBig = str.indexOf(printer.DHDW_STR) + printer.DHDW_STR.length();
				endBig = str.indexOf(printer.CLR_HW_STR);
				nouseLen = printer.DHDW_STR.length() + printer.CLR_HW_STR.length();
			}
			if(!printer.CLR_HW_STR.equals("")
				&&str.contains(printer.DH_STR)
				&& str.contains(printer.CLR_HW_STR)) {
				nouseLen = printer.DH_STR.length() + printer.CLR_HW_STR.length();
			}
			char[] chars = str.toCharArray();

			for (int i = 0; i < chars.length; i++) {
				if(startBig != -1 && endBig != -1) {
					if(i >= startBig && i < endBig) {
						isBig = true;
					} else {
						isBig = false;
					}
				}

				if (CharUtil.isChinese(chars[i])) {
					if (isBig) {
						len = len + 4;
					} else {
						len = len + 2;
					}
				} else {
					if (isBig) {
						len = len + 2;
					} else {
						len++;
					}
				}
			}
		}

		return len - nouseLen;
	}

	public static int caculateStrLength(String str, AbstractPrinter printer) {
		return caculateStrLength(str, false, printer);
	}

	public static int caculateStrsLength(AbstractPrinter printer, String... strs) {
        int len = 0;
        for (String str : strs) {
            len += caculateStrLength(str, printer);
        }
        return len;
	}
	
	public static String cutEndString(String oldStr, int cnt) {
		if(oldStr==null){
			return "";
		}
		if(oldStr.length() < cnt) {
			int spaceLen = cnt - oldStr.length();
			StringBuffer buffer = new StringBuffer(10);
			while (spaceLen-- > 0) {
				buffer.append(" ");
			}
			buffer.append(oldStr);
			
			return buffer.toString();
		} else {
			return oldStr.substring(oldStr.length() - cnt, oldStr.length());
		}
	}
	
	/**
	 * 根据实际显示长度切割字符，注意：一个中文宽度=2个英文
	 * @param oldStr
	 * @param realCnt
	 * @return
	 */
	public static String cutRealLenthStr(String oldStr, int realCnt) {
		StringBuilder builder = new StringBuilder(realCnt);
		
		int cnt = 0;
		char[] chars = oldStr.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (CharUtil.isChinese(chars[i])) {
				cnt += 2;
				if(cnt > realCnt) {
					break;
				}
				builder.append(chars[i]);
			} else {
				cnt++;
				if(cnt > realCnt) {
					break;
				}
				builder.append(chars[i]);
			}
			
			if(cnt >= realCnt) {
				break;
			}
		}
		
		return builder.toString();
	}
	
	/**
	 * 返回限定字节长度的客显文字
	 * 客显一个中文=两个英文
	 * @return
	 */
	public static String cutDisplayerString(String str, int cnt) {
		StringBuilder sb = new StringBuilder(cnt);


		char[] chars = str.toCharArray();
		int len = 0;
		for (char c : chars) {
			if(CharUtil.isChinese(c)) {
				len += 2;
			} else {
				len += 1;
			}
		}

		if(len <= cnt) {
			int spaceLen = cnt - len;
			if(spaceLen > 0) {
				for(int i = 0; i < spaceLen; i++) {
					sb.append(' ');
				}
				return sb.append(str).toString();
			}
			return str;
		}

		len = 0;
		for (char c : chars) {
			if(CharUtil.isChinese(c)) {
				len += 2;
			} else {
				len += 1;
			}
			if(len > cnt) {
				break;
			}
			
			sb.append(c);
		}
		
		return sb.toString();
	}
	
	/**
	 * 按照长度截断字符串，返回数组
	 * @param oldStr		要处理的字符串
	 * @param targetCnt		目标长度
	 * @return				字符List
	 */
	public static List<String> cutStringByLength(String oldStr, int targetCnt, AbstractPrinter printer) {
		List<String> splitStrs = new ArrayList<String>();
		if(caculateStrLength(oldStr, printer) < targetCnt) {
			splitStrs.add(oldStr);
		} else {
			int startBig = -1;
			int endBig = -1;
			startBig = oldStr.indexOf(printer.DHDW_STR) + printer.DHDW_STR.length();
			endBig = oldStr.indexOf(printer.CLR_HW_STR);
			StringBuilder builder = new StringBuilder(targetCnt);
			int cnt = 0;
			boolean isBig = false;
			char[] chars = oldStr.toCharArray();
			for (int i = 0; i < chars.length; i++) {
				if(startBig != -1 && endBig != -1) {
					if(i >= startBig && i < endBig) {
						isBig = true;
					} else {
						isBig = false;
					}
				}
				if (CharUtil.isChinese(chars[i])) {
					if (isBig) {
						cnt += 4;
					} else {
						cnt += 2;
					}
					if(cnt > targetCnt) {
						splitStrs.add(builder.toString());
						builder.delete(0, builder.length());
						cnt = 0;
					}
					builder.append(chars[i]);
				} else {
					if (isBig) {
						cnt += 2;
					} else {
						cnt += 1;
					}
					if(cnt > targetCnt) {
						splitStrs.add(builder.toString());
						builder.delete(0, builder.length());
						cnt = 0;
					}
					builder.append(chars[i]);
				}
				
				if(cnt >= targetCnt) {
					splitStrs.add(builder.toString());
					builder.delete(0, builder.length());
					cnt = 0;
				}
			}
		}
		
		return splitStrs;
	}

    public static char bitsToHex(int bit) {
        if(bit >= 0 && bit <= 9) {
            return (char)((int)'0'+bit);
        } else if(bit >= 10 && bit <= 15) {
            return (char)((int)'A'+bit-10);
        }
        return '-';
    }

    public static String bytesToHex(byte[] bs) {
        if(bs == null)
            return "null";

        StringBuilder sb = new StringBuilder();
        for(byte b : bs ) {
            sb.append(bitsToHex((b>>4)&0x0F));
            sb.append(bitsToHex(b&0x0F));
            sb.append(" ");
        }
        return sb.toString();
    }

    public static String bytesToHex(byte[] bs, int start, int count) {
        if(bs == null)
            return "null";

        StringBuilder sb = new StringBuilder();
        for(int i=start; i<count; i++) {
            final byte b = bs[i];
            sb.append(bitsToHex((b>>4)&0x0F));
            sb.append(bitsToHex(b&0x0F));
            sb.append(" ");
        }

        if(sb.length() > 0)
            return sb.toString();

        return "null";
    }

    public static int valueFromHex(char hex) throws Exception {
        if(hex >= '0' && hex <= '9')
            return (int) (hex-'0');
        if(hex >= 'a' && hex <= 'f')
            return (int) (hex-'a'+10);
        if(hex >= 'A' && hex <= 'F')
            return (int) (hex-'A'+10);
        throw new Exception("failed to convert hex.");
    }

    public static byte[] bytesFromHex(String str, int maxSize) throws Throwable {
        ByteBuffer bb = ByteBuffer.allocate(maxSize);
        // fix : order bug
        bb.order(ByteOrder.LITTLE_ENDIAN);

        char[] src = str.toCharArray();
        //mLogger.addLog(Utils.bytesToHex(src));

        for(int i=0; i<src.length; i++) {
            if(src[i] == 0x20)
                continue;
            if(i+1 < src.length) {
                int hi = valueFromHex(src[i]);
                int lo = valueFromHex(src[i+1]);
                bb.put((byte) (hi*16+lo));
                i++;
            } else {
                throw new Exception("failed to convert hex string.");
            }
        }

        if(bb.hasArray())
            return bb.array();
        return null;
    }

    public static Bitmap strings2Bitmap(List<String> strings, int bitmapWidth) {
        if(strings != null && strings.size() > 0) {
            Bitmap bitmap = Bitmap.createBitmap(bitmapWidth, 26*strings.size(), Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(Color.WHITE);
            TextPaint paint = new TextPaint();
            paint.setTextSize(24.0F);
            paint.setColor(Color.BLACK);
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setTypeface(Typeface.MONOSPACE);

            int i = 0;
            for (String string : strings) {
                canvas.drawText(string, 0, 24 + 26 * i, paint);
                canvas.save();
                canvas.restore();
                i++;
            }
//            canvas.save(Canvas.ALL_SAVE_FLAG);
//            canvas.restore();
            String path = ManagerFile.POSPAL_ROOT + "image.png";
            System.out.println(path);
            try {
                FileOutputStream os = new FileOutputStream(new File(path));
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.flush();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        return null;
    }

    public static String toHexString(String s) {
        StringBuilder sb = new StringBuilder(64);
        for (int i=0;i<s.length();i++) {
            int ch = (int)s.charAt(i);
            String s4 = Integer.toHexString(ch);
            sb.append(s4);
        }

        return sb.toString();
    }

	private static InputFilter[] filters;
	public static final InputFilter[] getEmojiFittlers() {
		if (filters == null) {
			InputFilter emojiFilter = new InputFilter() {

				Pattern emoji = Pattern.compile(
						"[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
						Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

				@Override
				public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart,
										   int dend) {

					Matcher emojiMatcher = emoji.matcher(source);
					if (emojiMatcher.find()) {
						return "";
					}

					return null;
				}
			};
			filters = new InputFilter[1];
			filters[0] = emojiFilter;
		}

		return filters;
	}

	public static final String addChar2Width(String oldString, int width, String fillChar) {
		int len = oldString.length();
		if (len >= width) {
			return oldString;
		}

		String str = oldString;
		int space = width - len;
		int preSpace = space / 2;
		int afterSpace = space - preSpace;
		StringBuilder sb = new StringBuilder(4);
		for (int i = 0; i < preSpace; i++) {
			sb.append(fillChar);
		}
		str = sb + str;
		sb = new StringBuilder(4);
		for (int i = 0; i < afterSpace; i++) {
			sb.append(fillChar);
		}
		str = str + sb;

		return str;
	}

	public static boolean isNullOrEmpty(String str) {
		return str == null || str.trim().equals("");
	}

    public static String sqliteEscape(String keyWord){
        keyWord = keyWord.replace("/", "//");
        keyWord = keyWord.replace("'", "''");
        keyWord = keyWord.replace("[", "/[");
        keyWord = keyWord.replace("]", "/]");
        keyWord = keyWord.replace("%", "/%");
        keyWord = keyWord.replace("&","/&");
        keyWord = keyWord.replace("_", "/_");
        keyWord = keyWord.replace("(", "/(");
        keyWord = keyWord.replace(")", "/)");
        return keyWord;
    }

	/**
	 * emoji表情替换
	 *
	 * @param source 原字符串
	 * @param slipStr emoji表情替换成的字符串
	 * @return 过滤后的字符串
	 */
	public static String filterEmoji(String source,String slipStr) {
		if(!isNullOrEmpty(source)){
			return source.replaceAll("[\ud800\udc00-\udbff\udfff\ud800-\udfff]", slipStr);
		}else{
			return source;
		}
	}

	public static String filterUnSuportChar(String source,String slipStr){
		return filterEmoji(source, slipStr);
	}

    public static String getUrlFileName(String url) {
        int lastIndexOf = url.lastIndexOf('/');
        String fileName = url.substring(lastIndexOf);
        return fileName;
    }
}
