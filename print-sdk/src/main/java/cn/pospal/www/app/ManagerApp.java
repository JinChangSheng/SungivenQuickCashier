package cn.pospal.www.app;

import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.SystemClock;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import cn.pospal.www.debug.D;
import cn.pospal.www.hardware.init.DefaultIniter;
import cn.pospal.www.manager.ManagerConf;
import cn.pospal.www.manager.ManagerFile;
import cn.pospal.www.posbase.R;
import cn.pospal.www.service.SystemService;
import cn.pospal.www.util.StringUtil;
import cn.pospal.www.util.SystemUtil;

/**
 * 软件管理器 主要功能有异常捕捉和软件退出
 *
 * @author Near
 *
 */
public class ManagerApp extends Application {
	private static ManagerApp instance;
	private static RequestQueue requestQueue;

    public static DefaultIniter pospalIniter;

	public static ManagerApp getInstance() {
		return instance;
	}

	public static RequestQueue getRequestQueue() {
		return requestQueue;
	}

	private String processName;
	@Override
	public void onCreate() {
        //判断进程名，保证只有主进程运行
		processName = SystemUtil.getProcessName(this, android.os.Process.myPid());
        if (StringUtil.isNullOrEmpty(processName) || !processName.equals(this.getPackageName())) {
            // 不是主进程不做初始化
            return;
        }

		instance = this;

        initData();
		super.onCreate();
	}

    public void initData() {
        // 初始化sd卡
        try {
            ManagerFile.getSDCardStatue(instance, true);
        } catch (Exception e) {
            e.printStackTrace();
        }


        // 程序初始化
        appBaseInit();

        // 配置volley
        requestQueue = Volley.newRequestQueue(instance, new PPHurlStack());
    }

	/**
	 * 基础初始化初始化
	 */
	public static void appBaseInit() {
		// 初始化配置文件
		ManagerConf.initManager_Conf(instance);
		// 初始化数据库
		//DatabaseHelper.initDatabase(instance);
		//DatabaseHelper.initAllTables();

        prepareSellData();
		//RamStatic.initAllPrinter();
	}

    public static void prepareSellData() {
        // 初始化设置和内存数据
		AppConfig.initAllConfig();
		RamStatic.intRamData(false);
    }

	private void showSystemToast(String msg, int showTime) {
		lastToastTime = System.currentTimeMillis();

		Toast toast = new Toast(this);
		View toastView = View.inflate(this, R.layout.toast, null);
		TextView msgTv = (TextView) toastView.findViewById(R.id.msg);
		msgTv.setText(msg);
		toast.setView(toastView);
		toast.setDuration(showTime);
		if ("siupo".equals(AppConfig.company)||"elc".equals(AppConfig.company)) {//讯普、易乐看 自助点餐显示在屏幕中心
			toast.setGravity(Gravity.CENTER, 0, 0);
		}else{
			toast.setGravity(Gravity.CENTER|Gravity.TOP, 0, getResources().getInteger(R.integer.toast_margin_top));
		}
		toast.show();
	}

	private int lastToastId = 0;
	private String lastToast = "";
	private long lastToastTime = 0;
	private final int SAME_TOAST_SPACE = 2000;
	public void showToast(@StringRes int stringId, int showTime) {
		if(instance != null) {
			long currentTime = System.currentTimeMillis();
			if(stringId == lastToastId
					&& (currentTime - lastToastTime < SAME_TOAST_SPACE)) {
				return;
			}
			lastToastId = stringId;
			lastToast = "";

			showSystemToast(getString(stringId), showTime);
		}
	}


	/**
	 * 重新初始化文件系统
	 * 安卓6.0之后需要重新授权SD卡权限
	 */
	public void reinitFileSystem() {
		ManagerFile.getSDCardStatue(instance, true);
		/*DiskLruImageCache lruImageCache
			= new DiskLruImageCache(instance, IMAGE_CACHE);*/
	}

	public void showToast(@StringRes int stringId) {
		showToast(stringId, Toast.LENGTH_SHORT);
	}

	public void showToast(String msg, int showTime) {
		// 防止NPE
		if(msg == null) {
			return;
		}

		if(instance != null) {
			long currentTime = System.currentTimeMillis();
			if(msg.equals(lastToast)
					&& (currentTime - lastToastTime < SAME_TOAST_SPACE)) {
				return;
			}
			lastToast = msg;
			lastToastId = 0;

			showSystemToast(msg, showTime);
		}
	}

	public void showToast(String msg) {
		showToast(msg, Toast.LENGTH_SHORT);
	}

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        D.out("ManagerApp onConfigurationChanged newConfig = " + newConfig);
		//判断进程名，保证只有主进程运行
		processName = SystemUtil.getProcessName(this, android.os.Process.myPid());
		if (StringUtil.isNullOrEmpty(processName) || !processName.equals(this.getPackageName())) {
			// 不是主进程不做初始化
			super.onConfigurationChanged(newConfig);
			return;
		}
        // 加载语言资源
        AppConfig.language = SystemUtil.getLanguage();

        super.onConfigurationChanged(newConfig);
    }

	public void startAllService() {
		D.out("startAllService");
		try {
            if (SystemService.getInstance() != null) {
                SystemService.getInstance().stopSelf();
            }
		} catch (Exception e) {
			D.out(e);
		}

        while (SystemService.getInstance() != null) {
            SystemClock.sleep(100);
        }
		startService(new Intent(this, SystemService.class));

	}
}

