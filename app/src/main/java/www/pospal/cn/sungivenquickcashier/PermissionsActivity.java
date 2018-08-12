package www.pospal.cn.sungivenquickcashier;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import cn.pospal.www.app.PermissionsChecker;

/**
 * 权限申请界面
 * 参考https://github.com/SpikeKing/wcl-permission-demo
 */

public class PermissionsActivity extends BaseActivity {
	public static final int PERMISSIONS_GRANTED = 0; // 权限授权
	public static final int PERMISSIONS_DENIED = 1; // 权限拒绝

	public static final int EXT_REQUEST_PERMISSION = 12358;                 // 外部请求
	private static final int PERMISSION_REQUEST_CODE = 0;                   // 系统权限管理页面的参数
	private static final String EXTRA_PERMISSIONS = "requestPermission";    // 权限参数
	private static final String EXTRA_MSG = "requestMessage";               // 权限参数
	private static final String PACKAGE_URL_SCHEME = "package:";            // 方案

	private boolean isRequireCheck; // 是否需要系统权限检测

	// 启动当前权限页面的公开接口
	public static void startActivityForResult(Activity activity, String msg, String... permissions) {
		Intent intent = new Intent(activity, PermissionsActivity.class);
		intent.putExtra(EXTRA_MSG, msg);
		intent.putExtra(EXTRA_PERMISSIONS, permissions);
		activity.startActivityForResult(intent, EXT_REQUEST_PERMISSION);
	}

	// 启动当前权限页面的公开接口
	public static void startActivityForResult(Fragment fragment, String msg, String... permissions) {
		Intent intent = new Intent(fragment.getActivity(), PermissionsActivity.class);
		intent.putExtra(EXTRA_MSG, msg);
		intent.putExtra(EXTRA_PERMISSIONS, permissions);
		fragment.startActivityForResult(intent, EXT_REQUEST_PERMISSION);
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getIntent() == null || !getIntent().hasExtra(EXTRA_PERMISSIONS)) {
			throw new RuntimeException("PermissionsActivity需要使用静态startActivityForResult方法启动!");
		}
		setContentView(R.layout.activity_permissions);

		isRequireCheck = true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (isRequireCheck) {
			String[] permissions = getPermissions();
			if (PermissionsChecker.lacksPermissions(permissions)) {
				requestPermissions(permissions); // 请求权限
			} else {
				allPermissionsGranted(); // 全部权限都已获取
			}
		} else {
			isRequireCheck = true;
		}
	}

	// 返回传递的权限参数
	private String[] getPermissions() {
		return getIntent().getStringArrayExtra(EXTRA_PERMISSIONS);
	}

	// 请求权限兼容低版本
	private void requestPermissions(String... permissions) {
		ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
	}

	// 全部权限均已获取
	private void allPermissionsGranted() {
		setResult(PERMISSIONS_GRANTED);
		finish();
	}

	/**
	 * 用户权限处理,
	 * 如果全部获取, 则直接过.
	 * 如果权限缺失, 则提示Dialog.
	 *
	 * @param requestCode  请求码
	 * @param permissions  权限
	 * @param grantResults 结果
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == PERMISSION_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
			isRequireCheck = true;
			allPermissionsGranted();
		} else {
			isRequireCheck = false;
			showMissingPermissionDialog();
		}
	}

	// 含有全部的权限
	private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
		for (int grantResult : grantResults) {
			if (grantResult == PackageManager.PERMISSION_DENIED) {
				return false;
			}
		}
		return true;
	}

	// 显示缺失权限提示
	private void showMissingPermissionDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(PermissionsActivity.this);
		builder.setTitle(R.string.request_permission);
		String permissionMsg = getIntent().getStringExtra(EXTRA_MSG);
		builder.setMessage(permissionMsg);

		// 拒绝, 退出应用
		builder.setNegativeButton(R.string.quit, new DialogInterface.OnClickListener() {
			@Override public void onClick(DialogInterface dialog, int which) {
				setResult(PERMISSIONS_DENIED);
				finish();
			}
		});

		builder.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
			@Override public void onClick(DialogInterface dialog, int which) {
				startAppSettings();
			}
		});

		builder.show();
	}

	// 启动应用的设置
	private void startAppSettings() {
		Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
		startActivity(intent);
	}
}