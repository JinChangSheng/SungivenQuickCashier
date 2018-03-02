package cn.pospal.www.fun.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cn.pospal.www.debug.D;

public class CheckUtil {
	public static String encryptToMd5String(String content) {
		String md5String = null;
		MessageDigest messageDigest;
		try {
			messageDigest = MessageDigest.getInstance("md5");
			messageDigest.update(content.getBytes());
			md5String = parseByte2HexString(messageDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			D.out(e);
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
}
