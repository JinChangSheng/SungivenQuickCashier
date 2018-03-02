package cn.pospal.www.manager;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

import cn.pospal.www.app.ManagerApp;
import cn.pospal.www.debug.D;
import cn.pospal.www.util.SystemUtil;

public class ManagerFile {
	/** 文件路径 **/
	public static String POSPAL_ROOT; // 软件路径
	public static String POSPAL_TMP_ROOT; // 临时文件夹
	public static String POSPAL_CACHE_ROOT; // 缓存文件夹
	public static String POSPAL_BAK_ROOT; // 备份文件夹
	public static String POSPAL_CONF_ROOT; // 配置文件夹
	public static String POSPAL_DOWNLOAD_ROOT; // 下载文件夹
	public static String POSPAL_CRASH_ROOT; // 崩溃日志文件夹
	public static String POSPAL_IMAGE_ROOT; // 图片文件夹
	public static String POSPAL_RAW_DATA_ROOT; // 内存raw文件夹
	public static String POSPAL_RAW_SD_ROOT; // SD卡raw文件夹
	public static String POSPAL_AD_VIDEO_ROOT; // 广告视频文件夹
	public static String POSPAL_AD_PICTURE_ROOT; // 广告图片文件夹
	public static String POSPAL_AD_SOUND_ROOT; // 广告声音文件夹

	/**
	 * 获取SD卡状态
	 * 
	 * @return SD卡状态代码
	 */
	public static final int SDCARD_STATUE_UNMOUNT = 98701; // SD卡未挂载
	public static final int SDCARD_STATUE_SPACE_ENOUGH = 98702; // SD卡空间足够
	public static final int SDCARD_STATUE_SPACE_WARING = 98703; // SD卡空间不足报警
	public static final int SDCARD_STATUE_SPACE_CLEAR = 98704; // SD卡需要清理

	public static int getSDCardStatue(Context context, boolean needSetPath) {
		String SDCARD_ROOT = "/mnt/SDCard";
		String sdcardStatus = Environment.getExternalStorageState();
		if (sdcardStatus.equals(Environment.MEDIA_MOUNTED)) {
			File sdcardDir = Environment.getExternalStorageDirectory();
			if(needSetPath) {
				SDCARD_ROOT = sdcardDir.getAbsolutePath();
				setAllPaths(SDCARD_ROOT + "/");
			}
			StatFs statFs = new StatFs(sdcardDir.getPath());
			int blockSize = statFs.getBlockSize();
			int freeBockSize = statFs.getAvailableBlocks();
			long freeSpaceSize = blockSize * freeBockSize / 1024 / 1024;

			if (freeSpaceSize > 50) {
				return SDCARD_STATUE_SPACE_ENOUGH;
			} else if (freeSpaceSize > 20) {
				return SDCARD_STATUE_SPACE_WARING;
			} else {
				return SDCARD_STATUE_SPACE_CLEAR;
			}
		} else {
			if(needSetPath) {
				SDCARD_ROOT = context.getFilesDir().getAbsolutePath();
				setAllPaths(SDCARD_ROOT + "/");
			}
		}

		return SDCARD_STATUE_UNMOUNT;
	}
	
	/**
	 * 设置所有路径
	 * @param SDCARD_ROOT
	 */
	private static void setAllPaths(String SDCARD_ROOT) {
		String softwreDir = SystemUtil.getPackageName() + "/";
		// 如果不是程序内部路径则需要指定路径（4.4要求）
		if(!SDCARD_ROOT.equals(ManagerApp.getInstance().getFilesDir().getAbsolutePath())) {
			softwreDir = "Android/data/" + softwreDir;
		}
		D.out("KKKKKK softwreDir = " + softwreDir);
		POSPAL_ROOT = SDCARD_ROOT + softwreDir;
		POSPAL_TMP_ROOT = POSPAL_ROOT + "tmp/";
		POSPAL_CACHE_ROOT = POSPAL_ROOT + ".cache/";
		POSPAL_BAK_ROOT = POSPAL_ROOT + "bak/";
		POSPAL_CONF_ROOT = POSPAL_ROOT + "conf/";
		POSPAL_DOWNLOAD_ROOT = POSPAL_ROOT + "download/";
		POSPAL_CRASH_ROOT = POSPAL_ROOT + ".crash/";
		POSPAL_IMAGE_ROOT = POSPAL_ROOT + ".image/";
		POSPAL_RAW_DATA_ROOT = SDCARD_ROOT + "raw/";
		POSPAL_RAW_SD_ROOT = POSPAL_ROOT + "raw/";
		
		String oldImgDir = POSPAL_ROOT + "image/";
		File oldFile = new File(oldImgDir);
		if(oldFile.exists() && oldFile.isDirectory()) {
			File newFile = new File(POSPAL_IMAGE_ROOT);
			oldFile.renameTo(newFile);
		}
		
		// 创建广告文件夹
        POSPAL_AD_VIDEO_ROOT = POSPAL_ROOT + "AD/video/";
		POSPAL_AD_PICTURE_ROOT = POSPAL_ROOT + "AD/picture/";
		POSPAL_AD_SOUND_ROOT = POSPAL_ROOT + "AD/sound/";
        File adVideoPath = new File(POSPAL_AD_VIDEO_ROOT);
        if(!adVideoPath.exists() || !adVideoPath.isDirectory()) {
            adVideoPath.mkdirs();
        }
		File adPicturePath = new File(POSPAL_AD_PICTURE_ROOT);
		if(!adPicturePath.exists() || !adPicturePath.isDirectory()) {
			adPicturePath.mkdirs();
		}
		File adSoundPath = new File(POSPAL_AD_SOUND_ROOT);
		if(!adSoundPath.exists() || !adSoundPath.isDirectory()) {
			adSoundPath.mkdirs();
		}
		File crashPath = new File(POSPAL_CRASH_ROOT);
		if(!crashPath.exists() || !crashPath.isDirectory()) {
			crashPath.mkdirs();
		}
	}

	/**
	 * 根据文件名路径判断文件是否存在
	 *
	 * @param fileName
	 *            文件绝对路径
	 * @return 文件是否存在
	 */
	public static boolean isFileExist(String fileName) {
		File testFile = new File(fileName);

		if (testFile.exists()) {
			return true;
		}

		return false;
	}

}
