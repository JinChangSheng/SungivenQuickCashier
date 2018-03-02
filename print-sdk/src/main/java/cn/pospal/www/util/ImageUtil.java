package cn.pospal.www.util;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.pospal.www.app.ManagerApp;
import cn.pospal.www.manager.ManagerFile;


/**
 * 图片处理相关工具类
 *
 * @author lin
 */
public class ImageUtil {
	
	/*
	 * Bitmap to Bytes
	 */
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/*
	 * Bytes to Bimap
	 */
	public static Bitmap Bytes2Bimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}
	
	public static void save2File(InputStream inStream, String imageUrl) throws IOException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		//创建一个Buffer字符串 
		byte[] buffer = new byte[1024];
		//每次读取的字符串长度，如果为-1，代表全部读取完毕 
		int len = 0;
		//使用一个输入流从buffer里把数据读取出来 
		while ((len = inStream.read(buffer)) != -1) {
			//用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
			outStream.write(buffer, 0, len);
		}
		//关闭输入流 
		inStream.close();
		//把outStream里的数据写入内存 

		//得到图片的二进制数据，以二进制封装得到数据，具有通用性 
		byte[] data = outStream.toByteArray();
		//new一个文件对象用来保存图片，默认保存当前工程根目录 
		File imageFile = new File(imageUrl);
		//创建输出流 
		FileOutputStream fileOutStream = new FileOutputStream(imageFile);
		//写入数据 
		fileOutStream.write(data);
		fileOutStream.close();

	}

	public static void save2File(Bitmap bitmap, String fileName) throws IOException {
		String path = ManagerFile.POSPAL_ROOT + fileName;
		System.out.println(path);
		try {
			FileOutputStream os = new FileOutputStream(new File(path));
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
			os.flush();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	
	public static byte[] decodeBitmap(String path) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高
		BitmapFactory.decodeFile(path, opts);
		opts.inSampleSize = computeSampleSize(opts, -1, 1024 * 800);
		opts.inJustDecodeBounds = false;// 这里一定要将其设置回false，因为之前我们将其设置成了true
		opts.inPurgeable = true;
		opts.inInputShareable = true;
		opts.inDither = false;
		opts.inPurgeable = true;
		opts.inTempStorage = new byte[16 * 1024];
		FileInputStream is = null;
		Bitmap bmp = null;
		ByteArrayOutputStream baos = null;
		try {
			is = new FileInputStream(path);
			bmp = BitmapFactory.decodeFileDescriptor(is.getFD(), null, opts);
			double scale = getScaling(opts.outWidth * opts.outHeight,
				1024 * 600);
			Bitmap bmp2 = Bitmap.createScaledBitmap(bmp,
				(int) (opts.outWidth * scale),
				(int) (opts.outHeight * scale), true);
			bmp.recycle();
			baos = new ByteArrayOutputStream();
			bmp2.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			bmp2.recycle();
			return baos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.gc();
		}
		return baos.toByteArray();
	}

	private static double getScaling(int src, int des) {
		/**
		 * 48 目标尺寸÷原尺寸 sqrt开方，得出宽高百分比 49
		 */
		double scale = Math.sqrt((double) des / (double) src);
		return scale;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
	                                    int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
			maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
	                                            int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
			.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
			Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * 将图片写入sdcard中
	 *
	 * @param bitmap
	 * @param path   部分URL
	 * @throws Exception
	 */
	public static void saveBitmap2SDCard(Bitmap bitmap, String path) throws Exception {
		// Bitmap 转成byte数组
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] bitmapByte = baos.toByteArray();

		String dirPath = path.substring(0, path.lastIndexOf("/"));
		File dirFile = new File(dirPath);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
			Thread.sleep(50);
		}

		if (!dirFile.exists()) {
			throw new IOException("创建文件夹失败！");
		}

		File writeFile = new File(path);
        boolean isCreateOK = true;
		if (!writeFile.exists()) {
            isCreateOK = writeFile.createNewFile();
            Thread.sleep(50);
		}

        if (isCreateOK) {
            FileOutputStream out;
            out = new FileOutputStream(writeFile);

            out.write(bitmapByte);
            out.flush();
            out.close();
        } else {
            throw new IOException("创建文件失败！");
        }
	}


	public static String getRealPathFromURI(Uri contentUri) {
		Cursor cursor = null;
		try {
			String[] proj = {MediaStore.Images.Media.DATA};
			cursor = ManagerApp.getInstance().getContentResolver().query(contentUri, proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	public static String getRealPathFromUrl(String url) {
		Uri uri = Uri.parse(url);
		return getRealPathFromURI(uri);
	}

    public static String getTargetSizeImgUrl(String imgPath, int targetWidth, int targetHeight) {
        String targetSize = "_" + targetWidth + "X" + targetHeight;
        if (!imgPath.contains(targetSize)) {
            if (imgPath.endsWith(".jpg")) {
                imgPath = imgPath.replace(".jpg", targetSize + ".jpg");
            }
            if (imgPath.endsWith(".JPG")) {
                imgPath = imgPath.replace(".JPG", targetSize + ".JPG");
            }
            if (imgPath.endsWith(".jpeg")) {
                imgPath = imgPath.replace(".jpeg", targetSize + ".jpeg");
            }
            if (imgPath.endsWith(".JPEG")) {
                imgPath = imgPath.replace(".JPEG", targetSize + ".JPEG");
            }
            if (imgPath.endsWith(".png")) {
                imgPath = imgPath.replace(".png", targetSize + ".png");
            }
            if (imgPath.endsWith(".PNG")) {
                imgPath = imgPath.replace(".PNG", targetSize + ".PNG");
            }
            if (imgPath.endsWith(".bmp")) {
                imgPath = imgPath.replace(".bmp", targetSize + ".bmp");
            }
            if (imgPath.endsWith(".BMP")) {
                imgPath = imgPath.replace(".BMP", targetSize + ".BMP");
            }
            if (imgPath.endsWith(".gif")) {
                imgPath = imgPath.replace(".gif", targetSize + ".gif");
            }
            if (imgPath.endsWith(".GIF")) {
                imgPath = imgPath.replace(".GIF", targetSize + ".GIF");
            }
        }

        return imgPath;
    }

    public static String getCoverImgUrl(String imgPath) {
        return getTargetSizeImgUrl(imgPath, 200, 200);
    }

	//获取自助图片
    public static String getHysCoverImgUrl(String imgPath) {
        return getTargetSizeImgUrl(imgPath, 640, 640);
    }

    public static String getPadDetailImgUrl(String imgPath) {
        return getTargetSizeImgUrl(imgPath, 900, 1200);
    }

}
