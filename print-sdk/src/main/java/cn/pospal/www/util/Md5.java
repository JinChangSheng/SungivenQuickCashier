package cn.pospal.www.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import cn.pospal.www.debug.D;

public class Md5 {

	public static String byteArrayToHexString(byte[] bytes) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < bytes.length; n++) {
			stmp = (java.lang.Integer.toHexString(bytes[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs;
	}

	public static String Digest(String plain) {
		try {
			String b;
			MessageDigest md = MessageDigest.getInstance("md5");
			b = byteArrayToHexString(md.digest(plain.getBytes()));
			return b;
		} catch (Exception e) {
			return null;
		}
	}

	public static void main(String[] args) {
		String a = "88888888&consume&111111&35";
		System.out.println(Digest(a));
	}

    /**
     * api请求时用来加密账号密码
     * @param content
     * @param appKey
     * @return
     */
    public static String encryptToMd5String(String content,String appKey) {
        return encryptToMd5String(appKey.trim() + content.trim());
    }

    public static String encryptToMd5String(String content) {
        String md5String = null;
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("md5");
            md.update(content.getBytes("UTF-8"));
            md5String = parseByte2HexString(md.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return md5String;
    }

    private static String parseByte2HexString(byte buf[]) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < buf.length; i++)
        {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {hex = '0' + hex;}
            stringBuffer.append(hex.toUpperCase());
        }
        return stringBuffer.toString();
    }

    private static final char[] POSPAL_DEFINED_STANDARD_ENCODE_TABLE =
            {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
                    'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
                    1,2,3,4,5,6,7,8,9,0};
    /**
     * 因pc客户端不想引入太多的dll，跟客户端协定了一个加密算法，
     * @param pwd == > 10位随机数+Base64(10位随机数+密码)
     * @return
     */
    public static String encryptPospalDefinedPwd(String pwd) {
        Random r = new Random();
        int bound = POSPAL_DEFINED_STANDARD_ENCODE_TABLE.length;
        StringBuffer randomBuffer = new StringBuffer();
        for(int i=0;i<10;i++){
            randomBuffer.append(POSPAL_DEFINED_STANDARD_ENCODE_TABLE[r.nextInt(bound)]);
        }

        String randomPwd = PasswordUtil.getBase64(randomBuffer.append(pwd).toString());
        randomBuffer = new StringBuffer();
        for(int i=0;i<10;i++){
            randomBuffer.append(POSPAL_DEFINED_STANDARD_ENCODE_TABLE[r.nextInt(bound)]);
        }
        return randomBuffer.append(randomPwd).toString();
    }


    public static String decryptPospalDefinedPwd(String encryptPassword) {
        String randomPwdB64 = encryptPassword.substring(10);
        String randomPwd = PasswordUtil.getFromBase64(randomPwdB64);
        return randomPwd.substring(10);
    }



}
