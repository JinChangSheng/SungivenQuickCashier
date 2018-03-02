package cn.pospal.www.util;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtil {

	public static String encryptToMd5String(String content) {
		String md5String = null;
		MessageDigest messageDigest;
		try {
			messageDigest = MessageDigest.getInstance("md5");
			messageDigest.update(content.getBytes("utf8"));
			md5String = parseByte2HexString(messageDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return md5String;
	}

	public static String parseByte2HexString(byte buf[]) {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			stringBuffer.append(hex.toUpperCase());
		}
		return stringBuffer.toString();
	}

	public static byte[] parseHexString2Byte(String hexStr) {
		if (hexStr.length() < 1) {
			return null;
		}
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

    // 加密
    public static String getBase64(String str) {
        byte[] b = null;
        String s = null;
        try {
            b = str.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (b != null) {
            s = Base64.encodeToString(b, Base64.DEFAULT);
        }
        return s;
    }


    // 解密
    public static String getFromBase64(String s) {
        String result = null;
        if(s == null) return result;
        try {
            result = new String(Base64.decode(s, Base64.DEFAULT),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }


}
