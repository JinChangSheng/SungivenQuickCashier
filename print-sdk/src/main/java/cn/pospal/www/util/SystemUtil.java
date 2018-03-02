package cn.pospal.www.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Point;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Vibrator;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Layout;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pospal.www.app.AppConfig;
import cn.pospal.www.app.ManagerApp;
import cn.pospal.www.debug.D;
import cn.pospal.www.posbase.R;

import static android.content.Context.TELEPHONY_SERVICE;

public class SystemUtil {
	public static String getLocalIpAddress() {
		String ipaddress = "";

		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						ipaddress = inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("IpAddress", ex.toString());

			return null;
		}
		D.out("DDDDDD ipaddress = " + ipaddress);
		return ipaddress;
	}

	/**
	 * Get IP address from first non-localhost interface
	 * 
	 * @param useIPv4
	 *            true=return ipv4, false=return ipv6
	 * @return address or empty string
	 */
	public static String getIPAddress(boolean useIPv4) {
		try {
			List<NetworkInterface> interfaces = Collections
					.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface intf : interfaces) {
				List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
				for (InetAddress addr : addrs) {
					if (!addr.isLoopbackAddress()) {
						String sAddr = addr.getHostAddress().toUpperCase(Locale.CHINA);

						return sAddr;
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * 获取IP
	 * @return
	 */
	public static String getLocalIP() {
		WifiManager wifiManager = (WifiManager) ManagerApp.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		if(wifiManager != null) {
			if(wifiManager.isWifiEnabled()) {
				WifiInfo wifiInfo = wifiManager.getConnectionInfo();
				if(wifiInfo != null) {
					int ipAddress = wifiInfo.getIpAddress();
					if (ipAddress != 0) {
						return ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "."
								+ (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));
					}
				}
			} else {
                return ManagerApp.getInstance().getString(R.string.wifi_not_open);
			}
		}

        // 实在不行使用老的方法获取
		return getIPAddress(true);
	}

	/**
	 * 判断是否为合法IP
	 */
	public static boolean isIp(String ipAddress) {
		if (ipAddress == null) {
			return false;
		}

		Pattern pattern = Pattern
				.compile("([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}");
		Matcher matcher = pattern.matcher(ipAddress);
		return matcher.matches();
	}

	/**
	 * 判断是否自身ip
	 */
	public static boolean isLocalIp(String ipAddress) {
		boolean isLocalIp = ipAddress.equals("127.0.0.1");
		if (!isLocalIp) {
			String localIp = getLocalIP();
			if (localIp != null && !localIp.equals(ManagerApp.getInstance().getString(R.string.wifi_not_open))) {
				return ipAddress.equals(localIp);
			}
		}

		return isLocalIp;
	}

	public static float getScreenDensity(Activity callActivity) {
		DisplayMetrics dm = new DisplayMetrics();
		callActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);

		return dm.density;
	}

	public static Point getWindowPar(Activity callActivity) {
        Point point = new Point(0, 0);

		DisplayMetrics dm = new DisplayMetrics();
		callActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		point.x = dm.widthPixels;
		point.y = dm.heightPixels;
		D.out("point.y = " + point.y + ", density = " + dm.density
			+ ", densityDpi = " + dm.densityDpi + ", scaledDensity = "
			+ dm.scaledDensity + ", heightPixels = " + dm.heightPixels
			+ ", widthPixels = " + dm.widthPixels + ", = ");
		return point;
	}

    private static Point screenSize;
    /**
     * Returns the size of the screen in pixels (width and height)
     *
     * @return a Point that has the screen width stored in x and height in y
     */
	/**
	 * 获取软件版本号
	 * 
	 * @return
	 */
	public static String getVerCode() {
		Context context = ManagerApp.getInstance();
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息

		try {
			PackageInfo packInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			String version = packInfo.versionName;
			return version;
		} catch (NameNotFoundException e) {
			D.out(e);

			return "-1";
		}
	}

	public static boolean isInstalled(String packageName) {
		Context context = ManagerApp.getInstance();
		final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
		// 从pinfo中将包名字逐一取出，压入pName list中
		if (pinfo != null) {
			for (int i = 0; i < pinfo.size(); i++) {
				String pn = pinfo.get(i).packageName;
				if (pn.equalsIgnoreCase(packageName)) {
					return true;
				}
			}
		}

		return false;// 判断pName中是否有目标程序的包名，有TRUE，没有FALSE
	}

	public static String getLocalDeviceId(Context _context) {
		TelephonyManager tm = (TelephonyManager) _context
				.getSystemService(TELEPHONY_SERVICE);
		String deviceId = tm.getDeviceId();
		if (deviceId == null || deviceId.trim().length() == 0) {
			deviceId = String.valueOf(System.currentTimeMillis());
		}
		return deviceId;
	}

	public static String getLocalMacAddress(Context context) {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

	public static String getDeviceId(Context context) {
		String deviceIdStr = getLocalDeviceId(context);
		if (deviceIdStr == null || deviceIdStr.equals("")) {
			deviceIdStr = getLocalMacAddress(context);

			if (deviceIdStr == null || deviceIdStr.equals("")) {
				deviceIdStr = Math.random() + "";
			}
		}

		return deviceIdStr;
	}

	public static void openKeyboard(EditText editText) {
		D.out("openKeyboard editText = " + editText);
        if (editText != null) {
//            InputMethodManager inputManager = (InputMethodManager) view
//                    .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//	        inputManager.toggleSoftInputFromWindow(
//		        view.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
	        editText.requestFocus();
	        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
	        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
//	        ((Activity) editText.getContext()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//	        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//	        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }
	}

	public static void closeKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
	}

//	public static void closeKeyboard(Activity activity) {
//		if(activity == null){
//			return;
//		}
//		InputMethodManager imm = (InputMethodManager) activity
//				.getSystemService(Context.INPUT_METHOD_SERVICE);
//		// 得到InputMethodManager的实例
//		if (imm.isActive() && activity.getCurrentFocus()!=null) {
//			// 如果开启
//			imm.hideSoftInputFromWindow(activity.getCurrentFocus()
//					.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//			// 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
//		}
//	}

	public static boolean hasCameras() {
		PackageManager pm = ManagerApp.getInstance().getPackageManager();

		if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) && Camera.getNumberOfCameras() > 0) {
			return true;
		}

		return false;
	}

	public static int getCameraCount() {
		return Camera.getNumberOfCameras();
	}
	
	public static String getCurrencySymbol(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        Currency currency = null;
        try {
            currency = Currency.getInstance(locale);
        } catch (Exception e) {
            e.printStackTrace();
            currency = Currency.getInstance(Locale.SIMPLIFIED_CHINESE);
        }
        D.out("XXXXXXX currency = " + currency.getCurrencyCode() + "->" + currency.getSymbol());
		return currency.getSymbol();
	}

    public static final int INTENT_TYPE_WEB = 0;
    public static final int INTENT_TYPE_TEL = 1;
    public static final int INTENT_TYPE_QQ = 2;
    public static final int INTENT_TYPE_SMS = 3;
    public static final int INTENT_TYPE_MAIL = 4;
    /**
     * 检测系统可否处理意图
     * @param intentType
     * @return
     */
    public static boolean canHandlerIntent(int intentType) {
        if(intentType == INTENT_TYPE_WEB) {
            Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://www.pospal.cn/"));
            PackageManager manager = ManagerApp.getInstance().getPackageManager();
            List<ResolveInfo> infos = manager.queryIntentActivities(intent, 0);

            return infos.size() > 0;
        }
        if(intentType == INTENT_TYPE_TEL) {
            Intent intent = new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:4008066866"));
            PackageManager manager = ManagerApp.getInstance().getPackageManager();
            List<ResolveInfo> infos = manager.queryIntentActivities(intent, 0);
            System.out.println(infos);
            return infos.size() > 0;
        }
        if(intentType == INTENT_TYPE_QQ) {
            if(isInstalled("com.tencent.mobileqq")
                    || isInstalled("com.tencent.qqlite")
                    || isInstalled("com.tencent.eim")) {
                return true;
            }
        }

        return false;
    }

    /**
     * 获取系统语言
     * @return
     */
    public static final String getLanguage() {
        Locale locale = ManagerApp.getInstance().getResources().getConfiguration().locale;
        String language = locale.getDefault().toString();

        return language;
    }

    public static final String getAppName() {
        Context context = ManagerApp.getInstance();
        ApplicationInfo info = context.getApplicationInfo();
        PackageManager localPackageManager = context.getPackageManager();
        String appName = info.loadLabel(localPackageManager).toString();

	    return appName;
    }

    public static final String getPackageName() {
        Context context = ManagerApp.getInstance();
        ApplicationInfo info = context.getApplicationInfo();
	    return info.packageName;
    }

	/**
	 * 打印进程输出
	 *
	 * @param process 进程
	 */
	private static void readProcessOutput(final Process process) {
		// 将进程的正常输出在 System.out 中打印，进程的错误输出在 System.err 中打印
		read(process.getInputStream(), System.out);
		read(process.getErrorStream(), System.err);
	}

	// 读取输入流
	private static void read(InputStream inputStream, PrintStream out) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

			String line;
			while ((line = reader.readLine()) != null) {
				out.println(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ping一个ip地址
	 * @param ip
	 * @return
	 */
	public static boolean pingLocalIp(final String ip){
		D.out("pingLocalIp");
		// 三星不支持ping
		if (android.os.Build.MODEL.toLowerCase().contains("samsung")) {
			return true;
		}
		if (!isIp(ip)) {
			return false;
		}

        final boolean[] success = {false};
        final boolean[] hasEnd = {false};
        final Process[] process = {null};
        long startTime = System.currentTimeMillis();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
	                // -c 次数
	                // -s 包大小，单位byte
	                // -i 间隔，单位秒
	                // -W 超时
	                // -w 最后期限，单位秒
	                // -q 只打印开始结束信息，中间忽略
                    process[0] = Runtime.getRuntime().exec("ping -c 2 -s 8 -i 0.2 -W 1 -w 2 -q " + ip);
                    if (process[0] != null) {
                        readProcessOutput(process[0]);

                        int status = process[0].waitFor();
                        if (status == 0) {
                            success[0] = true;
                        } else {
                            success[0] = false;
                        }
                        hasEnd[0] = true;
                    } else {
                        throw new IOException("open process error 1");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    success[0] = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    success[0] = false;
                } catch (Exception e) {
                    e.printStackTrace();
                    success[0] = false;
                } finally {
                    if (process[0] != null) {
                        process[0].destroy();
                    }
                }
            }
        });
        thread.start();

        while (!hasEnd[0]) {
            // 超过5s返回错误
            if (System.currentTimeMillis() - startTime > 2000) {
                try {
                    if (process[0] != null) {
                        process[0].destroy();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

		D.out("ping reult = " + success[0]);
		D.out("pingLocalIp time = " + (System.currentTimeMillis() - startTime));
		return success[0];
	}

	/**
	 * @author suncat
	 * @category 判断是否有外网连接（普通方法不能判断外网的网络是否连接，比如连接上局域网）
	 * @return
	 */
	public static boolean pingWeb(final String website) {
		// 三星不支持ping
		if (android.os.Build.MODEL.toLowerCase().contains("samsung")) {
			return true;
		}

        final boolean[] success = {false};
        final boolean[] hasEnd = {false};
        final Process[] process = {null};
        long startTime = System.currentTimeMillis();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    process[0] = Runtime.getRuntime().exec("ping -c 3 -w 5 -q " + website);
                    if (process[0] != null) {
                        readProcessOutput(process[0]);

                        int status = process[0].waitFor();
                        if (status == 0) {
                            success[0] = true;
                        } else {
                            success[0] = false;
                        }
                        hasEnd[0] = true;
                    } else {
                        throw new IOException("open process error 2");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    success[0] = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    success[0] = false;
                } catch (Exception e) {
                    e.printStackTrace();
                    success[0] = false;
                } finally {
                    if (process[0] != null) {
                        process[0].destroy();
                    }
                }
            }
        });
        thread.start();

        while (!hasEnd[0]) {
            // 超过15s返回错误
            if (System.currentTimeMillis() - startTime > 15000) {
                try {
                    if (process[0] != null) {
                        process[0].destroy();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        D.out("ping reult = " + success[0]);
        D.out("pingLocalIp time = " + (System.currentTimeMillis() - startTime));
        return success[0];
	}

    public static boolean pingWeb2() {
        D.out("pingWeb2");
        try {
            HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.baidu.com").openConnection());
            urlc.setRequestProperty("User-Agent", "Test");
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(3000);
            urlc.setReadTimeout(3000);
            urlc.connect();
            return (urlc.getResponseCode() == 200);
        } catch (IOException e) {
            Log.e("Pospal", "Error checking internet connection", e);
        }
        return false;
    }

    public static boolean pingWeb3() {
        D.out("pingWeb3");
        try {
            int timeoutMs = 3000;
            Socket socket = new Socket();
            socket.setSoTimeout(timeoutMs);
            SocketAddress socketAddress = new InetSocketAddress("www.baidu.com", 80);
            socket.connect(socketAddress, timeoutMs);
            socket.close();

            return true;
        } catch (IOException e) { return false; }
    }

    public static final void showRamInfo() {
        long totalMemory=Runtime.getRuntime().totalMemory();
        long freeMemory=Runtime.getRuntime().freeMemory();
        long usedMemory=(totalMemory-freeMemory)>>10;
        totalMemory = totalMemory>>10;
        freeMemory = freeMemory>>10;

        D.out("总内存：" + totalMemory);
        D.out("已使用内存：" + usedMemory);
        D.out("剩余内存：" + freeMemory);
    }

	/**
	 * 获取系统属性
	 * @param key
	 * @return
	 */
	public static final String getSystemProperty(String key, String defaultValue) {
		Properties p = new Properties();
		FileInputStream fis = null;

		String value = defaultValue;
		try {
			fis = new FileInputStream("/system/build.prop");
			p.load(fis);

			value = p.getProperty(key, defaultValue);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return value;
	}

	public static final String getRecommandIP() {
		String localIP = getLocalIP();

		if (localIP != null && localIP.equals(ManagerApp.getInstance().getString(R.string.wifi_not_open))) {
			String[] ips = localIP.split("\\.");
			if (ips.length == 4) {
				return ips[0] + "." + ips[1] + "." + ips[2] + ".";
			}
		}

		return "";
	}

	/***
	 * 合并字节数组
	 * @param a
	 * @return
	 */
	public static byte[] mergeArray(byte[]... a) {
		// 合并完之后数组的总长度
		int index = 0;
		int sum = 0;
		for (int i = 0; i < a.length; i++) {
			sum = sum + a[i].length;
		}
		byte[] result = new byte[sum];
		for (int i = 0; i < a.length; i++) {
			int lengthOne = a[i].length;
			if(lengthOne==0){
				continue;
			}
			// 拷贝数组
			System.arraycopy(a[i], 0, result, index, lengthOne);
			index = index + lengthOne;
		}
		return result;
	}

    public static final int QUICK_CLICK_TIME = 600;
	private static long lastClickTime = 0;
	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if ( 0 < timeD && timeD < QUICK_CLICK_TIME) {
			return true;
		}

		lastClickTime = time;
		return false;
	}

	private static String lastTag;
    private static long lastClickTime2 = 0;
	public static boolean isFastDoubleClickByTag(String tag) {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime2;
		if ((lastTag != null && lastTag.equals(tag)) && 0 < timeD && timeD < QUICK_CLICK_TIME) {
			return true;
		}

        lastClickTime2 = time;
        lastTag = tag;
		return false;
	}

    /**
     * 有时候点击跳转新界面里面的自动点击可能受到影响无法再次点击
     */
	public static void resetLastClickTime() {
        lastClickTime = 0;
    }

	public static final boolean isListNullOrEmpty(List list) {
		if (list == null || list.size() == 0) {
			return true;
		}

		return false;
	}

	/**
	 * 将json格式的字符串解析成Map对象
	 */
	public static final Map<String, Object> object2Map(Object object) {
		HashMap<String, Object> data = new HashMap<>();
		try {
			// 将Object转换成jsonObject
			JSONObject jsonObject = new JSONObject(GsonUtil.getInstance().toJson(object));
			Iterator it = jsonObject.keys();
			// 遍历jsonObject数据，添加到Map对象
			while (it.hasNext()) {
				String key = String.valueOf(it.next());
				Object value = jsonObject.get(key);
				data.put(key, value);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * Check if the device's camera has a Flashlight.
	 * @return true if there is Flashlight, otherwise false.
	 */
	public static boolean hasFlash() {
		return ManagerApp.getInstance().getPackageManager()
			.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
	}

    public static int getEllipsisLines(TextView textView) {
        Layout layout = textView.getLayout();
        if(layout != null) {
            int lines = layout.getLineCount();
            if(lines > 0) {
                return layout.getEllipsisCount(lines-1);
            }
        }

        return 0;
    }

	/**
	 * 验证手机格式
	 */
	public static boolean isMobileNO(String mobiles) {
		/*
		移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		联通：130、131、132、152、155、156、185、186
		电信：133、153、180、189、（1349卫通）
		总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		*/
		String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		if (TextUtils.isEmpty(mobiles)) {
			return false;
		} else {
			return mobiles.matches(telRegex);
		}
	}
	/**
	 * 检测邮箱地址是否合法
	 * @param email
	 * @return true合法 false不合法
	 */
	public static boolean isEmail(String email){
		if (null==email || "".equals(email)) return false;
//        Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
		Pattern p =  Pattern.compile("\\w+(\\.\\w)*@\\w+(\\.\\w{2,3}){1,3}");//复杂匹配
		Matcher m = p.matcher(email);
		return m.matches();
	}

    /**
     * 获取设备特征码
     * @return
     */
    public static final String getClientDeviceUid() {
        Context context = ManagerApp.getInstance();
        // 获取imei
        TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        String imei = TelephonyMgr.getDeviceId();
        // 拼凑出特殊uid
        String makeUid = "90" + //we make this look like a valid IMEI
                Build.BOARD.length()%10 +
                Build.BRAND.length()%10 +
                Build.CPU_ABI.length()%10 +
                Build.DEVICE.length()%10 +
                Build.DISPLAY.length()%10 +
                Build.HOST.length()%10 +
                Build.ID.length()%10 +
                Build.MANUFACTURER.length()%10 +
                Build.MODEL.length()%10 +
                Build.PRODUCT.length()%10 +
                Build.TAGS.length()%10 +
                Build.TYPE.length()%10 +
                Build.USER.length()%10 ; //13 digits
        // 获取AndroidUid
        String androidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        // 获取无线网卡地址
        WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        String wlanMacAddress = null;
        if (wm != null) {
            wlanMacAddress = wm.getConnectionInfo().getMacAddress();
        }
        // 获取蓝牙地址
        BluetoothAdapter bluetoothAdapter = null; // Local Bluetooth adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String btMacAddress = null;
        if (bluetoothAdapter != null) {
            btMacAddress = bluetoothAdapter.getAddress();
        }
        String caculateUid = imei + makeUid
                + androidID+ wlanMacAddress + btMacAddress;
        // compute md5
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        messageDigest.update(caculateUid.getBytes(), 0, caculateUid.length());
        // get md5 bytes
        byte p_md5Data[] = messageDigest.digest();
        // create a hex string
        String uniqueID = new String();
        for (int i=0;i<p_md5Data.length;i++) {
            int b =  (0xFF & p_md5Data[i]);
            // if it is a single digit, make sure it have 0 in front (proper padding)
            if (b <= 0xF)
                uniqueID+="0";
            // add number to string
            uniqueID+=Integer.toHexString(b);
        }   // hex string to uppercase
        uniqueID = uniqueID.toUpperCase();
        D.out("getClientDeviceUid uniqueID = " + uniqueID);
        return uniqueID;
    }

    public static void restartApp(Class<? extends Activity> startActivityClass) {
        ManagerApp context = ManagerApp.getInstance();
        Intent restartIntent = new Intent(context, startActivityClass);
        int pendingIntentId = 123456;
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, pendingIntentId, restartIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent);
        System.exit(0);
    }

    private static int statusBarHeight = -1;
    // 获取手机状态栏高度
    public static int getStatusBarHeight(Activity activity) {
        if (statusBarHeight == -1) {
            Class<?> c = null;
            Object obj = null;
            Field field = null;
            int x = 0;
            try {
                c = Class.forName("com.android.internal.R$dimen");
                obj = c.newInstance();
                field = c.getField("status_bar_height");
                x = Integer.parseInt(field.get(obj).toString());
                statusBarHeight = activity.getResources().getDimensionPixelSize(x);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return statusBarHeight;
    }

    private static int actionBarHeight = -1;
    // 获取ActionBar的高度
    public static int getActionBarHeight(Activity activity) {
        if (actionBarHeight == -1) {
            TypedValue tv = new TypedValue();
            if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))// 如果资源是存在的、有效的
            {
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, activity.getResources().getDisplayMetrics());
            }
        }
        D.out("actionBarHeight = " + actionBarHeight);
        return actionBarHeight;
    }


    /**
     * 是否存在导航栏
     *
     * @param activity
     * @return
     */
    public static boolean navigationBarExist2(Activity activity) {
        WindowManager windowManager = activity.getWindowManager();
        Display d = windowManager.getDefaultDisplay();

        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            d.getRealMetrics(realDisplayMetrics);
        }

        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;

        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
    }

    private static int navigationBar = -1;
    // 获取ActionBar的高度
    public static int getNavigationBarHeight(Activity activity) {
        if (navigationBarExist2(activity)) {
            if (navigationBar == -1) {
                navigationBar = 96;
                Resources resources = activity.getResources();
                int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    navigationBar = resources.getDimensionPixelSize(resourceId);
                }
            }
            D.out("navigationBar = " + navigationBar);
            return navigationBar;
        }

        return 0;
    }

    /**
     * 获取手机内部剩余存储空间
     *
     * @return
     */
    public static long getMaxMemorySize() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.maxMemory() / 1024 / 1024;
    }

    /**
     * 获取进程名称
     * @param cxt
     * @param pid
     * @return
     */
    public static String getProcessName(Context cxt, int pid) {
        String processNameQuick = getProcessNameQuick();

        if (StringUtil.isNullOrEmpty(processNameQuick)) {
            ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
            if (runningApps == null) {
                return null;
            }
            for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
                if (procInfo.pid == pid) {
                    return procInfo.processName;
                }
            }
            return null;
        }

        return processNameQuick;
    }

    public static String getProcessNameQuick() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 拆分集合
     * @param <T>
     * @param resList  要拆分的集合
     * @param count    每个集合的元素个数
     * @return  返回拆分后的各个集合
     */
    public static <T> List<List<T>> split(List<T> resList,int count){

        if(resList==null || count<1)
            return  null ;

        List<List<T>> ret= new ArrayList<>();
        int size=resList.size();
        if(size<=count){ //数据量不足count指定的大小
            ret.add(resList);
        } else {
            int pre=size/count;
            int last=size%count;
            //前面pre个集合，每个大小都是count个元素
            for(int i=0;i<pre;i++){
                List<T> itemList=new ArrayList<T>();
                for(int j=0;j<count;j++){
                    itemList.add(resList.get(i*count+j));
                }
                ret.add(itemList);
            }
            //last的进行处理
            if(last>0){
                List<T> itemList=new ArrayList<T>();
                for(int i=0;i<last;i++){
                    itemList.add(resList.get(pre*count+i));
                }
                ret.add(itemList);
            }
        }

        return ret;
    }

    public static InputFilter getTestFilter() {
        InputFilter testFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                System.out.println("testFilter source = " + source);
                return source;
            }
        };

        return testFilter;
    }

    /**
     * 是否英语版本
     * @return
     */
    public static boolean isEnLocalVersion() {
        return StringUtil.isStringNotNull(AppConfig.language) && AppConfig.language.startsWith("en_");

    }

    /**
     * 安卓POS 海外版折扣显示，改为 XXX% off
     * http://redmine.pospal.cn:12080/issues/8487
     */
    public static BigDecimal handleDiscountOff(BigDecimal discount) {
        if (discount != null && SystemUtil.isEnLocalVersion()) {
            return NumUtil.BigDecimal_100.subtract(discount);
        }

        return discount;
    }

    private static ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
    public static void beep() {
        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
    }

    public static void vibrate() {
        Vibrator v = (Vibrator) ManagerApp.getInstance().getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(300);
    }

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return  语言列表
     */
    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return  系统版本号
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return  手机型号
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return  手机厂商
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return  手机IMEI
     */
    public static String getIMEI(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
        if (tm != null) {
            return tm.getDeviceId();
        }
        return null;
    }

    /**
     * 通过左移位操作（<<）给每一段的数字加权
     * 第一段的权为2的24次方
     * 第二段的权为2的16次方
     * 第三段的权为2的8次方
     * 最后一段的权为1
     *
     * @param ip
     * @return int
     */
    public static int ipToInt(String ip) {
        String[] ips = ip.split("\\.");
        return (Integer.parseInt(ips[0]) << 24) + (Integer.parseInt(ips[1]) << 16)
                + (Integer.parseInt(ips[2]) << 8) + Integer.parseInt(ips[3]);
    }

    /**
     * 将整数值进行右移位操作（>>）
     * 右移24位，右移时高位补0，得到的数字即为第一段IP
     * 右移16位，右移时高位补0，得到的数字即为第二段IP
     * 右移8位，右移时高位补0，得到的数字即为第三段IP
     * 最后一段的为第四段IP
     *
     * @param addr
     * @return String
     */
    public static String intToIp(int addr) {
        return  ((addr & 0xFF) + "." +
                ((addr >>>= 8) & 0xFF) + "." +
                ((addr >>>= 8) & 0xFF) + "." +
                ((addr >>>= 8) & 0xFF));
    }

    public static int[] intToIpInts(int addr) {
        int[] ipParts = new int[4];
        ipParts[0] = addr & 0xFF;
        ipParts[1] = (addr >>>= 8) & 0xFF;
        ipParts[2] = (addr >>>= 8) & 0xFF;
        ipParts[3] = (addr >>>= 8) & 0xFF;
        return  ipParts;
    }


	public static final int LAST_SEND_TIME = 10*1000;
	private static long lastSendTime = 0;
	public static boolean isWithinAllottedTime() {
		long time = System.currentTimeMillis();
		long timeD = time - lastSendTime;
		if ( 0 < timeD && timeD < LAST_SEND_TIME) {
			return true;
		}

		lastSendTime = time;
		return false;
	}

	public static final boolean installApk(String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
        //为这个新apk开启一个新的activity栈
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            ManagerApp.getInstance().startActivity(intent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void startService(Intent intent) {
        if (Build.VERSION.SDK_INT < 21) {
            ManagerApp.getInstance().startService(intent);
        } else {
            ManagerApp.getInstance().startService(createExplicitFromImplicitIntent(ManagerApp.getInstance(), intent));
        }
    }

    public static void bindService(Intent intent, ServiceConnection connection) {
        if (Build.VERSION.SDK_INT < 21) {
            ManagerApp.getInstance().bindService(intent, connection, Context.BIND_AUTO_CREATE);
        } else {
            ManagerApp.getInstance().bindService(createExplicitFromImplicitIntent(ManagerApp.getInstance(), intent), connection, Context.BIND_AUTO_CREATE);
        }
    }

    private static Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);

        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }

        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);

        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);

        // Set the component to be explicit
        explicitIntent.setComponent(component);

        return explicitIntent;
    }

}
