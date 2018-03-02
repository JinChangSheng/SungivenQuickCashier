package cn.pospal.www.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;

/**
 * 参考https://github.com/SpikeKing/wcl-permission-demo
 * 检查权限的工具类
 * <p/>
 * Created by wangchenlong on 16/1/26.
 */
public class PermissionsChecker {
	// 判断权限集合
	public static boolean lacksPermissions(String... permissions) {
		// 6.0以下不判断是否有权限
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			return false;
		}

		for (String permission : permissions) {
			if (lacksPermission(permission)) {
				return true;
			}
		}
		return false;
	}

	// 判断是否缺少权限
	public static boolean lacksPermission(String permission) {
		return ContextCompat.checkSelfPermission(ManagerApp.getInstance(), permission) ==
			PackageManager.PERMISSION_DENIED;
	}

	public static boolean lackAlertPermission(Context context) {
		if(Build.VERSION.SDK_INT < 23){
			return false;
		} else {
			if (Settings.canDrawOverlays(context)) {
				return false;
			} else {
				return true;
			}
		}
	}

	public static final int REQUEST_ALERT_PERMISSION = 101;
	public static void checkAlertPermission(Activity context){
		Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
				Uri.parse("package:" + context.getPackageName()));
		context.startActivityForResult(intent, REQUEST_ALERT_PERMISSION);

	}

    public static boolean lackWriteSettingPermission(Context context) {
        if(Build.VERSION.SDK_INT < 23){
            return false;
        } else {
            if (Settings.System.canWrite(context)) {
                return false;
            } else {
                return true;
            }
        }
    }

    public static final int REQUEST_WRITESETTING_PERMISSION = 102;
    public static void checkWriteSettingPermission(Activity context){
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                Uri.parse("package:" + context.getPackageName()));
        context.startActivityForResult(intent, REQUEST_WRITESETTING_PERMISSION);

    }
}
