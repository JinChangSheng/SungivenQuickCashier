package cn.pospal.www.manager;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Build;

import cn.pospal.www.app.ManagerApp;
import cn.pospal.www.app.RamStatic;
import cn.pospal.www.otto.DeviceEvent;
import cn.pospal.www.posbase.R;

public class ManagerNet {
	/** 网络状态字 **/
	public static final int NET_STATUS_UNKNOWN = -1; // 状态未知
	public static final int NET_STATUS_UNCONNECT = 0; // 网络未连接
	public static final int NET_STATUS_STATE = 1; // WIFI网络或者以太网
	public static final int NET_STATUS_MOBILE = 2; // 手机网络
	public static final int NET_STATUS_WIFI_CHANGING = 3; // WIFI网络切换中
	public static final int NET_STATUS_MOBILE_CHANGING = 4; // 手机网络切换中

	public static int getNetStatus() {
		Context context = ManagerApp.getInstance();
		if(context == null) {
			return NET_STATUS_UNKNOWN;
		}
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo currentNetworkInfo = cm.getActiveNetworkInfo();

		if (currentNetworkInfo == null) {
			return NET_STATUS_UNCONNECT;
		}

		int type = currentNetworkInfo.getType();
		if (type == ConnectivityManager.TYPE_WIFI
				|| type == ConnectivityManager.TYPE_ETHERNET) {
			if (!currentNetworkInfo.isAvailable()) {
				if (currentNetworkInfo.getState() == State.CONNECTING) {
					return NET_STATUS_WIFI_CHANGING;
				}

				return NET_STATUS_UNCONNECT;
			}

			return NET_STATUS_STATE;
		} else {
			if (!currentNetworkInfo.isAvailable()) {
				if (currentNetworkInfo.getState() == State.CONNECTING) {
					return NET_STATUS_MOBILE_CHANGING;
				}

				return NET_STATUS_UNCONNECT;
			}

			return NET_STATUS_MOBILE;
		}

	}
	
	public static boolean isNetAlive() {
		int netStatus = getNetStatus();
		if(netStatus == NET_STATUS_STATE || netStatus == NET_STATUS_MOBILE) {
			return true;
		}
		return false;
	}

	public static void startNetworkSettingActivity(Context context) {
        try {
            Intent intent = new Intent();
            int sdkVersion = Build.VERSION.SDK_INT;
            if (sdkVersion >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
            } else {
                intent.setClassName("com.android.settings",
                    "com.android.settings.WirelessSettings");// android4.0系统找不到此activity。
            }

            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            ManagerApp.getInstance().showToast(R.string.set_network_manual);
        }
    }
}
